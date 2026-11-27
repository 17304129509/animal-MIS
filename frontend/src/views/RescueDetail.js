import "./RescueDetail.css";
import {
  withScope as j,
  onMounted as z,
  withDirectives as O,
  openBlock as m,
  createBlock as k,
  withCtx as a,
  createCommentVNode as _,
  ref as v,
  resolveDirective as q,
  resolveComponent as u,
  useRoute as G,
  createVNode as t,
  createTextVNode as s,
  toDisplayString as n,
  createElementVNode as w,
  createElementBlock as H,
  Fragment as J,
  computed as S,
  ElMessage as V,
} from "../vendor/vue-runtime.js";
import { hasRole as x } from "../session.js";
import { rescueApi as f } from "../api.js";
const K = { class: "header" },
  L = { class: "actions" },
  Q = {
    __name: "RescueDetail",
    setup(W) {
      const d = G(),
        e = v(null),
        y = v(!1),
        i = v("救助中"),
        c = v(""),
        N = S(() => {
          var o;
          return (
            x("volunteer", "admin") && ((o = e.value) == null ? void 0 : o.orderStatus) === "待处理"
          );
        }),
        D = S(() => {
          var o;
          return (
            x("volunteer", "admin") &&
            ["救助中", "待处理"].includes((o = e.value) == null ? void 0 : o.orderStatus)
          );
        }),
        B = S(() => x("admin"));
      function R(o) {
        return { 待处理: "warning", 救助中: "primary", 已完成: "success", 已关闭: "info" }[o] || "";
      }
      async function p() {
        y.value = !0;
        try {
          ((e.value = await f.get(d.params.id)),
            (i.value = e.value.orderStatus === "待处理" ? "救助中" : e.value.orderStatus),
            (c.value = e.value.result || ""));
        } finally {
          y.value = !1;
        }
      }
      async function T() {
        (await f.claim(d.params.id), V.success("认领成功"), p());
      }
      async function h() {
        (await f.updateStatus(d.params.id, { orderStatus: i.value, result: c.value }),
          V.success("进度已更新"),
          p());
      }
      async function E() {
        (await f.close(d.params.id, { orderStatus: "已关闭", result: c.value }),
          V.success("工单已关闭"),
          p());
      }
      return (
        z(p),
        (o, l) => {
          const A = u("el-tag"),
            r = u("el-descriptions-item"),
            F = u("el-descriptions"),
            g = u("el-button"),
            C = u("el-option"),
            I = u("el-select"),
            M = u("el-input"),
            P = u("el-card"),
            U = q("loading");
          return e.value
            ? O(
                (m(),
                k(
                  P,
                  { key: 0 },
                  {
                    header: a(() => [
                      w("div", K, [
                        w("span", null, "救助工单 #" + n(e.value.rescueId), 1),
                        t(
                          A,
                          { type: R(e.value.orderStatus) },
                          { default: a(() => [s(n(e.value.orderStatus), 1)]), _: 1 },
                          8,
                          ["type"],
                        ),
                      ]),
                    ]),
                    default: a(() => [
                      t(
                        F,
                        { column: 1, border: "" },
                        {
                          default: a(() => [
                            t(
                              r,
                              { label: "救助描述" },
                              { default: a(() => [s(n(e.value.rescueContent), 1)]), _: 1 },
                            ),
                            t(
                              r,
                              { label: "位置" },
                              { default: a(() => [s(n(e.value.location), 1)]), _: 1 },
                            ),
                            t(
                              r,
                              { label: "发布人" },
                              { default: a(() => [s(n(e.value.publishNickname), 1)]), _: 1 },
                            ),
                            t(
                              r,
                              { label: "处理人" },
                              { default: a(() => [s(n(e.value.dealNickname || "暂无"), 1)]), _: 1 },
                            ),
                            t(
                              r,
                              { label: "救助结果" },
                              { default: a(() => [s(n(e.value.result || "-"), 1)]), _: 1 },
                            ),
                            t(
                              r,
                              { label: "创建时间" },
                              { default: a(() => [s(n(e.value.createTime), 1)]), _: 1 },
                            ),
                          ]),
                          _: 1,
                        },
                      ),
                      w("div", L, [
                        N.value
                          ? (m(),
                            k(
                              g,
                              { key: 0, type: "primary", onClick: T },
                              {
                                default: a(() => [...(l[2] || (l[2] = [s("认领工单", -1)]))]),
                                _: 1,
                              },
                            ))
                          : _("", !0),
                        D.value
                          ? (m(),
                            H(
                              J,
                              { key: 1 },
                              [
                                t(
                                  I,
                                  {
                                    modelValue: i.value,
                                    "onUpdate:modelValue": l[0] || (l[0] = (b) => (i.value = b)),
                                    style: { width: "140px" },
                                  },
                                  {
                                    default: a(() => [
                                      t(C, { label: "救助中", value: "救助中" }),
                                      t(C, { label: "已完成", value: "已完成" }),
                                    ]),
                                    _: 1,
                                  },
                                  8,
                                  ["modelValue"],
                                ),
                                t(
                                  M,
                                  {
                                    modelValue: c.value,
                                    "onUpdate:modelValue": l[1] || (l[1] = (b) => (c.value = b)),
                                    placeholder: "救助结果备注",
                                    style: { width: "240px" },
                                  },
                                  null,
                                  8,
                                  ["modelValue"],
                                ),
                                t(
                                  g,
                                  { type: "success", onClick: h },
                                  {
                                    default: a(() => [...(l[3] || (l[3] = [s("更新进度", -1)]))]),
                                    _: 1,
                                  },
                                ),
                              ],
                              64,
                            ))
                          : _("", !0),
                        B.value
                          ? (m(),
                            k(
                              g,
                              { key: 2, type: "danger", onClick: E },
                              {
                                default: a(() => [...(l[4] || (l[4] = [s("关闭工单", -1)]))]),
                                _: 1,
                              },
                            ))
                          : _("", !0),
                      ]),
                    ]),
                    _: 1,
                  },
                )),
                [[U, y.value]],
              )
            : _("", !0);
        }
      );
    },
  },
  Z = j(Q, [["__scopeId", "data-v-99058425"]]);
export { Z as default };
