import { defineConfig } from "vite";

export default defineConfig({
  server: {
    proxy: {
      "/api": "http://127.0.0.1:8081",
      "/uploads": "http://127.0.0.1:8081",
    },
  },
  build: {
    modulePreload: { polyfill: false },
  },
});
