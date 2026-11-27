import "./AnimalDetail.css";
import {
  withScope as A,
  onMounted as U,
  withDirectives as z,
  openBlock as i,
  createBlock as F,
  withCtx as a,
  createCommentVNode as k,
  ref as f,
  resolveDirective as M,
  resolveComponent as r,
  useRoute as R,
  createElementVNode as p,
  normalizeStyle as $,
  createElementBlock as _,
  toDisplayString as o,
  createVNode as t,
  createTextVNode as s,
  Fragment as I,
  renderList as L,
  computed as q,
  ElMessage as G,
} from "../vendor/vue-runtime.js";
import { hasRole as H } from "../session.js";
import { animalApi as g } from "../api.js";
const J = { class: "header" },
  K = { class: "photo-section" },
  O = ["src"],
  P = { key: 1, class: "photo-emoji" },
  Q = { key: 0, class: "actions" },
  W = {
    __name: "AnimalDetail",
    setup(X) {
      const v = R(),
        e = f(null),
        m = f(!1),
        d = f(""),
        h = ["健康", "受伤", "康复", "已领养"],
        w = q(() => H("volunteer", "admin"));
      function x(n) {
        return { 猫咪: "#fef0ef", 狗狗: "#f0f5ef", 其他: "#f0f0f5" }[n] || "#f5f7fa";
      }
      function C(n) {
        return { 猫咪: "🐱", 狗狗: "🐶", 其他: "🐾" }[n] || "🐾";
      }
      function V(n) {
        return n ? n.replace("T", " ") : "";
      }
      async function y() {
        m.value = !0;
        try {
          ((e.value = await g.get(v.params.id)), (d.value = e.value.status));
        } finally {
          m.value = !1;
        }
      }
      async function N() {
        (await g.updateStatus(v.params.id, d.value), G.success("状态已更新"), y());
      }
      return (
        U(y),
        (n, l) => {
          const b = r("el-button"),
            u = r("el-descriptions-item"),
            D = r("el-tag"),
            E = r("el-descriptions"),
            S = r("el-option"),
            B = r("el-select"),
            T = r("el-card"),
            j = M("loading");
          return e.value
            ? z(
                (i(),
                F(
                  T,
                  { key: 0 },
                  {
                    header: a(() => [
                      p("div", J, [
                        p("span", null, o(e.value.animalName) + " 档案详情", 1),
                        t(
                          b,
                          { link: "", onClick: l[0] || (l[0] = (c) => n.$router.back()) },
                          { default: a(() => [...(l[2] || (l[2] = [s("返回", -1)]))]), _: 1 },
                        ),
                      ]),
                    ]),
                    default: a(() => [
                      p("div", K, [
                        p(
                          "div",
                          { class: "photo-box", style: $({ backgroundColor: x(e.value.type) }) },
                          [
                            e.value.photoUrl
                              ? (i(),
                                _(
                                  "img",
                                  { key: 0, src: e.value.photoUrl, class: "photo" },
                                  null,
                                  8,
                                  O,
                                ))
                              : (i(), _("span", P, o(C(e.value.type)), 1)),
                          ],
                          4,
                        ),
                      ]),
                      t(
                        E,
                        { column: 2, border: "" },
                        {
                          default: a(() => [
                            t(
                              u,
                              { label: "类型" },
                              { default: a(() => [s(o(e.value.type), 1)]), _: 1 },
                            ),
                            t(
                              u,
                              { label: "毛色" },
                              { default: a(() => [s(o(e.value.color || "-"), 1)]), _: 1 },
                            ),
                            t(
                              u,
                              { label: "位置" },
                              { default: a(() => [s(o(e.value.location), 1)]), _: 1 },
                            ),
                            t(
                              u,
                              { label: "状态" },
                              {
                                default: a(() => [
                                  t(D, null, { default: a(() => [s(o(e.value.status), 1)]), _: 1 }),
                                ]),
                                _: 1,
                              },
                            ),
                            t(
                              u,
                              { label: "建档人" },
                              { default: a(() => [s(o(e.value.creatorNickname), 1)]), _: 1 },
                            ),
                            t(
                              u,
                              { label: "建档时间" },
                              { default: a(() => [s(o(V(e.value.createTime)), 1)]), _: 1 },
                            ),
                            t(
                              u,
                              { label: "备注", span: 2 },
                              { default: a(() => [s(o(e.value.remark || "-"), 1)]), _: 1 },
                            ),
                          ]),
                          _: 1,
                        },
                      ),
                      w.value
                        ? (i(),
                          _("div", Q, [
                            t(
                              B,
                              {
                                modelValue: d.value,
                                "onUpdate:modelValue": l[1] || (l[1] = (c) => (d.value = c)),
                                placeholder: "更新状态",
                                style: { width: "140px" },
                              },
                              {
                                default: a(() => [
                                  (i(),
                                  _(
                                    I,
                                    null,
                                    L(h, (c) =>
                                      t(S, { key: c, label: c, value: c }, null, 8, [
                                        "label",
                                        "value",
                                      ]),
                                    ),
                                    64,
                                  )),
                                ]),
                                _: 1,
                              },
                              8,
                              ["modelValue"],
                            ),
                            t(
                              b,
                              { type: "primary", onClick: N },
                              {
                                default: a(() => [...(l[3] || (l[3] = [s("更新状态", -1)]))]),
                                _: 1,
                              },
                            ),
                          ]))
                        : k("", !0),
                    ]),
                    _: 1,
                  },
                )),
                [[j, m.value]],
              )
            : k("", !0);
        }
      );
    },
  },
  ee = A(W, [["__scopeId", "data-v-9f8c7af2"]]);
export { ee as default };
