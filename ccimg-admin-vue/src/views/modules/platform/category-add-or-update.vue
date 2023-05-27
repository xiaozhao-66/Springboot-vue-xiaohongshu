<template>
  <div>

<el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
  
  <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
         <el-menu-item index="1">添加一级分类</el-menu-item>
         <el-menu-item index="2">添加二级分类</el-menu-item>
  </el-menu>
  
  <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">  
      <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="一级分类" prop="pid"   v-show="activeIndex=='2'">
          <template slot-scope="scope">
            <el-select v-model="dataForm.pid" filterable placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </template>
      </el-form-item>
      <el-form-item label="排序" prop="sort" >
        <template slot-scope="scope">
          <el-input-number v-model="dataForm.sort" @change="handleChange" :min="1" :max="10" label="描述文字"></el-input-number>
        </template>
      </el-form-item>
      <el-form-item label="图片上传"  >
        <template slot-scope="scope">
          <el-upload
            action="http://localhost:88/api/utils/fileoss/uploadOssFile"
            list-type="picture-card"
            :on-success="handlePictureSuccess"
            :on-remove="handleRemove"
            :limit = 'limit'
            >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="imageUrl" alt="">
          </el-dialog>
        </template>
      </el-form-item>
      <el-form-item label="描述" prop="description"   v-show="activeIndex=='2'">
        <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="dataForm.description">
        </el-input>
      </el-form-item>
      </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
  </div>
</template>

<script>
import debounce from 'lodash/debounce'
import category from '@/api/category'
export default {
  data () {
    return {
      visible: true,
      activeIndex:'1',
      dataForm: {
         sort:'1',
      },
      options: [],
        value: '',
        imageUrl: '',
        imgList:[],
        dialogVisible: false,
        num: 1,
        limit:2,

    }
  },
  computed: {
    dataRule () {
      return {
        name: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        pid: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        sort: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updater: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updateDate: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ]
      }
    }
  },
  created(){
      this.getOneCategory()
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        // if (this.dataForm.id) {
        //   this.getInfo()
        // }
      })
    },
    handleSelect(key, keyPath) {
        this.activeIndex = keyPath
        if(key==2){
          this.limit=1
        }
    },
    handleChange(value) {
        console.log(value);
    },

    handleRemove(file, fileList) {
        console.log(file, fileList);
    },
  
    handlePictureSuccess(res, file){
        this.imgList.push(res.data)
        console.log(this.imgList)
    },

    getOneCategory(){
        category.getAllCategory().then(res=>{
           this.options = res.data.data
        })
      },
    // 表单提交
    dataFormSubmitHandle(){
      this.dataForm.cover = this.imgList[0]
        if(this.activeIndex=='1'){
          this.dataForm.pid=0
          this.dataForm.hotCover = this.imgList[1]
        }
       console.log('提交',this.dataForm)
       category.addCategory(this.dataForm).then(res=>{
         this.visible = false
       })
    }
  }
}
</script>
