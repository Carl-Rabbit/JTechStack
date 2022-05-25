import {testService} from '@/service';

// initial state
const state = () => ({
    objectList: [],
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
    createTestObject({commit, dispatch}, str) {
        testService.createTestObject({str}, () => {
            commit('createTestObject')
            dispatch('getAllTestObject')
        });
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
