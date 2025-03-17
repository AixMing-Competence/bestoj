import { createMemoryHistory, createRouter } from "vue-router";
import { routes } from "@/router/routes";

const router = createRouter({
  history: createMemoryHistory(),
  routes,
});

export { router };
