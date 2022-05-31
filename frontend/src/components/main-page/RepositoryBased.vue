<template>
  <div>
    <!--    <el-button> REPO</el-button>-->
    <div style="width: 40%; float: left">
      <el-table
          ref="singleTable"
          :data="tableData"
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
    <div id="tree">
    </div>
  </div>
</template>

<script>
import {mapState} from "vuex";
import * as echarts from 'echarts'
import response from '/fakedata/response.json'
import repositories from '/fakedata/repositories.json'

export default {
  name: "RepositoryBased",
  data() {
    return {
      currentRow: null,
      tableData: [],

    }
  },
  mounted() {
    console.log("hello");
    // this.$store.dispatch("main/getRepositories", {management: "Gradle"})
    this.tableData = repositories.items
    console.log(this.tableData)
  },
  computed: {
    ...mapState('main', {
      repositories: state => state.repositories,
      repoDependency: state => state.repoDependency
    })
  },
  methods: {
    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
    },
    handleCurrentChange(val) {
      this.currentRow = val;

      let myChart = echarts.getInstanceByDom(document.getElementById('tree'));
      if (myChart == null) {
        myChart = echarts.init(document.getElementById('tree'));
      }
      let option;

      myChart.showLoading();
      myChart.hideLoading();

      // this.$store.dispatch('main/getRepoDependencies', val.id)

      let tree = {}
      // tree['name'] = val.name
      // tree['children'] = [
      //   {
      //     "name": val.owner.login
      //   },
      //   {
      //     "name": "Contributors"
      //   },
      //   {
      //     "name": "Dependencies",
      //     "children": this.repoDependency[val.id]
      //   },
      //   {
      //     "name": "Topics",
      //     "children": []
      //   },
      // ]
      // tree.children.forEach(function (datum, index) {
      //   index % 2 === 0 && (datum.collapsed = true);
      // });
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