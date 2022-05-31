import Vue from 'vue';
import VueRouter from 'vue-router';
import TestPage from "@/view/TestPage";
import MainPage from "@/view/MainPage";

Vue.use(VueRouter)

const routes = [
  {path: '/test', component: TestPage},
  {path: '/home', component: MainPage},
  {path: '/', redirect: '/home'},
]

export default new VueRouter({
  mode: 'history',
  routes
})
