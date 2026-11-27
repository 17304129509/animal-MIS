import "./Login.css";
import {
  withScope as b,
  openBlock as k,
  createElementBlock as x,
  createVNode as o,
  withCtx as t,
  resolveComponent as a,
  useRoute as C,
  useRouter as q,
  createElementVNode as R,
  createTextVNode as i,
  ref as f,
  reactive as B,
  ElMessage as N,
} from "../vendor/vue-runtime.js";
import { storeSession as E } from "../session.js";
import { userApi as A } from "../api.js";
const L = { class: "auth-page" },
  U = {
    __name: "Login",
    setup($) {
      const _ = C(),
        c = q(),
        d = f(),
        n = f(!1),
        s = B({ username: "", password: "" }),
        w = {
          username: [{ required: !0, message: "请输入账号" }],
          password: [{ required: !0, message: "请输入密码" }],
        };
      async function g() {
        (await d.value.validate(), (n.value = !0));
        try {
          const l = await A.login(s);
          (E(l.token, l), N.success("登录成功"), c.push(_.query.redirect || "/"));
        } finally {
          n.value = !1;
        }
      }
      return (l, e) => {
        const m = a("el-input"),
          u = a("el-form-item"),
          p = a("el-button"),
          v = a("el-form"),
          y = a("el-alert"),
          V = a("el-card");
        return (
          k(),
          x("div", L, [
            o(
              V,
              { class: "auth-card" },
              {
                default: t(() => [
                  e[5] || (e[5] = R("h2", null, "用户登录", -1)),
                  o(
                    v,
                    { model: s, rules: w, ref_key: "formRef", ref: d, "label-width": "80px" },
                    {
                      default: t(() => [
                        o(
                          u,
                          { label: "账号", prop: "username" },
                          {
                            default: t(() => [
                              o(
                                m,
                                {
                                  modelValue: s.username,
                                  "onUpdate:modelValue": e[0] || (e[0] = (r) => (s.username = r)),
                                  placeholder: "学号或用户名",
                                },
                                null,
                                8,
                                ["modelValue"],
                              ),
                            ]),
                            _: 1,
                          },
                        ),
                        o(
                          u,
                          { label: "密码", prop: "password" },
                          {
                            default: t(() => [
                              o(
                                m,
                                {
                                  modelValue: s.password,
                                  "onUpdate:modelValue": e[1] || (e[1] = (r) => (s.password = r)),
                                  type: "password",
                                  "show-password": "",
                                },
                                null,
                                8,
                                ["modelValue"],
                              ),
                            ]),
                            _: 1,
                          },
                        ),
                        o(u, null, {
                          default: t(() => [
                            o(
                              p,
                              { type: "primary", loading: n.value, onClick: g },
                              { default: t(() => [...(e[3] || (e[3] = [i("登录", -1)]))]), _: 1 },
                              8,
                              ["loading"],
                            ),
                            o(
                              p,
                              {
                                link: "",
                                onClick: e[2] || (e[2] = (r) => l.$router.push("/register")),
                              },
                              { default: t(() => [...(e[4] || (e[4] = [i("去注册", -1)]))]), _: 1 },
                            ),
                          ]),
                          _: 1,
                        }),
                      ]),
                      _: 1,
                    },
                    8,
                    ["model"],
                  ),
                  o(y, {
                    type: "info",
                    closable: !1,
                    title: "测试账号：admin001 / vol001 / stu001，密码均为 123456",
                  }),
                ]),
                _: 1,
              },
            ),
          ])
        );
      };
    },
  },
  T = b(U, [["__scopeId", "data-v-efa2a1af"]]);
export { T as default };
