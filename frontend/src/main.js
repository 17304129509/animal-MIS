import "./style.css";
import { createApp, Root, ElementPlus, elementIcons, zhCn } from "./vendor/vue-runtime.js";
import { router } from "./router.js";

const app = createApp(Root);
for (const [name, icon] of Object.entries(elementIcons)) {
  app.component(name, icon);
}
app.use(ElementPlus, { locale: zhCn });
app.use(router);
app.mount("#app");
