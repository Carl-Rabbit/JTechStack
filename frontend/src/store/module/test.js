import {testService} from '@/service';
import data from '/fakedata/repo.json'
// initial state
const state = () => ({
    objectList: data.items,
})

// getters
const getters = {}

// actions
const actions = {
    getAllTestObject({commit}) {
        testService.getAllTestObject({}, resp => {
            commit('updateObjectList', resp);
        })
    },
    createTestObject({dispatch}, str) {
        testService.createTestObject({str}, () => {
            dispatch('getAllTestObject')
        });
    },
    getRepoList(){

    },
}

// mutations
const mutations = {
    updateObjectList(state, objectList) {
        state.objectList = objectList
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}
