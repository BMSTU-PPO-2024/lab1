import "@/assets/main.css"
import { createApp } from "vue";
import App from "@/App.vue";
import router from "@/router/router";
import {createPinia} from "pinia";
import uiComponents from "@/components/ui";
// import stores from "@/stores/stores";


const app = createApp(App);
const pinia = createPinia();

uiComponents.forEach((comp) => {
    app.component(comp.name as string, comp);
});

app
    // .use(stores)
    .use(pinia)
    .use(router)
    .mount("#app");


