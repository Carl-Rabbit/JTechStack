<template>
    <div>
        <!--    <el-button> REPO</el-button>-->
        <div style="width: 40%; float: left">
            <el-table
                    ref="singleTable"
                    :data="repositories"
                    highlight-current-row

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
        </div>
        <div id="d-graph">
            <svg id="svg-container">
                <g class="link-group">
                    <g v-for="edge in graph.edges" :key="edge.source.id + ' ' + edge.target.id">
                        <line :x1="edge.source.x" :y1="edge.source.y"
                              :x2="edge.target.x" :y2="edge.target.y"
                              stroke="black"></line>
                    </g>
                </g>
                <g class="node-group">
                    <g v-for="node in graph.nodes" :key="node.id"
                         :transform="'translate(' + [node.x, node.y] + ')'">
                        <circle :id="node.id" cx="0" cy="0" :r="node.r" fill="red"></circle>
                        <text>{{ node.id }}</text>
                    </g>
                </g>
            </svg>
        </div>
    </div>
</template>

<script>
import {mapState} from "vuex";
import * as d3 from "d3";

/* eslint-disable */

export default {
    name: "DependencyGraph",
    data() {
        const nodes = [
            {id: "A", group: 1, x: 100, y: 100, r: 5},
            {id: "B", group: 2, x: 120, y: 110, r: 5},
            {id: "C", group: 3, x: 110, y: 120, r: 5},
        ]
        const edges = [
            {source: nodes[0], target: nodes[1], value: 1},
            {source: nodes[0], target: nodes[2], value: 4},
        ]
        return {
            currentRow: null,
            tableData: [],

            graph: {nodes, edges},
            simulation: {},
        }
    },
    mounted() {
        console.log("hello");
        this.$store.dispatch("main/getRepositories", {management: "Maven"})
        // this.tableData = repositories.items

        const width = 600;
        const height = 800;
        d3.select("svg")
                .attr("width", width)
                .attr("height", height)
                .attr("viewBox", [-width / 2, -height / 2, width, height])
                .attr("style", "max-width: 100%; height: auto; height: intrinsic;");

        this.updateGraph();
    },
    computed: {
        ...mapState('main', {
            repositories: state => state.repositories,
            repoDependency: state => state.repoDependency
        })
    },
    methods: {
        updateGraph() {
            const N = d3.map(this.graph.nodes, d => d.id);

            const forceNode = d3.forceManyBody();
            const forceLink = d3.forceLink(this.graph.edges).id(({index: i}) => N[i]);

            const simulation = this.simulation = d3.forceSimulation(this.graph.nodes)
                    .force("link", forceLink)
                    .force("charge", forceNode)
                    .force("center",  d3.forceCenter())
                    .on("tick", ticked);

            function ticked() {
                // console.log(graph)
            }

            const nodeMap = new Map();
            this.graph.nodes.forEach(node => nodeMap.set(node.id, node));

            function dragstarted(event) {
                if (!event.active) simulation.alphaTarget(0.3).restart();
                // const node = nodeMap.get(this.id)
                // node.offsetX = node.x - event.x;
                // node.offsetY = node.y - event.y;
            }

            function dragged(event) {
                // event.subject.fx = event.x;
                // event.subject.fy = event.y;
                const node = nodeMap.get(this.id)
                node.x = event.x;
                node.y = event.y;
            }

            function dragended(event) {
                if (!event.active) simulation.alphaTarget(0);
                // const node = nodeMap.get(this.id)
                // node.offsetX = null;
                // node.offsetY = null;
            }

            const drag = d3.drag()
                    .on("start", dragstarted)
                    .on("drag", dragged)
                    .on("end", dragended)
            this.$nextTick(() => {
                d3.select(this.$el).selectAll('circle').call(drag)
            })
        },
        setCurrent(row) {
            this.$refs.singleTable.setCurrentRow(row);
        },
        handleCurrentChange(val) {
            this.currentRow = val;


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

#tree {
    position: relative;
    height: 100vh;
    overflow: hidden;
}
</style>
