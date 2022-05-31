import axios from 'axios'

const DATA_URL_PREFIX = '/api';

export function getRepositories(params, callback) {
    const url=`${DATA_URL_PREFIX}/repositories`;
    axios.get(url, {params})
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}

export function getMavenPackages(params, callback) {
    const url=`${DATA_URL_PREFIX}/maven-packages`;
    axios.get(url, {params})
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}

export function getTopics(params, callback) {
    const url=`${DATA_URL_PREFIX}/topics`;
    axios.get(url, {params})
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}

export function getMavenPackageUsages({mvn_repo_id, sort, order, limit, offset}, callback) {
    const url=`${DATA_URL_PREFIX}/maven-package/${mvn_repo_id}/usages`;
    axios.get(url, {
            params: {
                sort, order, limit, offset
            }
        })
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}

export function getRepoDependencies({repo_id}, callback) {
    const url=`${DATA_URL_PREFIX}/repository/${repo_id}/dependencies`;
    axios.get(url, {})
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}

export function getContributors({repo_id}, callback) {
    const url=`${DATA_URL_PREFIX}/repository/${repo_id}/contributors`;
    axios.get(url, {})
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}

export function getTopicRepositories({topic_str, sort, order, limit, offset}, callback) {
    const url=`${DATA_URL_PREFIX}/topic/${topic_str}/repositories`;
    axios.get(url, {
            params: {
                sort, order, limit, offset
            }
        })
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}

export function getUserRepositories({user_id, sort, order, limit, offset}, callback) {
    const url=`${DATA_URL_PREFIX}/user/${user_id}/repositories`;
    axios.get(url, {
            params: {
                sort, order, limit, offset
            }
        })
        .then(response =>{
            callback(response.data)
        }, errResponse => {
            console.log(errResponse)
        })
}


