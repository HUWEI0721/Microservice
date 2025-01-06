import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import BaiduMap from "vue-baidu-map-3x";


const app = createApp(App);

app.use(store);
app.use(router);

app.use(BaiduMap, {
    ak: "vYLZOMxHvxFK0SswtKPk2V9rBS6xkhb1",
    // v:'2.0',  // 默认使用3.0
    // type: 'WebGL' // ||API 默认API  (使用此模式 BMap=BMapGL)
});

app.mount("#app");
