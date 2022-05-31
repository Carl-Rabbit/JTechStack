<template>
  <div class="main-page">
    <el-container>
      <el-header>

      </el-header>
      <el-main>
        <div style="float: top">
          <el-radio-group v-model="radio" style="float: right">
            <el-radio-button label="Repository"></el-radio-button>
            <el-radio-button label="Dependency"></el-radio-button>
          </el-radio-group>
        </div>
<!--        <el-divider></el-divider>-->
        <div style="clear:both;">
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

</style>
