import "./Register.css";
import {
  withScope as k,
  openBlock as b,
  createElementBlock as v,
  createVNode as o,
  withCtx as a,
  resolveComponent as n,
  useRouter as y,
  createElementVNode as x,
  createTextVNode as i,
  ref as f,
  reactive as C,
  ElMessage as q,
} from "../vendor/vue-runtime.js";
import { storeSession as R } from "../session.js";
import { userApi as U } from "../api.js";
const B = { class: "auth-page" },
  E = {
    __name: "Register",
    setup(N) {
      const _ = y(),
        m = f(),
        d = f(!1),
        l = C({ username: "", password: "", nickname: "", phone: "" }),
        c = {
          username: [{ required: !0, message: "请输入账号" }],
          password: [{ required: !0, message: "请输入密码" }],
          nickname: [{ required: !0, message: "请输入昵称" }],
        };
      async function V() {
        (await m.value.validate(), (d.value = !0));
        try {
          const r = await U.register(l);
          (R(r.token, r), q.success("注册成功"), _.push("/"));
        } finally {
          d.value = !1;
        }
      }
      return (r, e) => {
        const u = n("el-input"),
          t = n("el-form-item"),
          p = n("el-button"),
          w = n("el-form"),
          g = n("el-card");
        return (
          b(),
          v("div", B, [
            o(
              g,
              { class: "auth-card" },
              {
                default: a(() => [
                  e[7] || (e[7] = x("h2", null, "用户注册", -1)),
                  o(
                    w,
                    { model: l, rules: c, ref_key: "formRef", ref: m, "label-width": "80px" },
                    {
                      default: a(() => [
                        o(
                          t,
                          { label: "账号", prop: "username" },
                          {
                            default: a(() => [
                              o(
                                u,
                                {
                                  modelValue: l.username,
                                  "onUpdate:modelValue": e[0] || (e[0] = (s) => (l.username = s)),
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
                          t,
                          { label: "密码", prop: "password" },
                          {
                            default: a(() => [
                              o(
                                u,
                                {
                                  modelValue: l.password,
                                  "onUpdate:modelValue": e[1] || (e[1] = (s) => (l.password = s)),
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
                        o(
                          t,
                          { label: "昵称", prop: "nickname" },
                          {
                            default: a(() => [
                              o(
                                u,
                                {
                                  modelValue: l.nickname,
                                  "onUpdate:modelValue": e[2] || (e[2] = (s) => (l.nickname = s)),
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
                          t,
                          { label: "手机", prop: "phone" },
                          {
                            default: a(() => [
                              o(
                                u,
                                {
                                  modelValue: l.phone,
                                  "onUpdate:modelValue": e[3] || (e[3] = (s) => (l.phone = s)),
                                },
                                null,
                                8,
                                ["modelValue"],
                              ),
                            ]),
                            _: 1,
                          },
                        ),
                        o(t, null, {
                          default: a(() => [
                            o(
                              p,
                              { type: "primary", loading: d.value, onClick: V },
                              { default: a(() => [...(e[5] || (e[5] = [i("注册", -1)]))]), _: 1 },
                              8,
                              ["loading"],
                            ),
                            o(
                              p,
                              {
                                link: "",
                                onClick: e[4] || (e[4] = (s) => r.$router.push("/login")),
                              },
                              {
                                default: a(() => [...(e[6] || (e[6] = [i("已有账号", -1)]))]),
                                _: 1,
                              },
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
                ]),
                _: 1,
              },
            ),
          ])
        );
      };
    },
  },
  I = k(E, [["__scopeId", "data-v-b8fa21b8"]]);
export { I as default };
