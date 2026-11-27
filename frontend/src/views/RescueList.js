import "./RescueList.css";
import { rescueApi as S } from "../api.js";
import {
  withScope as V,
  onMounted as x,
  openBlock as c,
  createElementBlock as v,
  createElementVNode as N,
  createVNode as t,
  withCtx as l,
  unref as $,
  createBlock as L,
  createCommentVNode as I,
  ref as r,
  resolveComponent as n,
  Fragment as T,
  renderList as F,
  createTextVNode as d,
  toDisplayString as R,
} from "../vendor/vue-runtime.js";
import { isLoggedIn as B } from "../session.js";
const U = { class: "toolbar" },
  z = {
    __name: "RescueList",
    setup(A) {
      const b = ["待处理", "救助中", "已完成", "已关闭"],
        u = r(""),
        _ = r([]),
        p = r(1),
        m = r(0);
      function f(o) {
        return { 待处理: "warning", 救助中: "primary", 已完成: "success", 已关闭: "info" }[o] || "";
      }
      async function i() {
        const o = await S.list({ orderStatus: u.value, page: p.value, pageSize: 10 });
        ((_.value = o.list), (m.value = o.total));
      }
      return (
        x(i),
        (o, a) => {
          const y = n("el-option"),
            w = n("el-select"),
            g = n("el-button"),
            s = n("el-table-column"),
            k = n("el-tag"),
            C = n("el-table"),
            h = n("el-pagination");
          return (
            c(),
            v("div", null, [
              N("div", U, [
                t(
                  w,
                  {
                    modelValue: u.value,
                    "onUpdate:modelValue": a[0] || (a[0] = (e) => (u.value = e)),
                    placeholder: "工单状态",
                    clearable: "",
                    style: { width: "140px" },
                    onChange: i,
                  },
                  {
                    default: l(() => [
                      (c(),
                      v(
                        T,
                        null,
                        F(b, (e) =>
                          t(y, { key: e, label: e, value: e }, null, 8, ["label", "value"]),
                        ),
                        64,
                      )),
                    ]),
                    _: 1,
                  },
                  8,
                  ["modelValue"],
                ),
                $(B)
                  ? (c(),
                    L(
                      g,
                      {
                        key: 0,
                        type: "primary",
                        onClick: a[1] || (a[1] = (e) => o.$router.push("/rescue/new")),
                      },
                      { default: l(() => [...(a[3] || (a[3] = [d("发布救助", -1)]))]), _: 1 },
                    ))
                  : I("", !0),
              ]),
              t(
                C,
                { data: _.value, stripe: "" },
                {
                  default: l(() => [
                    t(s, { prop: "rescueContent", label: "救助描述", "show-overflow-tooltip": "" }),
                    t(s, { prop: "location", label: "位置", width: "160" }),
                    t(s, { prop: "publishNickname", label: "发布人", width: "100" }),
                    t(
                      s,
                      { prop: "orderStatus", label: "状态", width: "100" },
                      {
                        default: l(({ row: e }) => [
                          t(
                            k,
                            { type: f(e.orderStatus) },
                            { default: l(() => [d(R(e.orderStatus), 1)]), _: 2 },
                            1032,
                            ["type"],
                          ),
                        ]),
                        _: 1,
                      },
                    ),
                    t(s, { prop: "createTime", label: "创建时间", width: "170" }),
                    t(
                      s,
                      { label: "操作", width: "100" },
                      {
                        default: l(({ row: e }) => [
                          t(
                            g,
                            {
                              link: "",
                              type: "primary",
                              onClick: (D) => o.$router.push(`/rescue/${e.rescueId}`),
                            },
                            { default: l(() => [...(a[4] || (a[4] = [d("详情", -1)]))]), _: 1 },
                            8,
                            ["onClick"],
                          ),
                        ]),
                        _: 1,
                      },
                    ),
                  ]),
                  _: 1,
                },
                8,
                ["data"],
              ),
              t(
                h,
                {
                  "current-page": p.value,
                  "onUpdate:currentPage": a[2] || (a[2] = (e) => (p.value = e)),
                  total: m.value,
                  layout: "total, prev, pager, next",
                  onCurrentChange: i,
                  class: "pager",
                },
                null,
                8,
                ["current-page", "total"],
              ),
            ])
          );
        }
      );
    },
  },
  P = V(z, [["__scopeId", "data-v-0796cbd8"]]);
export { P as default };
