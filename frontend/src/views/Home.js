import "./Home.css";
import { rescueApi as GS, animalApi as ZO, feedPointApi as $O, statsApi as Yf } from "../api.js";
import {
  withScope as KO,
  onMounted as qO,
  nextTick as JO,
  openBlock as ed,
  createElementBlock as FS,
  createVNode as qt,
  withCtx as zt,
  ref as Dl,
  resolveComponent as mn,
  createElementVNode as aa,
  unref as HS,
  createTextVNode as nr,
  toDisplayString as Jo,
  createCommentVNode as US,
  createBlock as jO,
  computed as QO,
} from "../vendor/vue-runtime.js";
import { isLoggedIn as WS, getUser as tN } from "../session.js";
import { initChart as bw, LinearGradient as Go } from "../vendor/echarts.js";
const wtt = { class: "home" },
  Ttt = { key: 0, class: "my-points" },
  Ctt = { class: "num" },
  Att = { class: "num" },
  Mtt = { class: "num" },
  Dtt = { class: "num" },
  Itt = {
    __name: "Home",
    setup(r) {
      const t = Dl({}),
        e = Dl([]),
        a = Dl(0),
        n = QO(() => tN()),
        i = Dl(null),
        o = Dl(null);
      function s(u) {
        return { 待处理: "warning", 救助中: "primary", 已完成: "success", 已关闭: "info" }[u] || "";
      }
      qO(async () => {
        const [u, f, v, h] = await Promise.all([
          GS.list({ orderStatus: "待处理", page: 1, pageSize: 1 }),
          ZO.list({ page: 1, pageSize: 1 }),
          $O.list({ page: 1, pageSize: 1 }),
          GS.list({ page: 1, pageSize: 5 }),
        ]);
        ((t.value = { pendingRescue: u.total, animalCount: f.total, feedPointCount: v.total }),
          (e.value = h.list));
        try {
          const [c, p, d, g] = await Promise.all([
            Yf.dashboard(),
            Yf.weeklyFeeding(),
            Yf.foodStatus(),
            Yf.rescueRate(),
          ]);
          ((t.value = { ...t.value, ...c }), (a.value = g.rate || 0), await JO(), l(p, d));
        } catch (c) {
          console.log("Stats not available:", c);
        }
      });
      function l(u, f) {
        if (i.value) {
          const v = bw(i.value),
            h = u.map((p) => p.day),
            c = u.map((p) => p.count);
          (v.setOption({
            tooltip: { trigger: "axis" },
            xAxis: { type: "category", data: h.length ? h : ["暂无数据"] },
            yAxis: { type: "value", minInterval: 1 },
            series: [
              {
                type: "line",
                data: c.length ? c : [0],
                smooth: !0,
                lineStyle: { color: "#6b9080" },
                itemStyle: { color: "#6b9080" },
                areaStyle: {
                  color: new Go(0, 0, 0, 1, [
                    { offset: 0, color: "rgba(107,144,128,0.3)" },
                    { offset: 1, color: "rgba(107,144,128,0)" },
                  ]),
                },
              },
            ],
            grid: { top: 10, right: 20, bottom: 20, left: 40 },
          }),
            window.addEventListener("resize", () => v.resize()));
        }
        if (o.value) {
          const v = bw(o.value);
          (v.setOption({
            tooltip: { trigger: "item" },
            legend: { bottom: 0 },
            series: [
              {
                type: "pie",
                radius: ["40%", "70%"],
                data: f.map((h) => ({ name: h.name, value: h.value })),
                label: { formatter: "{b}: {c}" },
                color: ["#84a88c", "#c4a56a", "#d4837a"],
              },
            ],
            grid: { top: 0, right: 0, bottom: 0, left: 0 },
          }),
            window.addEventListener("resize", () => v.resize()));
        }
      }
      return (u, f) => {
        const v = mn("el-button"),
          h = mn("el-space"),
          c = mn("el-card"),
          p = mn("el-col"),
          d = mn("el-row"),
          g = mn("el-table-column"),
          y = mn("el-tag"),
          m = mn("el-table");
        return (
          ed(),
          FS("div", wtt, [
            qt(
              c,
              { class: "hero" },
              {
                default: zt(() => [
                  f[10] || (f[10] = aa("h1", null, "校园流浪动物投喂与救助管理系统", -1)),
                  f[11] ||
                    (f[11] = aa(
                      "p",
                      null,
                      "记录每一次投喂，跟踪每一份爱心，让校园流浪动物被温柔以待。",
                      -1,
                    )),
                  HS(WS) && n.value
                    ? (ed(),
                      FS("div", Ttt, [
                        f[4] || (f[4] = aa("span", { class: "heart" }, "❤", -1)),
                        f[5] || (f[5] = nr(" 我的爱心积分: ", -1)),
                        aa("strong", null, Jo(n.value.lovePoints), 1),
                      ]))
                    : US("", !0),
                  qt(
                    h,
                    { wrap: "" },
                    {
                      default: zt(() => [
                        qt(
                          v,
                          {
                            type: "primary",
                            onClick: f[0] || (f[0] = (_) => u.$router.push("/animals")),
                          },
                          {
                            default: zt(() => [...(f[6] || (f[6] = [nr("查看动物档案", -1)]))]),
                            _: 1,
                          },
                        ),
                        qt(
                          v,
                          { onClick: f[1] || (f[1] = (_) => u.$router.push("/feed-points")) },
                          { default: zt(() => [...(f[7] || (f[7] = [nr("投喂点位", -1)]))]), _: 1 },
                        ),
                        qt(
                          v,
                          {
                            type: "warning",
                            onClick: f[2] || (f[2] = (_) => u.$router.push("/rescue")),
                          },
                          { default: zt(() => [...(f[8] || (f[8] = [nr("救助工单", -1)]))]), _: 1 },
                        ),
                        HS(WS)
                          ? (ed(),
                            jO(
                              v,
                              {
                                key: 0,
                                type: "success",
                                onClick: f[3] || (f[3] = (_) => u.$router.push("/rescue/new")),
                              },
                              {
                                default: zt(() => [...(f[9] || (f[9] = [nr("发布救助", -1)]))]),
                                _: 1,
                              },
                            ))
                          : US("", !0),
                      ]),
                      _: 1,
                    },
                  ),
                ]),
                _: 1,
              },
            ),
            qt(
              d,
              { gutter: 16, class: "stats" },
              {
                default: zt(() => [
                  qt(
                    p,
                    { span: 6 },
                    {
                      default: zt(() => [
                        qt(
                          c,
                          { shadow: "hover" },
                          {
                            header: zt(() => [...(f[12] || (f[12] = [nr("待处理救助", -1)]))]),
                            default: zt(() => [aa("div", Ctt, Jo(t.value.pendingRescue || 0), 1)]),
                            _: 1,
                          },
                        ),
                      ]),
                      _: 1,
                    },
                  ),
                  qt(
                    p,
                    { span: 6 },
                    {
                      default: zt(() => [
                        qt(
                          c,
                          { shadow: "hover" },
                          {
                            header: zt(() => [...(f[13] || (f[13] = [nr("动物档案", -1)]))]),
                            default: zt(() => [aa("div", Att, Jo(t.value.animalCount || 0), 1)]),
                            _: 1,
                          },
                        ),
                      ]),
                      _: 1,
                    },
                  ),
                  qt(
                    p,
                    { span: 6 },
                    {
                      default: zt(() => [
                        qt(
                          c,
                          { shadow: "hover" },
                          {
                            header: zt(() => [...(f[14] || (f[14] = [nr("投喂点位", -1)]))]),
                            default: zt(() => [aa("div", Mtt, Jo(t.value.feedPointCount || 0), 1)]),
                            _: 1,
                          },
                        ),
                      ]),
                      _: 1,
                    },
                  ),
                  qt(
                    p,
                    { span: 6 },
                    {
                      default: zt(() => [
                        qt(
                          c,
                          { shadow: "hover" },
                          {
                            header: zt(() => [...(f[15] || (f[15] = [nr("救助完成率", -1)]))]),
                            default: zt(() => [aa("div", Dtt, Jo(a.value) + "%", 1)]),
                            _: 1,
                          },
                        ),
                      ]),
                      _: 1,
                    },
                  ),
                ]),
                _: 1,
              },
            ),
            qt(
              d,
              { gutter: 16, class: "charts-row" },
              {
                default: zt(() => [
                  qt(
                    p,
                    { span: 12 },
                    {
                      default: zt(() => [
                        qt(
                          c,
                          { shadow: "hover" },
                          {
                            header: zt(() => [...(f[16] || (f[16] = [nr("近7日投喂趋势", -1)]))]),
                            default: zt(() => [
                              aa(
                                "div",
                                { ref_key: "feedingChart", ref: i, style: { height: "300px" } },
                                null,
                                512,
                              ),
                            ]),
                            _: 1,
                          },
                        ),
                      ]),
                      _: 1,
                    },
                  ),
                  qt(
                    p,
                    { span: 12 },
                    {
                      default: zt(() => [
                        qt(
                          c,
                          { shadow: "hover" },
                          {
                            header: zt(() => [...(f[17] || (f[17] = [nr("点位物资分布", -1)]))]),
                            default: zt(() => [
                              aa(
                                "div",
                                { ref_key: "foodChart", ref: o, style: { height: "300px" } },
                                null,
                                512,
                              ),
                            ]),
                            _: 1,
                          },
                        ),
                      ]),
                      _: 1,
                    },
                  ),
                ]),
                _: 1,
              },
            ),
            qt(c, null, {
              header: zt(() => [...(f[18] || (f[18] = [nr("最新救助工单", -1)]))]),
              default: zt(() => [
                qt(
                  m,
                  { data: e.value, stripe: "" },
                  {
                    default: zt(() => [
                      qt(g, { prop: "rescueContent", label: "描述", "show-overflow-tooltip": "" }),
                      qt(g, { prop: "location", label: "位置", width: "160" }),
                      qt(
                        g,
                        { prop: "orderStatus", label: "状态", width: "100" },
                        {
                          default: zt(({ row: _ }) => [
                            qt(
                              y,
                              { type: s(_.orderStatus) },
                              { default: zt(() => [nr(Jo(_.orderStatus), 1)]), _: 2 },
                              1032,
                              ["type"],
                            ),
                          ]),
                          _: 1,
                        },
                      ),
                      qt(
                        g,
                        { label: "操作", width: "100" },
                        {
                          default: zt(({ row: _ }) => [
                            qt(
                              v,
                              {
                                link: "",
                                type: "primary",
                                onClick: (S) => u.$router.push(`/rescue/${_.rescueId}`),
                              },
                              {
                                default: zt(() => [...(f[19] || (f[19] = [nr("详情", -1)]))]),
                                _: 1,
                              },
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
              ]),
              _: 1,
            }),
          ])
        );
      };
    },
  },
  Rtt = KO(Itt, [["__scopeId", "data-v-ed8e2dab"]]);
export { Rtt as default };
