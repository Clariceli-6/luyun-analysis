import { createApp } from 'vue'; // 使用 createApp 初始化
import App from './App.vue';
import router from './router'; // 引入修正后的 router
import axios from 'axios';

const app = createApp(App);
app.use(router); // 使用 router
app.mount('#app');

axios.interceptors.request.use(config => {
    config.baseURL = 'http://localhost:8081';
    return config;
});
