<template>
    <div class="test-component">
        <el-input v-model="input"
                  style="width: 400px; margin-right: 10px;"
                  placeholder="Please input"></el-input>
        <el-button @click="addObject">Add</el-button>
        <el-table
                :data="objectList"
                style="width: 100%">
            <el-table-column
                    prop="id"
                    label="ID"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="str"
                    label="String">
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import {mapState} from 'vuex';

export default {
    name: 'TestComponent',
    data() {
        return {
            input: '',
        }
    },
    mounted() {
        this.$store.dispatch('test/getAllTestObject');
    },
    methods: {
        addObject() {
            if (this.input) {
                this.$store.dispatch('test/createTestObject', this.input);
            } else {
                this.$message('Can\'t add empty string!');
            }
        }
    },
    computed: {
        ...mapState('test', {
            objectList: state => state.objectList,
        }),
    }
}
</script>

<style scoped>

</style>
