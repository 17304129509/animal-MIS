import "./FeedCheckin.css";
import {
  withScope as W,
  onMounted as X,
  openBlock as v,
  createElementBlock as w,
  withDirectives as Y,
  createBlock as N,
  withCtx as s,
  createVNode as l,
  resolveDirective as Z,
  ref as f,
  resolveComponent as i,
  useRoute as ee,
  useRouter as te,
  createTextVNode as d,
  toDisplayString as a,
  createCommentVNode as k,
  createElementVNode as t,
  normalizeClass as V,
  reactive as le,
  computed as P,
  ElMessage as $,
} from "../vendor/vue-runtime.js";
import { updateSession as se, getUser as oe } from "../session.js";
import { feedPointApi as ae, feedingApi as ne } from "../api.js";
const ie = { class: "feed-checkin" },
  de = { class: "card-header" },
  ue = { key: 0, class: "point-info" },
  re = { class: "step-section" },
  pe = { class: "step-title" },
  me = { key: 1, class: "step-section" },
  ve = { class: "step-title" },
  fe = { class: "payment-row" },
  ce = { class: "payment-row" },
  _e = { class: "payment-row" },
  ye = { class: "payment-row total" },
  ge = { class: "total-amount" },
  ke = { key: 0, class: "payment-row" },
  be = { class: "step-section actions" },
  we = { class: "payment-sim" },
  Ve = { class: "sim-amount" },
  Pe = { class: "sim-detail" },
  xe = { class: "sim-methods" },
  Te = {
    __name: "FeedCheckin",
    setup(Ce) {
      const U = ee(),
        A = te(),
        u = f(null),
        x = f(!1),
        D = f(!1),
        r = P(() => oe()),
        o = le({ foodType: "猫粮", quantity: 1, remark: "" }),
        c = f(!1),
        b = f(!1),
        m = f("points"),
        M = { 猫粮: 5, 狗粮: 5, 火腿肠: 3, 饮用水: 2, 罐头: 8 },
        T = P(() => M[o.foodType] || 5),
        _ = P(() => T.value * o.quantity),
        E = P(() => (!o.foodType || !r.value ? !1 : r.value.lovePoints >= _.value));
      function R(p) {
        return { 充足: "success", 不足: "warning", 空缺: "danger" }[p] || "info";
      }
      async function h() {
        x.value = !0;
        try {
          u.value = await ae.get(U.params.id);
        } finally {
          x.value = !1;
        }
      }
      function F() {
        ((m.value = "points"), (c.value = !0));
      }
      async function I() {
        b.value = !0;
        try {
          await new Promise((e) => setTimeout(e, 1200));
          const p = await ne.create({
            pointId: Number(U.params.id),
            foodType: o.foodType,
            amount: T.value,
            quantity: o.quantity,
            remark: o.remark,
          });
          ((c.value = !1),
            p.lovePoints !== void 0 && se({ lovePoints: p.lovePoints }),
            $.success("支付成功！感谢你的爱心投喂，积分已自动扣除"),
            A.push("/feed-points"));
        } catch (p) {
          ((c.value = !1), $.error(p.msg || "支付失败，请重试"));
        } finally {
          b.value = !1;
        }
      }
      return (
        X(h),
        (p, e) => {
          const y = i("el-tag"),
            C = i("el-descriptions-item"),
            j = i("el-descriptions"),
            g = i("el-option"),
            G = i("el-select"),
            q = i("el-form-item"),
            H = i("el-input-number"),
            J = i("el-input"),
            K = i("el-form"),
            L = i("el-divider"),
            B = i("el-card"),
            z = i("el-button"),
            O = i("el-dialog"),
            Q = Z("loading");
          return (
            v(),
            w("div", ie, [
              Y(
                (v(),
                N(B, null, {
                  header: s(() => {
                    var n;
                    return [
                      t("div", de, [
                        t(
                          "span",
                          null,
                          "投喂打卡 — " + a((n = u.value) == null ? void 0 : n.pointName),
                          1,
                        ),
                        r.value
                          ? (v(),
                            N(
                              y,
                              { key: 0, type: "info", size: "small" },
                              {
                                default: s(() => [d(" 我的爱心积分: " + a(r.value.lovePoints), 1)]),
                                _: 1,
                              },
                            ))
                          : k("", !0),
                      ]),
                    ];
                  }),
                  default: s(() => [
                    u.value
                      ? (v(),
                        w("div", ue, [
                          l(
                            j,
                            { column: 2, border: "", size: "small" },
                            {
                              default: s(() => [
                                l(
                                  C,
                                  { label: "点位名称" },
                                  { default: s(() => [d(a(u.value.pointName), 1)]), _: 1 },
                                ),
                                l(
                                  C,
                                  { label: "位置" },
                                  { default: s(() => [d(a(u.value.location), 1)]), _: 1 },
                                ),
                                l(
                                  C,
                                  { label: "当前物资" },
                                  {
                                    default: s(() => [
                                      l(
                                        y,
                                        { type: R(u.value.foodStatus), size: "small" },
                                        { default: s(() => [d(a(u.value.foodStatus), 1)]), _: 1 },
                                        8,
                                        ["type"],
                                      ),
                                    ]),
                                    _: 1,
                                  },
                                ),
                              ]),
                              _: 1,
                            },
                          ),
                        ]))
                      : k("", !0),
                    t("div", re, [
                      t("h4", pe, [
                        l(
                          y,
                          { type: "primary", size: "small", effect: "dark", round: "" },
                          { default: s(() => [...(e[8] || (e[8] = [d("1", -1)]))]), _: 1 },
                        ),
                        e[9] || (e[9] = d(" 选择投喂物资 ", -1)),
                      ]),
                      l(
                        K,
                        { model: o, "label-width": "100px", style: { "max-width": "500px" } },
                        {
                          default: s(() => [
                            l(
                              q,
                              { label: "食物类型" },
                              {
                                default: s(() => [
                                  l(
                                    G,
                                    {
                                      modelValue: o.foodType,
                                      "onUpdate:modelValue":
                                        e[0] || (e[0] = (n) => (o.foodType = n)),
                                      placeholder: "请选择食物类型",
                                      style: { width: "100%" },
                                    },
                                    {
                                      default: s(() => [
                                        l(g, { label: "猫粮 (5积分/份)", value: "猫粮" }),
                                        l(g, { label: "狗粮 (5积分/份)", value: "狗粮" }),
                                        l(g, { label: "火腿肠 (3积分/份)", value: "火腿肠" }),
                                        l(g, { label: "饮用水 (2积分/份)", value: "饮用水" }),
                                        l(g, { label: "罐头 (8积分/份)", value: "罐头" }),
                                      ]),
                                      _: 1,
                                    },
                                    8,
                                    ["modelValue"],
                                  ),
                                ]),
                                _: 1,
                              },
                            ),
                            l(
                              q,
                              { label: "投喂数量" },
                              {
                                default: s(() => [
                                  l(
                                    H,
                                    {
                                      modelValue: o.quantity,
                                      "onUpdate:modelValue":
                                        e[1] || (e[1] = (n) => (o.quantity = n)),
                                      min: 1,
                                      max: 10,
                                    },
                                    null,
                                    8,
                                    ["modelValue"],
                                  ),
                                  e[10] || (e[10] = t("span", { class: "unit-hint" }, "份", -1)),
                                ]),
                                _: 1,
                              },
                            ),
                            l(
                              q,
                              { label: "备注留言" },
                              {
                                default: s(() => [
                                  l(
                                    J,
                                    {
                                      modelValue: o.remark,
                                      "onUpdate:modelValue": e[2] || (e[2] = (n) => (o.remark = n)),
                                      maxlength: "100",
                                      "show-word-limit": "",
                                      placeholder: "想对小动物说的话...",
                                      type: "textarea",
                                      rows: 2,
                                    },
                                    null,
                                    8,
                                    ["modelValue"],
                                  ),
                                ]),
                                _: 1,
                              },
                            ),
                          ]),
                          _: 1,
                        },
                        8,
                        ["model"],
                      ),
                    ]),
                    o.foodType
                      ? (v(),
                        w("div", me, [
                          t("h4", ve, [
                            l(
                              y,
                              { type: "warning", size: "small", effect: "dark", round: "" },
                              { default: s(() => [...(e[11] || (e[11] = [d("2", -1)]))]), _: 1 },
                            ),
                            e[12] || (e[12] = d(" 爱心捐赠确认 ", -1)),
                          ]),
                          l(
                            B,
                            { shadow: "never", class: "payment-card" },
                            {
                              default: s(() => [
                                t("div", fe, [
                                  e[13] || (e[13] = t("span", null, "物资类型", -1)),
                                  t("span", null, a(o.foodType), 1),
                                ]),
                                t("div", ce, [
                                  e[14] || (e[14] = t("span", null, "数量", -1)),
                                  t("span", null, a(o.quantity) + " 份", 1),
                                ]),
                                t("div", _e, [
                                  e[15] || (e[15] = t("span", null, "单价", -1)),
                                  t("span", null, a(T.value) + " 积分/份", 1),
                                ]),
                                l(L),
                                t("div", ye, [
                                  e[16] || (e[16] = t("span", null, "合计消耗积分", -1)),
                                  t("span", ge, a(_.value) + " 积分", 1),
                                ]),
                                r.value
                                  ? (v(),
                                    w("div", ke, [
                                      e[18] || (e[18] = t("span", null, "当前积分", -1)),
                                      t(
                                        "span",
                                        {
                                          class: V({ insufficient: r.value.lovePoints < _.value }),
                                        },
                                        [
                                          d(a(r.value.lovePoints) + " 积分 ", 1),
                                          r.value.lovePoints < _.value
                                            ? (v(),
                                              N(
                                                y,
                                                { key: 0, type: "danger", size: "small" },
                                                {
                                                  default: s(() => [
                                                    ...(e[17] || (e[17] = [d("积分不足", -1)])),
                                                  ]),
                                                  _: 1,
                                                },
                                              ))
                                            : k("", !0),
                                        ],
                                        2,
                                      ),
                                    ]))
                                  : k("", !0),
                              ]),
                              _: 1,
                            },
                          ),
                        ]))
                      : k("", !0),
                    t("div", be, [
                      l(
                        z,
                        { onClick: e[3] || (e[3] = (n) => p.$router.back()) },
                        { default: s(() => [...(e[19] || (e[19] = [d("返回", -1)]))]), _: 1 },
                      ),
                      l(
                        z,
                        { type: "primary", loading: D.value, disabled: !E.value, onClick: F },
                        { default: s(() => [...(e[20] || (e[20] = [d(" 确认投喂 ", -1)]))]), _: 1 },
                        8,
                        ["loading", "disabled"],
                      ),
                    ]),
                  ]),
                  _: 1,
                })),
                [[Q, x.value]],
              ),
              l(
                O,
                {
                  modelValue: c.value,
                  "onUpdate:modelValue": e[7] || (e[7] = (n) => (c.value = n)),
                  title: "模拟支付确认",
                  width: "400px",
                  "close-on-click-modal": !1,
                },
                {
                  default: s(() => {
                    var n;
                    return [
                      t("div", we, [
                        e[24] ||
                          (e[24] = t("div", { class: "sim-header" }, "校园爱心捐赠平台", -1)),
                        t("div", Ve, "-" + a(_.value) + " 积分", 1),
                        t("div", Pe, [
                          t("div", null, a(o.foodType) + " × " + a(o.quantity) + " 份", 1),
                          t(
                            "div",
                            null,
                            "投喂点位: " + a((n = u.value) == null ? void 0 : n.pointName),
                            1,
                          ),
                        ]),
                        t("div", xe, [
                          t(
                            "div",
                            {
                              class: V(["sim-method", { active: m.value === "wechat" }]),
                              onClick: e[4] || (e[4] = (S) => (m.value = "wechat")),
                            },
                            [
                              ...(e[21] ||
                                (e[21] = [
                                  t("span", { class: "method-icon" }, "💚", -1),
                                  t("span", null, "微信支付", -1),
                                ])),
                            ],
                            2,
                          ),
                          t(
                            "div",
                            {
                              class: V(["sim-method", { active: m.value === "alipay" }]),
                              onClick: e[5] || (e[5] = (S) => (m.value = "alipay")),
                            },
                            [
                              ...(e[22] ||
                                (e[22] = [
                                  t("span", { class: "method-icon" }, "💙", -1),
                                  t("span", null, "支付宝", -1),
                                ])),
                            ],
                            2,
                          ),
                          t(
                            "div",
                            {
                              class: V(["sim-method", { active: m.value === "points" }]),
                              onClick: e[6] || (e[6] = (S) => (m.value = "points")),
                            },
                            [
                              ...(e[23] ||
                                (e[23] = [
                                  t("span", { class: "method-icon" }, "❤️", -1),
                                  t("span", null, "爱心积分", -1),
                                ])),
                            ],
                            2,
                          ),
                        ]),
                        l(
                          z,
                          {
                            type: "success",
                            size: "large",
                            loading: b.value,
                            onClick: I,
                            style: { width: "100%", "margin-top": "16px" },
                          },
                          {
                            default: s(() => [d(a(b.value ? "支付处理中..." : "立即支付"), 1)]),
                            _: 1,
                          },
                          8,
                          ["loading"],
                        ),
                        e[25] ||
                          (e[25] = t("div", { class: "sim-footer" }, "公益捐赠 · 爱心无价", -1)),
                      ]),
                    ];
                  }),
                  _: 1,
                },
                8,
                ["modelValue"],
              ),
            ])
          );
        }
      );
    },
  },
  Ne = W(Te, [["__scopeId", "data-v-2f99691e"]]);
export { Ne as default };
