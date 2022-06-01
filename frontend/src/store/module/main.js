import {dataService} from '@/service';
// import {assert} from "echarts";
// import {getContributors} from "@/service/module/dataService";

// initial state
const state = () => ({
    repositories: [],
    mavenPackages: [],
    topics: {},
    repoDependency: {},
    repoTopics: {},
    tree: {},
    contributors: {},
    bid: false,
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
            commit('updateRepoDependencies', {repo_id, repoDependencies: resp})
        })
    },
    getContributors({commit}, {repo_id}) {
        dataService.getContributors({repo_id}, resp => {
            // console.log(typeof resp)
            commit('updateContributors', {repo_id, contributors: resp})
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
    },
}

// mutations
const mutations = {
    updateRepositories(state, repositories) {
        state.repositories = repositories
        console.log(state.repositories)
    },
    updateMavenPackages(state, mavenPackages) {
        state.mavenPackages = mavenPackages
    },
    updateRepoDependencies(state, {repo_id, repoDependencies}) {
        console.log("depen", repoDependencies)
        const pickUpKeys = ['groupId', 'artifactId', 'usedBy', 'description']
        let repoDep = []
        for (const repoDependenciesEle of repoDependencies) {
            if (repoDependenciesEle["mavenRepo"] === null)
                continue
            let tmp = {}
            let children = []
            // console.log("maven", repoDependenciesEle["mavenRepo"])
            // console.log("maven", repoDependenciesEle["mavenRepo"]["name"])
            tmp["name"] = repoDependenciesEle["mavenRepo"]["name"]
            for (const pickUpKey of pickUpKeys) {
                children.push({"name": repoDependenciesEle["mavenRepo"][pickUpKey]})
            }
            tmp["children"] = children
            repoDep.push(tmp)
        }
        state.repoDependency[repo_id] = repoDep
        console.log("Depen", repoDep, state.repoDependency)
        state.bid = true
        // console.log(state.bid)
    },
    updateTopics(state, topics) {

        state.topics = topics
    },
    updateContributors(state, {repo_id, contributors}) {
        let refacted = []
        for (const con of contributors)
            refacted.push({"name": con["login"], "value": con["contributions"]})
        state.contributors[repo_id] = refacted
        console.log("Con", state.contributors)
        // state.bid = true
    },
    updateTreeGraph(state, val) {
        let tree = {}
        let repo_id = val.id
        console.log("val", val)
        console.log("contri", state.contributors[repo_id])
        console.log("dependency", state.repoDependency[repo_id])
        let topics = []
        for (const topic of val.topics) {
            topics.push({"name": topic})
        }
        tree['name'] = val.name
        tree['children'] = [
            {
                "name": val.owner.login
            },
            {
                "name": "Contributors",
                "children": state.contributors[repo_id]
            },
            {
                "name": "Dependencies",
                "children": state.repoDependency[repo_id]
            },
            {
                "name": "Topics",
                "children": topics
            },
        ]
        // console.log(state)
        console.log(tree)
        state.tree = tree
        state.bid = false

    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
    //
    // computed:{
    //
    // }
}
