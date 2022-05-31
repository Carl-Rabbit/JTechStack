import {dataService} from '@/service';

// initial state
const state = () => ({
    repositories: [],
    mavenPackages: [],
    topics: [],
    repoDependency: {},
})

// getters
const getters = {}

// actions
const actions = {
    getRepositories({commit}, params) {
        dataService.getRepositories(params, resp => {
            commit('updateRepositories', resp.map(r => JSON.parse(r.content)))
        })
    },
    getMavenPackages({commit}, params) {
        dataService.getMavenPackages(params, resp => {
            commit('updateMavenPackages', resp)
        })
    },
    getTopics({commit}, params) {
        dataService.getTopics(params, resp => {
            commit('updateTopics', resp)
        })
    },
    getRepoDependencies({commit}, {repo_id}) {
        dataService.getRepoDependencies({repo_id}, resp => {
            commit('updateRepoDependencies', {repo_id, resp})
        })
    },
    getContributors({commit}, {repo_id}) {
        dataService.getContributors({repo_id}, resp => {
            commit('xxx', {repo_id, resp})
        })
    },
    getTopicRepositories({commit}, {topic_str}) {
        dataService.getTopicRepositories({topic_str}, resp => {
            commit('xxx', {topic_str, resp})
        })
    },
    getMavenPackageUsages({commit}, params) {
        dataService.getMavenPackageUsages(params, resp => {
            commit('xxx', {mvn_repo_id: params.mvn_repo_id, resp})
        })
    },
    getUserRepositories({commit}, params) {
        dataService.getUserRepositories(params, resp => {
            commit('xxx', {user_id: params.user_id, resp})
        })
    }
}

// mutations
const mutations = {
    updateRepositories(state, repositories) {
        state.repositories = repositories
    },
    updateMavenPackages(state, mavenPackages) {
        state.mavenPackages = mavenPackages
    },
    updateRepoDependencies(state, repo_id, repoDependencies) {
        const pickUpKeys = ['groupId','artifactId','UsedBy', 'description']
        let repoDep=[]
        for (const repoDependenciesEle of repoDependencies) {
            let tmp = {}
            for (const pickUpKey of pickUpKeys) {
                tmp[pickUpKey] = repoDependenciesEle[pickUpKey]
            }
            repoDep.push(tmp)
        }
        state.repoDependency[repo_id] = repoDep
    },
    updateTopics(state, topics) {
        state.topics = topics
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}
