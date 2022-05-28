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
    createTestObject({dispatch}, str) {
        testService.createTestObject({str}, () => {
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
