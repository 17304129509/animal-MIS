import "./AnimalNew.css";
import {
  withScope as M,
  openBlock as i,
  createBlock as V,
  withCtx as a,
  resolveComponent as n,
  useRouter as E,
  createVNode as l,
  createElementBlock as R,
  unref as I,
  PlusIcon as $,
  createElementVNode as j,
  createTextVNode as f,
  ref as c,
  reactive as z,
  ElMessage as p,
} from "../vendor/vue-runtime.js";
import { uploadApi as L, animalApi as T } from "../api.js";
const W = ["src"],
  D = {
    __name: "AnimalNew",
    setup(F) {
      const v = E(),
        _ = c(),
        m = c(!1),
        y = c(!1),
        o = z({ animalName: "", type: "猫咪", color: "", location: "", photoUrl: "", remark: "" }),
        g = {
          animalName: [{ required: !0, message: "请输入昵称" }],
          type: [{ required: !0, message: "请选择类型" }],
          location: [{ required: !0, message: "请输入位置" }],
        };
      function k(s) {
        const e = s.type.startsWith("image/"),
          u = s.size / 1024 / 1024 < 5;
        return e
          ? u
            ? !0
            : (p.error("图片大小不能超过5MB"), !1)
          : (p.error("只能上传图片文件"), !1);
      }
      async function w(s) {
        y.value = !0;
        try {
          const e = await L.upload(s.file);
          ((o.photoUrl = e.url), p.success("照片上传成功"));
        } catch {
          p.error("上传失败");
        } finally {
          y.value = !1;
        }
      }
      async function U() {
        (await _.value.validate(), (m.value = !0));
        try {
          (await T.create(o), p.success("建档申请已提交"), v.push("/animals"));
        } finally {
          m.value = !1;
        }
      }
      return (s, e) => {
        const u = n("el-input"),
          r = n("el-form-item"),
          d = n("el-option"),
          N = n("el-select"),
          x = n("el-icon"),
          B = n("el-upload"),
          b = n("el-button"),
          q = n("el-form"),
          A = n("el-alert"),
          C = n("el-card");
        return (
          i(),
          V(C, null, {
            header: a(() => [...(e[6] || (e[6] = [f("新增动物档案", -1)]))]),
            default: a(() => [
              l(
                q,
                {
                  model: o,
                  rules: g,
                  ref_key: "formRef",
                  ref: _,
                  "label-width": "100px",
                  style: { "max-width": "560px" },
                },
                {
                  default: a(() => [
                    l(
                      r,
                      { label: "昵称", prop: "animalName" },
                      {
                        default: a(() => [
                          l(
                            u,
                            {
                              modelValue: o.animalName,
                              "onUpdate:modelValue": e[0] || (e[0] = (t) => (o.animalName = t)),
                            },
                            null,
                            8,
                            ["modelValue"],
                          ),
                        ]),
                        _: 1,
                      },
                    ),
                    l(
                      r,
                      { label: "类型", prop: "type" },
                      {
                        default: a(() => [
                          l(
                            N,
                            {
                              modelValue: o.type,
                              "onUpdate:modelValue": e[1] || (e[1] = (t) => (o.type = t)),
                            },
                            {
                              default: a(() => [
                                l(d, { label: "猫咪", value: "猫咪" }),
                                l(d, { label: "狗狗", value: "狗狗" }),
                                l(d, { label: "其他", value: "其他" }),
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
                      r,
                      { label: "毛色", prop: "color" },
                      {
                        default: a(() => [
                          l(
                            u,
                            {
                              modelValue: o.color,
                              "onUpdate:modelValue": e[2] || (e[2] = (t) => (o.color = t)),
                            },
                            null,
                            8,
                            ["modelValue"],
                          ),
                        ]),
                        _: 1,
                      },
                    ),
                    l(
                      r,
                      { label: "常驻位置", prop: "location" },
                      {
                        default: a(() => [
                          l(
                            u,
                            {
                              modelValue: o.location,
                              "onUpdate:modelValue": e[3] || (e[3] = (t) => (o.location = t)),
                            },
                            null,
                            8,
                            ["modelValue"],
                          ),
                        ]),
                        _: 1,
                      },
                    ),
                    l(
                      r,
                      { label: "照片" },
                      {
                        default: a(() => [
                          l(
                            B,
                            {
                              class: "photo-upload",
                              "show-file-list": !1,
                              "before-upload": k,
                              "http-request": w,
                              accept: "image/*",
                            },
                            {
                              default: a(() => [
                                o.photoUrl
                                  ? (i(),
                                    R(
                                      "img",
                                      { key: 0, src: o.photoUrl, class: "preview" },
                                      null,
                                      8,
                                      W,
                                    ))
                                  : (i(),
                                    V(
                                      x,
                                      { key: 1, class: "upload-icon" },
                                      { default: a(() => [l(I($))]), _: 1 },
                                    )),
                              ]),
                              _: 1,
                            },
                          ),
                          e[7] ||
                            (e[7] = j(
                              "div",
                              { class: "upload-tip" },
                              "点击上传照片，支持 jpg/png，不超过5MB",
                              -1,
                            )),
                        ]),
                        _: 1,
                      },
                    ),
                    l(
                      r,
                      { label: "备注", prop: "remark" },
                      {
                        default: a(() => [
                          l(
                            u,
                            {
                              modelValue: o.remark,
                              "onUpdate:modelValue": e[4] || (e[4] = (t) => (o.remark = t)),
                              type: "textarea",
                            },
                            null,
                            8,
                            ["modelValue"],
                          ),
                        ]),
                        _: 1,
                      },
                    ),
                    l(r, null, {
                      default: a(() => [
                        l(
                          b,
                          { type: "primary", loading: m.value, onClick: U },
                          { default: a(() => [...(e[8] || (e[8] = [f("提交建档", -1)]))]), _: 1 },
                          8,
                          ["loading"],
                        ),
                        l(
                          b,
                          { onClick: e[5] || (e[5] = (t) => s.$router.back()) },
                          { default: a(() => [...(e[9] || (e[9] = [f("取消", -1)]))]), _: 1 },
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
              l(A, {
                type: "info",
                closable: !1,
                title: "学生提交的档案需管理员审核后才会在列表展示（管理员提交直接通过）",
              }),
            ]),
            _: 1,
          })
        );
      };
    },
  },
  J = M(D, [["__scopeId", "data-v-44db497e"]]);
export { J as default };
