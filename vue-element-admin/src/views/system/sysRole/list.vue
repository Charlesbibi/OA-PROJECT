<template>
  <div class="app-container">
    <!--查询表单-->
    <div class="search-div">
        <el-form label-width="70px" size="small">
        <el-row>
            <el-col :span="24">
            <el-form-item label="角色名称">
                <el-input style="width: 100%" v-model="searchObj.roleName" placeholder="角色名称"></el-input>
            </el-form-item>
            </el-col>
        </el-row>
        <el-row style="display:flex">
            <el-button type="primary" icon="el-icon-search" size="mini" :loading="loading" @click="fetchData()"
                      :disabled="$hasBP('bnt.sysRole.list')  === false">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
            <!-- 工具条 -->

            <el-button type="success" icon="el-icon-plus" size="mini" @click="add" :disabled="$hasBP('bnt.sysRole.add')  === false">添 加</el-button>
            <el-button class="btn-add" size="mini" @click="batchRemove()" :disabled="$hasBP('bnt.sysRole.remove')  === false">批量删除</el-button>
            
        </el-row>
        </el-form>
    </div>
    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%;margin-top: 10px;"
      @selection-change="handleSelectionChange">

      <el-table-column type="selection"/>

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" width="160"/>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row.id)" title="修改"
                    :disabled="$hasBP('bnt.sysRole.update')  === false"/>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="删除"
                    :disabled="$hasBP('bnt.sysRole.remove')  === false"/>
          <el-button type="warning" icon="el-icon-baseball" size="mini" @click="showAssignAuth(scope.row)" title="分配权限"
                    :disabled="$hasBP('bnt.sysRole.assignAuth')  === false"/>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
        :current-page="page"
        :total="total"
        :page-size="limit"
        style="padding: 30px 0; text-align: center;"
        layout="total, prev, pager, next, jumper"
        @current-change="fetchData"
    />
    <!--定义弹出层-->
    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%" >
      <el-form ref="dataForm" :model="sysRole" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="角色名称">
          <el-input v-model="sysRole.roleName"/>
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="sysRole.roleCode"/>
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="sysRole.description"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/system/sysRole'
export default {
  // 定义数据模型
  data() {
    return {
        list: [], // 列表
        total: 0, // 总记录数
        page: 1, // 页码
        limit: 10, // 每页记录数
        searchObj: {}, // 查询条件
        multipleSelection: [],// 批量删除选中的记录列表
        
        idList: [],  //复选框选中id的数组
        dialogVisible: false, //弹窗默认不显示 
        sysRole: {}, //添加或者修改中封装的对象
    }
  },
  // 页面渲染成功后获取数据
  created() {
    this.fetchData()
  },
  // 定义方法
  methods: {

      showAssignAuth(row) {
        this.$router.push('/system/assignAuth?id='+row.id+'&roleName='+row.roleName);
      },

      handleSelectionChange(selection){
        this.idList=[]
        //复选框需要记录当前选中的数据对应的id值
        selection.forEach(element => {
          var id = element.id
          this.idList.push(id)
        });
      },
      batchRemove() {
        //当前没有选中复选框
        if(this.idList.length == 0){
          this.$message.warning('请选择要删除的记录')
          return 
        }
         this.$confirm('此操作将永久删除这些记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
          api.deleteByPatchId(this.idList).then(response => {
              this.$message.success(response.message || '操作成功')
              this.fetchData(this.page)
          })   
        })
      },
      add() {
        this.dialogVisible = true
      },
      edit(roleId) {
        this.dialogVisible = true
        api.getSysRoleById(roleId).then(response => {
            this.sysRole = response.data
        })    
      },
      saveOrUpdate(){ 
        //this.saveBtnDisabled = true // 防止表单重复提交
        if(!this.sysRole.id)
          this.save() //如果没有id则是添加
        else
          this.update()  //如果有id则是修改
      },
      save(){
        api.addSysRole(this.sysRole).
          then(response => {
              this.$message.success(response.message || '操作成功')
              this.dialogVisible = false
              this.sysRole = {}
              this.fetchData(this.page)
          })
      },
      update(){
        // debugger
        this.$confirm('此操作将永久更新该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
          api.updateSysRole(this.sysRole).then(response => {
              this.$message.success(response.message || '操作成功')
              this.dialogVisible = false
              this.sysRole = {}
              this.fetchData(this.page)
          })   
        })
      },
      fetchData(current=1) {
        this.page = current
        // 调用api
        api.getPageList(this.page, this.limit, this.searchObj).then(response => {
          this.list = response.data.records
          this.total = response.data.total
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
            return api.deleteSysRoleById(id)
        }).then((response) => {
            this.fetchData(this.page)
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