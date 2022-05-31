<template>
    <div style="height: 100%" id="tree"></div>
</template>

<script>
import * as echarts from 'echarts'
// import response from '/fakedata/response.json'
import {mapState} from "vuex";

export default {
    name: "treeGraph",
    data() {
        return {
            // treeData: null
        }
    },
    computed: {
        ...mapState('main', {
            treeData: state => state.tree
        })
    },
    watch: {
        treeData(n, o) {
            console.log(n, o)
            this.updateGraph()
        }
    },
    methods: {
        updateGraph() {
            console.log('updateGraph', this.treeData)
            // this.$store.
            let myChart = echarts.getInstanceByDom(document.getElementById('tree'));
            if (myChart == null) {
                myChart = echarts.init(document.getElementById('tree',), null, {renderer: 'svg'});
            }
            let option;

            myChart.showLoading();
            myChart.hideLoading();
            /*tree.children.forEach(function (datum, index) {
                index % 2 === 0 && (datum.collapsed = true);
            });*/
            // treeData = response
            // console.log(tree)
            myChart.setOption(
                (option = {
                    tooltip: {
                        trigger: 'item',
                        triggerOn: 'mousemove'
                    },
                    series: [
                        {
                            type: 'tree',
                            data: [this.treeData],
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
        }
    }
}
</script>

<style scoped>
#tree{
    position: relative;
    height: 100vh;
    overflow: hidden;
}
</style>