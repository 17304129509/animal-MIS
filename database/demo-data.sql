INSERT INTO user (username, password, nickname, role) VALUES
('admin001', '{b}$2b$10$4/Wnv19yxe0N1vTRos.UiuivQnTlpaC1Kv3T0SKJ1LikJ5ieZmmyC', '管理员小王', 'admin'),
('vol001', '{b}$2b$10$4/Wnv19yxe0N1vTRos.UiuivQnTlpaC1Kv3T0SKJ1LikJ5ieZmmyC', '志愿者小李', 'volunteer'),
('stu001', '{b}$2b$10$4/Wnv19yxe0N1vTRos.UiuivQnTlpaC1Kv3T0SKJ1LikJ5ieZmmyC', '学生小张', 'student');

INSERT INTO feed_point (point_name, location, food_status) VALUES
('图书馆后投喂点', '图书馆东侧后门台阶旁', '充足'),
('操场西侧投喂点', '操场西侧围墙内草坪', '不足'),
('宿舍区投喂点', '3号宿舍楼后小花园', '空缺');

INSERT INTO animal (animal_name, type, color, location, status, is_approved, create_user, photo_url) VALUES
('橘橘', '猫咪', '橘色', '图书馆附近', '健康', 1, 2, '/uploads/juju.jpg'),
('小黑', '猫咪', '黑色', '操场西侧', '受伤', 1, 2, '/uploads/xiaohui.jpg'),
('旺财', '狗狗', '棕白色', '宿舍区', '健康', 1, 3, '/uploads/wangcai.jpg');

INSERT INTO feed_record (user_id, point_id, food_type) VALUES
(3, 1, '猫粮'),
(3, 2, '狗粮'),
(2, 1, '饮用水');

INSERT INTO rescue_order (animal_id, publish_user, rescue_content, location, order_status) VALUES
(2, 3, '小黑右前腿受伤，走路一瘸一拐，需要救助', '操场西侧草坪', '待处理');
