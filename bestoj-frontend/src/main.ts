import { createApp } from "vue";
import App from "./App.vue";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import router from "@/router";
import "bytemd/dist/index.css";
import "./access";
import { createPinia } from "pinia";

const pinia = createPinia();
createApp(App).use(ArcoVue).use(pinia).use(router).mount("#app");
