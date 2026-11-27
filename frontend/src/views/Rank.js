import { rankApi as c } from "../api.js";
import {
  onMounted as m,
  openBlock as f,
  createBlock as b,
  withCtx as a,
  ref as _,
  resolveComponent as l,
  createVNode as e,
  createTextVNode as u,
} from "../vendor/vue-runtime.js";
const v = {
  __name: "Rank",
  setup(w) {
    const n = _([]),
      r = _([]);
    return (
      m(async () => {
        ((n.value = await c.feed(10)), (r.value = await c.rescue(10)));
      }),
      (k, o) => {
        const t = l("el-table-column"),
          p = l("el-table"),
          d = l("el-card"),
          s = l("el-col"),
          i = l("el-row");
        return (
          f(),
          b(
            i,
            { gutter: 20 },
            {
              default: a(() => [
                e(
                  s,
                  { span: 12 },
                  {
                    default: a(() => [
                      e(d, null, {
                        header: a(() => [...(o[0] || (o[0] = [u("投喂次数榜 Top 10", -1)]))]),
                        default: a(() => [
                          e(
                            p,
                            { data: n.value, stripe: "" },
                            {
                              default: a(() => [
                                e(t, { type: "index", label: "排名", width: "70" }),
                                e(t, { prop: "nickname", label: "昵称" }),
                                e(t, { prop: "count", label: "打卡次数", width: "120" }),
                              ]),
                              _: 1,
                            },
                            8,
                            ["data"],
                          ),
                        ]),
                        _: 1,
                      }),
                    ]),
                    _: 1,
                  },
                ),
                e(
                  s,
                  { span: 12 },
                  {
                    default: a(() => [
                      e(d, null, {
                        header: a(() => [...(o[1] || (o[1] = [u("救助参与榜 Top 10", -1)]))]),
                        default: a(() => [
                          e(
                            p,
                            { data: r.value, stripe: "" },
                            {
                              default: a(() => [
                                e(t, { type: "index", label: "排名", width: "70" }),
                                e(t, { prop: "nickname", label: "昵称" }),
                                e(t, { prop: "count", label: "完成救助", width: "120" }),
                              ]),
                              _: 1,
                            },
                            8,
                            ["data"],
                          ),
                        ]),
                        _: 1,
                      }),
                    ]),
                    _: 1,
                  },
                ),
              ]),
              _: 1,
            },
          )
        );
      }
    );
  },
};
export { v as default };
