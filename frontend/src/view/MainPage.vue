<template>
    <div class="main-page">
        <el-container>
            <el-header>
                <div>JTechStack</div>
            </el-header>
            <el-main>
                <div style="height:100%; width: 40%; overflow: scroll; float: left">
                    <RepositoryBased v-if="radio==='Repository'"/>
                </div>
                <div style="height: 100%; padding-left: 45%">
                    <TreeGraph></TreeGraph>
                </div>

            </el-main>

        </el-container>
    </div>
</template>

<script>
// import DependencyBased from "@/components/main-page/DependencyBased";
import RepositoryBased from "@/components/main-page/RepositoryBased";
import TreeGraph from "@/components/main-page/treeGraph";

export default {
    name: 'MainPage',
    components: {
        TreeGraph,
        // DependencyBased,
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

/*
#tree {
    position: relative;
    height: 100vh;
    overflow: hidden;
}*/
</style>
