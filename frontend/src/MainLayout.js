import {
  useRoute as rie,
  useRouter as oie,
  computed as S,
  resolveComponent as Ot,
  openBlock as w,
  createBlock as ue,
  withCtx as ne,
  createVNode as J,
  createElementVNode as E,
  createTextVNode as ct,
  createCommentVNode as oe,
  createElementBlock as O,
  Fragment as Fe,
  toDisplayString as xe,
  withScope as Xw,
} from "./vendor/vue-runtime.js";
import { getUser as jd, clearSession as sie } from "./session.js";
const iie = { class: "user-area" },
  uie = { class: "love-points" },
  cie = {
    __name: "MainLayout",
    setup(e) {
      const t = rie(),
        n = oie(),
        a = S(() => jd()),
        l = S(() => "/" + (t.path.split("/")[1] || "")),
        o = S(() => {
          var u;
          return (
            { student: "学生", volunteer: "志愿者", admin: "管理员" }[
              (u = a.value) == null ? void 0 : u.role
            ] || ""
          );
        });
      function r() {
        (sie(), n.push("/login"));
      }
      return (s, u) => {
        const c = Ot("Promotion"),
          d = Ot("el-icon"),
          f = Ot("el-menu-item"),
          p = Ot("el-menu"),
          g = Ot("el-tag"),
          v = Ot("el-button"),
          m = Ot("el-header"),
          h = Ot("router-view"),
          y = Ot("el-main"),
          b = Ot("el-footer"),
          C = Ot("el-container");
        return (
          w(),
          ue(
            C,
            { class: "layout" },
            {
              default: ne(() => [
                J(
                  m,
                  { class: "header" },
                  {
                    default: ne(() => [
                      E(
                        "div",
                        { class: "brand", onClick: u[0] || (u[0] = (_) => s.$router.push("/")) },
                        [
                          J(d, null, { default: ne(() => [J(c)]), _: 1 }),
                          u[4] || (u[4] = E("span", null, "校园流浪动物救助", -1)),
                        ],
                      ),
                      J(
                        p,
                        {
                          mode: "horizontal",
                          ellipsis: !1,
                          router: "",
                          "default-active": l.value,
                          class: "nav-menu",
                        },
                        {
                          default: ne(() => {
                            var _;
                            return [
                              J(
                                f,
                                { index: "/" },
                                {
                                  default: ne(() => [...(u[5] || (u[5] = [ct("首页", -1)]))]),
                                  _: 1,
                                },
                              ),
                              J(
                                f,
                                { index: "/animals" },
                                {
                                  default: ne(() => [...(u[6] || (u[6] = [ct("动物档案", -1)]))]),
                                  _: 1,
                                },
                              ),
                              J(
                                f,
                                { index: "/feed-points" },
                                {
                                  default: ne(() => [...(u[7] || (u[7] = [ct("投喂点位", -1)]))]),
                                  _: 1,
                                },
                              ),
                              J(
                                f,
                                { index: "/rescue" },
                                {
                                  default: ne(() => [...(u[8] || (u[8] = [ct("救助工单", -1)]))]),
                                  _: 1,
                                },
                              ),
                              J(
                                f,
                                { index: "/adoption" },
                                {
                                  default: ne(() => [...(u[9] || (u[9] = [ct("动物领养", -1)]))]),
                                  _: 1,
                                },
                              ),
                              J(
                                f,
                                { index: "/rank" },
                                {
                                  default: ne(() => [...(u[10] || (u[10] = [ct("公益榜单", -1)]))]),
                                  _: 1,
                                },
                              ),
                              ((_ = a.value) == null ? void 0 : _.role) === "admin"
                                ? (w(),
                                  ue(
                                    f,
                                    { key: 0, index: "/admin" },
                                    {
                                      default: ne(() => [
                                        ...(u[11] || (u[11] = [ct("管理后台", -1)])),
                                      ]),
                                      _: 1,
                                    },
                                  ))
                                : oe("", !0),
                            ];
                          }),
                          _: 1,
                        },
                        8,
                        ["default-active"],
                      ),
                      E("div", iie, [
                        a.value
                          ? (w(),
                            O(
                              Fe,
                              { key: 0 },
                              [
                                J(
                                  g,
                                  { size: "small", type: "success" },
                                  { default: ne(() => [ct(xe(o.value), 1)]), _: 1 },
                                ),
                                E("span", uie, [
                                  u[12] || (u[12] = E("span", { class: "heart" }, "❤", -1)),
                                  ct(" " + xe(a.value.lovePoints) + " 积分 ", 1),
                                ]),
                                J(
                                  v,
                                  {
                                    link: "",
                                    onClick: u[1] || (u[1] = (_) => s.$router.push("/profile")),
                                  },
                                  { default: ne(() => [ct(xe(a.value.nickname), 1)]), _: 1 },
                                ),
                                J(
                                  v,
                                  { link: "", type: "danger", onClick: r },
                                  {
                                    default: ne(() => [...(u[13] || (u[13] = [ct("退出", -1)]))]),
                                    _: 1,
                                  },
                                ),
                              ],
                              64,
                            ))
                          : (w(),
                            O(
                              Fe,
                              { key: 1 },
                              [
                                J(
                                  v,
                                  {
                                    type: "primary",
                                    link: "",
                                    onClick: u[2] || (u[2] = (_) => s.$router.push("/login")),
                                  },
                                  {
                                    default: ne(() => [...(u[14] || (u[14] = [ct("登录", -1)]))]),
                                    _: 1,
                                  },
                                ),
                                J(
                                  v,
                                  {
                                    link: "",
                                    onClick: u[3] || (u[3] = (_) => s.$router.push("/register")),
                                  },
                                  {
                                    default: ne(() => [...(u[15] || (u[15] = [ct("注册", -1)]))]),
                                    _: 1,
                                  },
                                ),
                              ],
                              64,
                            )),
                      ]),
                    ]),
                    _: 1,
                  },
                ),
                J(y, null, { default: ne(() => [J(h)]), _: 1 }),
                J(
                  b,
                  { class: "footer" },
                  {
                    default: ne(() => [
                      ...(u[16] ||
                        (u[16] = [ct("校园流浪动物投喂与救助管理系统 · 公益校园项目", -1)])),
                    ]),
                    _: 1,
                  },
                ),
              ]),
              _: 1,
            },
          )
        );
      };
    },
  },
  die = Xw(cie, [["__scopeId", "data-v-b1bf5852"]]);
export default die;
