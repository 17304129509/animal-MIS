import axios from "./vendor/axios.js";
import { getToken as Dt, clearSession as Lt } from "./session.js";
import { ElMessage as Oe } from "./vendor/vue-runtime.js";
import { router as Ut } from "./router.js";
const b = axios.create({ baseURL: "/api", timeout: 15e3 });
b.interceptors.request.use((e) => {
  const t = Dt();
  return (t && (e.headers.Authorization = `Bearer ${t}`), e);
});
b.interceptors.response.use(
  (e) => {
    const t = e.data;
    return t.code !== 200 ? (Oe.error(t.msg || "请求失败"), Promise.reject(t)) : t.data;
  },
  (e) => {
    var n, r, s;
    const t =
      ((r = (n = e.response) == null ? void 0 : n.data) == null ? void 0 : r.msg) ||
      e.message ||
      "网络错误";
    return (
      ((s = e.response) == null ? void 0 : s.status) === 403
        ? (Oe.error(t), t.includes("登录") && (Lt(), Ut.push("/login")))
        : Oe.error(t),
      Promise.reject(e)
    );
  },
);
const Kr = {
    register: (e) => b.post("/user/register", e),
    login: (e) => b.post("/user/login", e),
    info: () => b.get("/user/info"),
    updateInfo: (e) => b.put("/user/info", e),
    changePassword: (e) => b.put("/user/password", e),
    listUsers: (e) => b.get("/admin/users", { params: e }),
    updateRole: (e, t) => b.put(`/admin/users/${e}/role`, { role: t }),
  },
  Xr = {
    list: (e) => b.get("/animal", { params: e }),
    get: (e) => b.get(`/animal/${e}`),
    create: (e) => b.post("/animal", e),
    update: (e, t) => b.put(`/animal/${e}`, t),
    updateStatus: (e, t) => b.patch(`/animal/${e}/status`, { status: t }),
    approve: (e, t) => b.patch(`/animal/${e}/approve`, { approved: t }),
    remove: (e) => b.delete(`/animal/${e}`),
  },
  Gr = {
    list: (e) => b.get("/feed-point", { params: e }),
    get: (e) => b.get(`/feed-point/${e}`),
    create: (e) => b.post("/feed-point", e),
    updateStatus: (e, t) => b.patch(`/feed-point/${e}/status`, { foodStatus: t }),
    remove: (e) => b.delete(`/feed-point/${e}`),
  },
  Qr = {
    create: (e) => b.post("/feed-record", e),
    my: (e) => b.get("/feed-record/my", { params: e }),
    byPoint: (e, t) => b.get(`/feed-record/point/${e}`, { params: t }),
    stats: () => b.get("/feed-record/stats"),
  },
  Zr = {
    list: (e) => b.get("/rescue", { params: e }),
    get: (e) => b.get(`/rescue/${e}`),
    create: (e) => b.post("/rescue", e),
    claim: (e) => b.patch(`/rescue/${e}/claim`),
    updateStatus: (e, t) => b.patch(`/rescue/${e}/status`, t),
    close: (e, t) => b.patch(`/rescue/${e}/close`, t),
    my: (e) => b.get("/rescue/my", { params: e }),
  },
  Yr = {
    feed: (e) => b.get("/rank/feed", { params: { limit: e } }),
    rescue: (e) => b.get("/rank/rescue", { params: { limit: e } }),
  },
  es = {
    upload: (e) => {
      const t = new FormData();
      return (
        t.append("file", e),
        b.post("/upload", t, { headers: { "Content-Type": "multipart/form-data" } })
      );
    },
  },
  ts = {
    dashboard: () => b.get("/stats/dashboard"),
    weeklyFeeding: () => b.get("/stats/weekly-feeding"),
    foodStatus: () => b.get("/stats/food-status"),
    rescueRate: () => b.get("/stats/rescue-rate"),
  },
  ns = {
    apply: (e) => b.post("/adoption", e),
    list: (e) => b.get("/adoption", { params: e }),
    my: (e) => b.get("/adoption/my", { params: e }),
    get: (e) => b.get(`/adoption/${e}`),
    review: (e, t) => b.patch(`/adoption/${e}/review`, t),
  };
export {
  Xr as animalApi,
  Qr as feedingApi,
  ns as adoptionApi,
  Yr as rankApi,
  Kr as userApi,
  Gr as feedPointApi,
  Zr as rescueApi,
  ts as statsApi,
  es as uploadApi,
};
