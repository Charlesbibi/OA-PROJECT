<template>
    <div class="app-container">
      <!--查询表单-->
      <div class="search-div">
          <el-form label-width="70px" size="small">
          <el-row>
              <el-col :span="24">
              <el-form-item label="角色名称">
                  <el-input style="width: 100%" v-model="searchObj.name" placeholder="岗位名称"></el-input>
              </el-form-item>
              </el-col>
          </el-row>
          <el-row style="display:flex">
              <el-button type="primary" icon="el-icon-search" size="mini" :loading="loading" @click="fetchData()"
                        :disabled="$hasBP('bnt.sysPost.list')  === false">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
              <!-- 工具条 -->
  
              <el-button type="success" icon="el-icon-plus" size="mini" @click="add" :disabled="$hasBP('bnt.sysPost.add')  === false">添 加</el-button>
          </el-row>
          </el-form>
      </div>
      <!-- 表格 -->
      <el-table
        v-loading="listLoading"
        :data="list"
        stripe
        border
        style="width: 100%;margin-top: 10px;">
  
        <el-table-column type="selection"/>
  
        <el-table-column
          label="序号"
          width="70"
          align="center"
          prop="id" />
  
        <el-table-column prop="name" label="岗位名称" />
        <el-table-column prop="postCode" label="岗位编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="createTime" label="创建时间" width="160"/>
        <el-table-column prop="updateTime" label="创建时间" width="160"/>
        <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          <el-switch
            :active-value="1"
            :inactive-value="0"
            v-model="scope.row.status"
            active-color="green" 
            inactive-color="red"    
            @change="switchStatus(scope.row.id)">
          </el-switch>
        </template>
      </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row)" title="修改"
                      :disabled="$hasBP('bnt.sysPost.update')  === false"/>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="删除"
                      :disabled="$hasBP('bnt.sysPost.remove')  === false"/>
          </template>
        </el-table-column>
      </el-table>
     
      <!--定义弹出层-->
      <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%" @close="cancel">
        <el-form ref="dataForm" :model="sysPost" label-width="150px" size="small" style="padding-right: 40px;">
          <el-form-item label="职位名称">
            <el-input v-model="sysPost.name"/>
          </el-form-item>
          <el-form-item label="职位编码">
            <el-input v-model="sysPost.postCode"/>
          </el-form-item>
          <el-form-item label="职位描述">
            <el-input v-model="sysPost.description"/>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="cancel()" size="small" icon="el-icon-refresh-right">取 消</el-button>
          <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import api from '@/api/system/sysPost'
  export default {
    // 定义数据模型
    data() {
      return {
          list: [], // 列表
          searchObj: {}, // 查询条件

          dialogVisible: false, //弹窗默认不显示 
          sysPost: {}, //添加或者修改中封装的对象
      }
    },
    // 页面渲染成功后获取数据
    created() {
      this.fetchData()
    },
    // 定义方法
    methods: {      

      cancel(){
        this.dialogVisible = false
        this.sysPost = {}
      },

        switchStatus(id){
            api.changeStatus(id).then(response => {
                this.$message.success(response.message || '操作成功')
            })
        },
        add() {
          this.dialogVisible = true
        },
        edit(row) {
          this.dialogVisible = true
          this.sysPost = row    
        },
        saveOrUpdate(){ 
          this.saveBtnDisabled = true // 防止表单重复提交
          if(!this.sysPost.id)
            this.save() //如果没有id则是添加
          else
            this.update()  //如果有id则是修改
        },
        save(){
          api.addSysPost(this.sysPost).
            then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogVisible = false
                this.sysPost = {}
                this.fetchData()
            })
        },
        update(){
          // debugger
          this.$confirm('此操作将永久更新该记录, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => {
            api.updateSysPost(this.sysPost).then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogVisible = false
                this.sysPost = {}
                this.fetchData()
            })   
          })
        },
        fetchData() {
          
          // 调用api
          api.getPosts(this.searchObj).then(response => {
            this.list = response.data
          })
        },
      // 根据id删除数据
        removeDataById(id) {
          // debugger
          this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => { // promise
              // 点击确定，远程调用ajax
              return api.deleteSysPostById(id)
          }).then((response) => {
              this.fetchData()
              this.$message.success(response.message || '删除成功')
          })
        },
        // 重置表单
        resetData() {
            console.log('重置查询表单')
            this.searchObj = {}
            this.fetchData()
        }
  
    }
  }
  </script>