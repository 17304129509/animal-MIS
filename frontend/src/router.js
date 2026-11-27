import MainLayout from "./MainLayout.js";
import { createRouter, createWebHistory } from "./vendor/vue-runtime.js";
import { getToken, getUser } from "./session.js";

const loginRequired = { auth: true };

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      component: MainLayout,
      children: [
        { path: "", name: "home", component: () => import("./views/Home.js") },
        { path: "animals", name: "animals", component: () => import("./views/Animals.js") },
        { path: "animals/new", name: "animal-new", meta: loginRequired, component: () => import("./views/AnimalNew.js") },
        { path: "animals/:id", name: "animal-detail", component: () => import("./views/AnimalDetail.js") },
        { path: "feed-points", name: "feed-points", component: () => import("./views/FeedPoints.js") },
        { path: "feed-points/:id/checkin", name: "feed-checkin", meta: loginRequired, component: () => import("./views/FeedCheckin.js") },
        { path: "rescue", name: "rescue", component: () => import("./views/RescueList.js") },
        { path: "rescue/new", name: "rescue-new", meta: loginRequired, component: () => import("./views/RescueNew.js") },
        { path: "rescue/:id", name: "rescue-detail", component: () => import("./views/RescueDetail.js") },
        { path: "adoption", name: "adoption", component: () => import("./views/AdoptionList.js") },
        { path: "adoption/new", name: "adoption-new", meta: loginRequired, component: () => import("./views/AdoptionApply.js") },
        { path: "adoption/:id", name: "adoption-detail", component: () => import("./views/AdoptionDetail.js") },
        { path: "rank", name: "rank", component: () => import("./views/Rank.js") },
        { path: "profile", name: "profile", meta: loginRequired, component: () => import("./views/Profile.js") },
        { path: "admin", name: "admin", meta: { auth: true, role: "admin" }, component: () => import("./views/Admin.js") },
      ],
    },
    { path: "/login", name: "login", component: () => import("./views/Login.js") },
    { path: "/register", name: "register", component: () => import("./views/Register.js") },
  ],
});

router.beforeEach((to) => {
  if (to.meta.auth && !getToken()) {
    return { name: "login", query: { redirect: to.fullPath } };
  }
  if (to.meta.role && getUser()?.role !== to.meta.role) {
    return { name: "home" };
  }
});
