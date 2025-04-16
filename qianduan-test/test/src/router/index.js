import { createRouter, createWebHistory } from 'vue-router';  // 使用 Vue Router 4 的方式
import LoginForm from '../components/LoginForm.vue';
import RegisterForm from '../components/RegisterForm.vue';
import HomePage from '../views/HomePage.vue';
import SelectionPage from '@/components/SelectionPage.vue';
import DataExportPage from '@/components/DataExportPage.vue';
import DataMonitorPage from '@/components/DataMonitorPage.vue';
import UserCenterPage from '@/components/UserCenterPage.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
  },
  {
    path: '/login',
    name: 'login',
    component: LoginForm,
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterForm,
  },
  {
    path: '/visual',
    name: 'DataMonitorPage',
    component: DataMonitorPage,
  },
  {
    path: '/query',
    name: 'SelectionPage',
    component: SelectionPage,
  },
  {
    path: '/export',
    name: 'export',
    component: DataExportPage,
  },
  {
    path: '/center',
    name: 'UserCenterPage',
    component: UserCenterPage,
  },
];

const router = createRouter({
  history: createWebHistory(), // 使用 Vue Router 4 的方式
  routes,
});

export default router;
