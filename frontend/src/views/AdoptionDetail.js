import "./AdoptionDetail.css";
import {
  withScope as U,
  onMounted as j,
  withDirectives as S,
  openBlock as r,
  createBlock as d,
  withCtx as a,
  resolveDirective as q,
  ref as m,
  resolveComponent as i,
  useRoute as z,
  createVNode as l,
  createTextVNode as o,
  toDisplayString as s,
  createCommentVNode as v,
  createElementBlock as F,
  createElementVNode as _,
  computed as G,
  ElMessage as H,
} from "../vendor/vue-runtime.js";
import { getUser as J } from "../session.js";
import { adoptionApi as N } from "../api.js";
const K = { class: "header" },
  L = { key: 1, class: "review-section" },
  O = { class: "actions" },
  P = {
    __name: "AdoptionDetail",
    setup(Q) {
      const w = z(),
        b = G(() => J()),
        t = m({}),
        f = m(!1),
        p = m(!1),
        y = m("");
      function T(n) {
        return { 待审核: "warning", 已通过: "success", 已拒绝: "danger" }[n] || "info";
      }
      function k(n) {
        return n ? n.replace("T", " ") : "";
      }
      async function C() {
        f.value = !0;
        try {
          t.value = await N.get(w.params.id);
        } finally {
          f.value = !1;
        }
      }
      async function x(n) {
        p.value = !0;
        try {
          (await N.review(w.params.id, { status: n, reviewComment: y.value }),
            H.success(n === "已通过" ? "已通过领养申请，申请人获得50积分奖励" : "已拒绝领养申请"),
            C());
        } finally {
          p.value = !1;
        }
      }
      return (
        j(C),
        (n, e) => {
          const B = i("el-tag"),
            u = i("el-descriptions-item"),
            $ = i("el-descriptions"),
            A = i("el-divider"),
            E = i("el-input"),
            g = i("el-button"),
            I = i("el-space"),
            M = i("el-card"),
            R = q("loading");
          return S(
            (r(),
            d(M, null, {
              header: a(() => [
                _("div", K, [
                  e[4] || (e[4] = _("span", null, "领养申请详情", -1)),
                  l(
                    B,
                    { type: T(t.value.status) },
                    { default: a(() => [o(s(t.value.status), 1)]), _: 1 },
                    8,
                    ["type"],
                  ),
                ]),
              ]),
              default: a(() => {
                var V, D;
                return [
                  t.value.id
                    ? (r(),
                      d(
                        $,
                        { key: 0, column: 2, border: "" },
                        {
                          default: a(() => [
                            l(
                              u,
                              { label: "申请人" },
                              { default: a(() => [o(s(t.value.applicantName), 1)]), _: 1 },
                            ),
                            l(
                              u,
                              { label: "动物ID" },
                              { default: a(() => [o(s(t.value.animalId), 1)]), _: 1 },
                            ),
                            l(
                              u,
                              { label: "联系方式" },
                              { default: a(() => [o(s(t.value.contact || "未填写"), 1)]), _: 1 },
                            ),
                            l(
                              u,
                              { label: "申请时间" },
                              { default: a(() => [o(s(k(t.value.applyTime)), 1)]), _: 1 },
                            ),
                            l(
                              u,
                              { label: "申请理由", span: 2 },
                              { default: a(() => [o(s(t.value.reason), 1)]), _: 1 },
                            ),
                            t.value.status !== "待审核"
                              ? (r(),
                                d(
                                  u,
                                  { key: 0, label: "审核人" },
                                  { default: a(() => [o(s(t.value.reviewerName), 1)]), _: 1 },
                                ))
                              : v("", !0),
                            t.value.status !== "待审核"
                              ? (r(),
                                d(
                                  u,
                                  { key: 1, label: "审核时间" },
                                  { default: a(() => [o(s(k(t.value.reviewTime)), 1)]), _: 1 },
                                ))
                              : v("", !0),
                            t.value.reviewComment
                              ? (r(),
                                d(
                                  u,
                                  { key: 2, label: "审核意见", span: 2 },
                                  { default: a(() => [o(s(t.value.reviewComment), 1)]), _: 1 },
                                ))
                              : v("", !0),
                          ]),
                          _: 1,
                        },
                      ))
                    : v("", !0),
                  t.value.status === "待审核" &&
                  (((V = b.value) == null ? void 0 : V.role) === "admin" ||
                    ((D = b.value) == null ? void 0 : D.role) === "volunteer")
                    ? (r(),
                      F("div", L, [
                        l(A),
                        e[7] || (e[7] = _("h4", null, "审核操作", -1)),
                        l(
                          E,
                          {
                            modelValue: y.value,
                            "onUpdate:modelValue": e[0] || (e[0] = (c) => (y.value = c)),
                            type: "textarea",
                            rows: 2,
                            placeholder: "审核意见（可选）",
                            style: { "margin-bottom": "12px" },
                          },
                          null,
                          8,
                          ["modelValue"],
                        ),
                        l(I, null, {
                          default: a(() => [
                            l(
                              g,
                              {
                                type: "success",
                                loading: p.value,
                                onClick: e[1] || (e[1] = (c) => x("已通过")),
                              },
                              {
                                default: a(() => [...(e[5] || (e[5] = [o("通过申请", -1)]))]),
                                _: 1,
                              },
                              8,
                              ["loading"],
                            ),
                            l(
                              g,
                              {
                                type: "danger",
                                loading: p.value,
                                onClick: e[2] || (e[2] = (c) => x("已拒绝")),
                              },
                              {
                                default: a(() => [...(e[6] || (e[6] = [o("拒绝申请", -1)]))]),
                                _: 1,
                              },
                              8,
                              ["loading"],
                            ),
                          ]),
                          _: 1,
                        }),
                      ]))
                    : v("", !0),
                  _("div", O, [
                    l(
                      g,
                      { onClick: e[3] || (e[3] = (c) => n.$router.back()) },
                      { default: a(() => [...(e[8] || (e[8] = [o("返回", -1)]))]), _: 1 },
                    ),
                  ]),
                ];
              }),
              _: 1,
            })),
            [[R, f.value]],
          );
        }
      );
    },
  },
  Y = U(P, [["__scopeId", "data-v-6153d215"]]);
export { Y as default };
