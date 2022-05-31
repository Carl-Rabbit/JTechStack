<template>
    <!--    <el-button> REPO</el-button>-->
    <el-table
        ref="singleTable"
        :data="tableData"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange">
        <el-table-column type="expand">
            <template slot-scope="props">
                <el-form class="demo-table-expand" inline label-position="left">
                    <el-form-item label="Name">
                        <a :href=props.row.html_url target="_blank">
                            <el-tag>{{ props.row.name }}</el-tag>
                        </a>
                    </el-form-item>
                    <el-form-item label="Owner">
                        <a :href=props.row.owner.html_url target="_blank">
                            <el-tag>{{ props.row.owner.login }}</el-tag>
                        </a>
                    </el-form-item>
                </el-form>
                <el-form class="sec-table-expand" inline label-position="left">
                    <el-form-item label="Description">
                        <span>{{ props.row.description }}</span>
                    </el-form-item>
                </el-form>
                <!--          <el-button></el-button>-->
            </template>
        </el-table-column>
        <!--      <el-table-column-->
        <!--          label="ID"-->
        <!--          prop="id">-->
        <!--      </el-table-column>-->
        <el-table-column
            label="Name"
            prop="name">
        </el-table-column>
        <el-table-column
            label="Star"
            prop="stargazers_count">
            <template slot-scope="scope">
                <i class="el-icon-star-on">{{ scope.row.stargazers_count }}</i>
            </template>
        </el-table-column>
        <el-table-column
            label="Fork"
            prop="forks">
            <template slot-scope="scope">
                <i class="el-icon-share">{{ scope.row.forks }}</i>
            </template>
        </el-table-column>
    </el-table>
</template>

<script>
import {mapState} from "vuex";

// import repositories from '/fakedata/repositories.json'

export default {
    name: "RepositoryBased",
    data() {
        return {
            currentRow: null,
            updateTree: false,
            // repositories: [],
            // tableData: [],

        }
    },
    mounted() {
        console.log("hello");
        this.$store.dispatch("main/getRepositories", {management: "Maven"})
        // this.tableData = this.repositories
        // console.log(this.tableData)
    },
    computed: {
        ...mapState('main', {
            tableData: state => state.repositories,
            bid: state => state.bid
            // repoDependency: state => state.repoDependency,
            // contributors: state => state.contributors,
            // repoTopics: state => state.repoTopics,
        })
    },
    watch: {
        bid(n, o) {
            console.log(o, n)
            if (n === true)
                this.updateGraph()
        }
    },
    methods: {
        setCurrent(row) {
            this.$refs.singleTable.setCurrentRow(row);
        },
        handleCurrentChange(val) {
            this.currentRow = val;
            let repo_id = this.currentRow.id
            this.$store.dispatch("main/getContributors", {repo_id: repo_id})
            this.$store.dispatch("main/getRepoDependencies", {repo_id: repo_id})
            /*
                        let myChart = echarts.getInstanceByDom(document.getElementById('tree'));
                        if (myChart == null) {
                            myChart = echarts.init(document.getElementById('tree',), null, {renderer: 'svg'});
                        }
                        let option;

                        myChart.showLoading();
                        myChart.hideLoading();

                        // this.$store.dispatch('main/getRepoDependencies', val.id)

                        console.log(val.topics)
                        let tree = {}
                        let repoID = val.id
                        tree['name'] = val.name
                        tree['children'] = [
                            {
                                "name": val.owner.login
                            },
                            {
                                "name": "Contributors",
                                "children": this.contributors[repoID]
                            },
                            {
                                "name": "Dependencies",
                                "children": this.repoDependency[repoID]
                            },
                            {
                                "name": "Topics",
                                "children": val.topics.map(t => {"name": t})


                            },
                        ]
                        tree.children.forEach(function (datum, index) {
                            index % 2 === 0 && (datum.collapsed = true);
                        });
                        tree = response
                        console.log(tree)
                        myChart.setOption(
                            (option = {
                                tooltip: {
                                    trigger: 'item',
                                    triggerOn: 'mousemove'
                                },
                                series: [
                                    {
                                        type: 'tree',
                                        data: [tree],
                                        top: '1%',
                                        left: '7%',
                                        bottom: '1%',
                                        right: '20%',
                                        symbolSize: 7,
                                        label: {
                                            position: 'left',
                                            verticalAlign: 'middle',
                                            align: 'right',
                                            fontSize: 9
                                        },
                                        // layout: "radial",
                                        leaves: {
                                            label: {
                                                position: 'right',
                                                verticalAlign: 'middle',
                                                align: 'left'
                                            }
                                        },
                                        emphasis: {
                                            focus: 'descendant'
                                        },
                                        expandAndCollapse: true,
                                        animationDuration: 550,
                                        animationDurationUpdate: 750
                                    },
                                ]

                            })
                        );
                        myChart.setOption(option);
            */

        },
        updateGraph() {
            console.log("!!!")
            this.$store.commit("main/updateTreeGraph", this.currentRow)

        }
    }

}
</script>

<style>
.demo-table-expand {
    font-size: 0;
    margin-left: 5%;
}

.demo-table-expand label {
    width: 90px;
    color: #99a9bf;
}

.demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
}

.sec-table-expand {
    font-size: 0;
    margin-left: 5%;
}

.sec-table-expand label {
    width: 90px;
    color: #99a9bf;
}

.sec-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 100%;
}

</style>
