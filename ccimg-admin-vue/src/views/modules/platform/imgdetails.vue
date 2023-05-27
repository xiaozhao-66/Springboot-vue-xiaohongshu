<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-platform__imgdetails}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.id" placeholder="id" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('platform:imgdetails:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('platform:imgdetails:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="title" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="content" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="cover" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="userId" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="categoryId" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="imgsUrl" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="sort" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="status" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="creator" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="createDate" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="updater" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="updateDate" label="" header-align="center" align="center"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('platform:imgdetails:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('platform:imgdetails:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="limit"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandle"
        @current-change="pageCurrentChangeHandle">
      </el-pagination>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './imgdetails-add-or-update'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/platform/imgdetails/page',
        getDataListIsPage: true,
        exportURL: '/platform/imgdetails/export',
        deleteURL: '/platform/imgdetails',
        deleteIsBatch: true
      },
      dataForm: {
        id: ''
      }
    }
  },
  components: {
    AddOrUpdate
  }
}
</script>
