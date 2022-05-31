<template>
  <div class="main-page">
    <el-container>
      <el-header>
        <div>JTechStack</div>
      </el-header>
      <el-main>
<!--        <div style="float: top">-->
<!--          <el-radio-group v-model="radio" style="float: right">-->
<!--            <el-radio-button label="Repository"></el-radio-button>-->
<!--            <el-radio-button label="Dependency"></el-radio-button>-->
<!--          </el-radio-group>-->
<!--        </div>-->
        <!--        <el-divider></el-divider>-->
        <div style="height:100%; overflow: scroll;">
          <DependencyBased v-if="radio==='Dependency'"/>
          <RepositoryBased v-if="radio==='Repository'"/>
        </div>
      </el-main>
    </el-container>

  </div>
</template>

<script>
import DependencyBased from "@/components/main-page/DependencyBased";
import RepositoryBased from "@/components/main-page/RepositoryBased";

export default {
  name: 'MainPage',
  components: {
    DependencyBased,
    RepositoryBased
  },
  data() {
    return {
      radio: "Repository"
    }
  }
}
</script>

<style scoped>
.main-page, .el-container {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.el-header {
  background: #2c3e50;
  display: flex;
  align-items: center;
}

.el-main {
  padding: 0;
  height: calc(100% - 60px);
  overflow: hidden;
}

.el-header > div {
  color: white;
  font-family: 'Microsoft YaHei', arial, tahoma, \5b8b\4f53, sans-serif;
  font-weight: bold;
  font-size: 2.5rem;
}
</style>
