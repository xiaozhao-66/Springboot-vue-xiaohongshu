<template>
  <div>
    <el-button  type="primary" @click="add">增加</el-button>
    <el-table
    :data="dataList"
    style="width: 100%;margin-bottom: 20px;"
    row-key="id"
    border
   
    :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
    <el-table-column
      prop="id"
      label="id"
      sortable
      width="220">
    </el-table-column>
    <el-table-column
      prop="name"
      label="名称"
      sortable
      width="80">
    </el-table-column>
    <el-table-column
      label="标签"
      width="80">

      <template slot-scope="scope">
        <el-tag v-if="scope.row.pid === 0" size="small">一级分类</el-tag>
        <el-tag v-else size="small" type="info">二级分类</el-tag>
      </template>

    </el-table-column>
    <el-table-column
      prop="createDate"
      label="创建日期">
    </el-table-column>
    <el-table-column
      prop="updateDate"
      label="修改日期">
    </el-table-column>

    <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="edit(scope.row.id)">编辑</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
  </el-table>

   <!-- 弹窗, 新增 / 修改 -->
   <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" ></add-or-update>
   
  </div>
</template>

<script>

import category from "@/api/category";
import AddOrUpdate from "./category-add-or-update";
export default {
  components:{
    AddOrUpdate
  },
data() {
      return {
        dataList:[],
        addOrUpdateVisible:false
      }
    },
    created(){
        this.getAllCategory()
    },
    methods: {

       
      getAllCategory(){
           category.getAllCategory().then(res=>{
              this.dataList = res.data.data
           })
      },

      add(){
         this.addOrUpdateVisible = true
      }
    },
  }
</script>
