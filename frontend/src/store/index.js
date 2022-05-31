import Vue from 'vue';
import Vuex from 'vuex';
import test from './module/test';
import main from './module/main';

Vue.use(Vuex)

// const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store({
  modules: {
    test, main
  },
  // strict: debug,
  strict: false,
})
