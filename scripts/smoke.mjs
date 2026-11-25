import assert from 'node:assert/strict';
import { randomUUID } from 'node:crypto';
import { readFile } from 'node:fs/promises';

const userHost = process.env.USER_URL ?? 'http://127.0.0.1:8081';
const adminHost = process.env.ADMIN_URL ?? 'http://127.0.0.1:8082';
const suffix = randomUUID().slice(0, 8);
let checks = 0;

async function api(host, path, { method = 'GET', token, body, code = 200 } = {}) {
  const headers = token ? { Authorization: 'Bearer ' + token } : {};
  if (body && !(body instanceof FormData)) headers['Content-Type'] = 'application/json';
  const response = await fetch(host + path, {
    method, headers, body: headers['Content-Type'] ? JSON.stringify(body) : body,
  });
  const result = await response.json();
  assert.equal(response.status, code, `${method} ${path}: ${result.msg}`);
  assert.equal(result.code, code, `${method} ${path}: ${result.msg}`);
  assert.equal(result.msg, result.message);
  checks++;
  return result.data;
}

const user = (path, opts) => api(userHost, '/api' + path, opts);
const admin = (path, opts) => api(adminHost, '/api/admin' + path, opts);
const post = (body, token, code) => ({ method: 'POST', body, token, code });
const put = (body, token, code) => ({ method: 'PUT', body, token, code });
const patch = (body, token, code) => ({ method: 'PATCH', body, token, code });
const demoLogin = username => post({ username, password: '123456' });

for (const [url, marker] of [[userHost, '/assets/'], [adminHost, 'admin_token']]) {
  const page = await fetch(url);
  assert.equal(page.status, 200);
  assert.ok((await page.text()).includes(marker));
  checks++;
}

const { token: studentDemo } = await user('/user/login', demoLogin('stu001'));
const { token: volunteer } = await user('/user/login', demoLogin('vol001'));
const { token: manager } = await admin('/login', demoLogin('admin001'));
await admin('/login', { ...demoLogin('stu001'), code: 400 });
await admin('/stats', { code: 401 });
await admin('/stats', { token: 'invalid', code: 401 });
await admin('/stats', { token: studentDemo, code: 403 });
await admin('/stats', { token: volunteer, code: 403 });
assert.equal((await admin('/info', { token: manager })).role, 'admin');

const account = { username: 'qa_' + suffix, nickname: '验收用户', password: 'Test-Only-834' };
const { token: student, userId } = await user('/user/register', post(account));
await user('/user/register', post(account, null, 400));
await user('/user/info', put({ nickname: '验收用户改' }, student));
assert.equal((await user('/user/info', { token: student })).nickname, '验收用户改');
await user('/user/password', put({ oldPassword: account.password, newPassword: 'Test-Only-835' }, student));
await user('/user/login', post({ username: account.username, password: account.password }, null, 400));
await user('/user/login', post({ username: account.username, password: 'Test-Only-835' }));

const photo = new FormData();
photo.set('file', new Blob([await readFile(new URL('../src/main/resources/static/uploads/juju.jpg', import.meta.url))],
  { type: 'image/jpeg' }), 'sample.jpg');
const { url: photoUrl } = await user('/upload', post(photo, student));
assert.equal((await fetch(userHost + photoUrl)).headers.get('content-type'), 'image/jpeg');
assert.equal((await fetch(userHost + '/uploads/juju.jpg')).status, 200);
await user('/upload', post(null, student, 400));

const animalRequest = { animalName: '验收猫' + suffix, type: '猫咪', location: '联调' + suffix, photoUrl };
const { animalId } = await user('/animal', post(animalRequest, student));
assert.equal((await user('/animal/' + animalId, { token: student })).isApproved, false);
await user('/animal/' + animalId, { code: 404 });
await admin('/animals/' + animalId + '/approve', put(null, student, 403));
assert.ok((await admin('/animals?status=pending', { token: manager })).some(row => row.animal_id === animalId));
await admin('/animals/' + animalId + '/reject', put({ reason: '核对资料' }, manager));
await admin('/animals/' + animalId + '/approve', put(null, manager));
const filtered = new URLSearchParams({ type: '猫咪', status: '健康', location: suffix, page: '1', pageSize: '1' });
const animals = await user('/animal?' + filtered);
assert.equal(animals.total, 1);
assert.equal(animals.list[0].animalId, animalId);
assert.equal((await fetch(userHost + '/animals/' + animalId)).status, 200);

const adminAnimal = { animal_name: '后台档案' + suffix, type: '狗', gender: '公', health: '健康', location: '行政楼' };
const { id: otherId } = await admin('/animals/create', post(adminAnimal, manager));
await admin('/animals/' + otherId + '/edit', put({ ...adminAnimal, health: '轻微受伤' }, manager));
assert.equal((await admin('/animals?status=approved', { token: manager }))
  .find(row => row.animal_id === otherId).health, '轻微受伤');
await user('/animal/' + otherId + '/status', patch({ status: '康复' }, volunteer));
assert.equal((await user('/animal/' + otherId)).status, '康复');
const volunteerReview = await user('/adoption',
  post({ animalId: otherId, reason: '志愿者审核检查' }, student));
await user('/adoption/' + volunteerReview.id + '/review', patch({ status: '已通过' }, volunteer));
assert.equal((await user('/user/info', { token: student })).lovePoints, 150);
const { id: unusedId } = await admin('/animals/create', post({ ...adminAnimal, animal_name: '待删除' + suffix }, manager));
await admin('/animals/' + unusedId, { method: 'DELETE', token: manager });

const point = { point_name: '投喂点' + suffix, location: '东门', food_status: '充足' };
await admin('/feed-points', post(point, manager));
const findPoint = async () => (await admin('/feed-points', { token: manager })).find(row => row.point_name === point.point_name);
const { point_id: pointId } = await findPoint();
await admin('/feed-points/' + pointId, put({ ...point, food_status: '不足' }, manager));
await user('/feed-point/' + pointId + '/status', patch({ foodStatus: '充足' }, volunteer));
assert.equal((await findPoint()).food_status, '充足');
await user('/feed-record', post({ pointId, foodType: '猫粮', amount: 5, quantity: 2 }, student));
assert.equal((await user('/user/info', { token: student })).lovePoints, 140);
await user('/feed-record', post({ pointId, foodType: '猫粮', amount: 1, remark: '长'.repeat(201) }, student, 400));
assert.equal((await user('/user/info', { token: student })).lovePoints, 140, 'failed feeding must roll back points');
assert.equal((await user('/feed-record/my', { token: student })).total, 1);
assert.equal((await user('/feed-record/point/' + pointId)).total, 1);
assert.equal((await user('/feed-point/' + pointId)).recentRecords.length, 1);
assert.ok((await admin('/feed-records', { token: manager })).some(row => row.point_id === pointId));

const job = { animalId, rescueContent: '联调救助', location: '东门' };
const { rescueId } = await user('/rescue', post(job, student));
await user('/rescue/' + rescueId + '/claim', patch(null, student, 403));
const claims = await Promise.allSettled([
  user('/rescue/' + rescueId + '/claim', patch(null, volunteer)),
  user('/rescue/' + rescueId + '/claim', patch(null, volunteer)),
]);
assert.equal(claims.filter(row => row.status === 'fulfilled').length, 1);
assert.match(claims.find(row => row.status === 'rejected').reason.message, /仅待处理/);
assert.ok((await admin('/rescues?status=processing', { token: manager })).some(row => row.rescue_id === rescueId));
await admin('/rescues/' + rescueId + '/complete', put({ result: '送医完成' }, manager));
await user('/rescue/' + rescueId + '/status', patch({ orderStatus: '救助中' }, volunteer, 400));
assert.equal((await user('/rescue/my', { token: student })).list[0].orderStatus, '已完成');
const { id: adminJob } = await admin('/rescues/create', post({ animal_id: String(animalId),
  rescue_content: '重复报告', location: '东门', urgency: '紧急' }, manager));
await admin('/rescues/' + adminJob + '/reject', put({ reason: '重复' }, manager));
assert.equal((await user('/rescue/' + adminJob)).orderStatus, '已驳回');
const { id: activeAdminJob } = await admin('/rescues/create', post({ rescue_content: '后台接单', location: '东门' }, manager));
await admin('/rescues/' + activeAdminJob + '/process', put(null, manager));
assert.equal((await user('/rescue/' + activeAdminJob)).orderStatus, '救助中');
await admin('/rescues/' + activeAdminJob + '/complete', put({}, manager));

const request = { animalId, reason: '可以长期照顾', contact: '验收联系方式' };
const first = await user('/adoption', post(request, student));
const second = await user('/adoption', post(request, studentDemo));
await admin('/adoptions/' + first.id + '/approve', put({ comment: '资料已核对' }, manager));
await admin('/adoptions/' + first.id + '/approve', put({}, manager, 400));
await admin('/adoptions/' + second.id + '/approve', put({}, manager, 400));
assert.equal((await user('/adoption/' + second.id, { token: studentDemo })).status, '待审核');
await admin('/adoptions/' + second.id + '/reject', put({ reason: '本轮名额已用' }, manager));
assert.equal((await user('/animal/' + animalId)).status, '已领养');
assert.equal((await user('/adoption/my', { token: student })).list[0].status, '已通过');
assert.ok((await admin('/adoptions?status=rejected', { token: manager })).some(row => row.id === second.id));

for (const path of ['/stats/dashboard', '/stats/weekly-feeding', '/stats/food-status',
  '/stats/rescue-rate', '/rank/feed', '/rank/rescue']) {
  await user(path, { token: student });
}
assert.ok((await admin('/users', { token: manager })).some(row => row.user_id === userId));
assert.ok((await user('/admin/users?role=student', { token: manager })).list.some(row => row.userId === userId));
await admin('/users/' + userId + '/role', put({ role: 'volunteer' }, manager));
assert.equal((await user('/user/info', { token: student })).role, 'volunteer');
assert.ok((await admin('/volunteers', { token: manager })).some(row => row.user_id === userId));
console.log(`通过 ${checks} 项 HTTP 响应检查，以及业务状态、事务回滚和并发断言。`);
