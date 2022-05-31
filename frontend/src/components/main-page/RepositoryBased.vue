<template>
  <div>
    <el-button> REPO</el-button>
    <el-table
        :data="tableData"
        style="width: 40%"
        ref="singleTable"
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
          <i class="el-icon-star-on">{{scope.row.stargazers_count}}</i>
        </template>
      </el-table-column>
      <el-table-column
          label="Fork"
          prop="forks">
        <template slot-scope="scope">
          <i class="el-icon-share">{{scope.row.forks}}</i>
        </template>
      </el-table-column>
    </el-table>
    <div>

    </div>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: "RepositoryBased",
  data() {
    return {
      currentRow: null
      // tableData: []
      // tableData: [{
      //   id: '12987122',
      //   name: '好滋好味鸡蛋仔',
      //   category: '江浙小吃、小吃零食',
      //   desc: '荷兰优质淡奶，奶香浓而不腻',
      //   address: '上海市普陀区真北路',
      //   shop: '王小虎夫妻店',
      //   shopId: '10333'
      // }, {
      //   id: '12987123',
      //   name: '好滋好味鸡蛋仔',
      //   category: '江浙小吃、小吃零食',
      //   desc: '荷兰优质淡奶，奶香浓而不腻',
      //   address: '上海市普陀区真北路',
      //   shop: '王小虎夫妻店',
      //   shopId: '10333'
      // }, {
      //   id: '12987125',
      //   name: '好滋好味鸡蛋仔',
      //   category: '江浙小吃、小吃零食',
      //   desc: '荷兰优质淡奶，奶香浓而不腻',
      //   address: '上海市普陀区真北路',
      //   shop: '王小虎夫妻店',
      //   shopId: '10333'
      // }, {
      //   id: '12987126',
      //   name: '好滋好味鸡蛋仔',
      //   category: '江浙小吃、小吃零食',
      //   desc: '荷兰优质淡奶，奶香浓而不腻',
      //   address: '上海市普陀区真北路',
      //   shop: '王小虎夫妻店',
      //   shopId: '10333'
      // }]
    }
  },
  mounted() {
    console.log("hello");
  },
  computed: {
    ...mapState('test', {
      tableData: state => state.objectList
    })
  },
  methods:{
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
</style>