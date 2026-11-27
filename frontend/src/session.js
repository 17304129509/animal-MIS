const Av = "campus_animal_token",
  Wd = "campus_animal_user";
function u5() {
  return localStorage.getItem(Av);
}
function pie(e, t) {
  (localStorage.setItem(Av, e), localStorage.setItem(Wd, JSON.stringify(t)));
}
function jd() {
  const e = localStorage.getItem(Wd);
  return e ? JSON.parse(e) : null;
}
function vie(e) {
  const t = jd();
  t && (Object.assign(t, e), localStorage.setItem(Wd, JSON.stringify(t)));
}
function sie() {
  (localStorage.removeItem(Av), localStorage.removeItem(Wd));
}
function hie() {
  return !!u5();
}
function mie(...e) {
  const t = jd();
  return t && e.includes(t.role);
}
export {
  vie as updateSession,
  pie as storeSession,
  u5 as getToken,
  sie as clearSession,
  hie as isLoggedIn,
  jd as getUser,
  mie as hasRole,
};
