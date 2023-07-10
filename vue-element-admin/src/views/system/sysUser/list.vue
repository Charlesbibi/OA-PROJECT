<template>
  <div class="app-container">

    <div class="search-div">
      <el-form label-width="100px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="请选择关键字">
              <input type="radio" name="radios" value="1" v-model="param"><label>用户名</label>
              <input type="radio" name="radios" value="2" v-model="param"><label>姓名</label>
              <input type="radio" name="radios" value="3" v-model="param"><label>手机号码</label>

              <el-input style="width: 95%" v-model="keyword" placeholder="请输入值"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作时间">
              <el-date-picker
                v-model="createTimes"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="margin-right: 10px;width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini" :loading="loading" @click="fetchData()" :disabled="$hasBP('bnt.sysUser.list')  === false">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>

          <el-button type="success" icon="el-icon-plus" size="mini" @click="add" :disabled="$hasBP('bnt.sysUser.add')  === false">添 加</el-button>
        </el-row>
      </el-form>
    </div>

    <!-- 列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%;margin-top: 10px;">

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="用户名" width="100"/>
      <el-table-column prop="name" label="姓名" width="70"/>
      <el-table-column prop="phone" label="手机" width="120"/>
      <el-table-column prop="postName" label="岗位" width="100"/>
      <el-table-column prop="deptName" label="部门" width="100"/>
      <el-table-column label="所属角色" width="130">
        <template slot-scope="scope">
          <template v-if="scope.row.roleList!=undefined">
            <span v-for="item in scope.row.roleList" :key="item.id" style="margin-right: 10px;">{{ item.roleName }}</span>
          </template>  
          <template v-if="scope.row.roleList==undefined">
            <span style="margin-right: 10px;">暂未分配角色</span>
          </template>    
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          <el-switch
            :active-value="1"
            :inactive-value="0"
            v-model="scope.row.status"
            active-color="green" 
            inactive-color="red"    
            @change="switchStatus(scope.row.status,scope.row.id)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160"/>

      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template slot-scope="scope">
          <div style="margin-bottom:10px;">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row.id)" title="修改" 
                    :disabled="$hasBP('bnt.sysUser.update')  === false"/>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="删除" 
                    :disabled="$hasBP('bnt.sysUser.remove')  === false"/>
            <el-button type="warning" icon="el-icon-baseball" size="mini" @click="showAssignRole(scope.row)" title="分配角色" 
                    :disabled="$hasBP('bnt.sysUser.assignRole')  === false"/>
          </div>
          <div>
            <el-button type="warning" icon="el-icon-tickets" size="mini" @click="showAllocatePost(scope.row)" title="分配岗位" 
                    :disabled="$hasBP('bnt.sysPost.allocatePost')  === false"/>
            <el-button type="warning" icon="el-icon-s-cooperation" size="mini" @click="showAllocateDept(scope.row)" title="分配部门" 
                    :disabled="$hasBP('bnt.sysDept.allocateDept')  === false"/>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: center;"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="fetchData"
      @size-change="changeSize"
    />

    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%" >
      <el-form ref="dataForm" :model="sysUser"  label-width="100px" size="small" style="padding-right: 40px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="sysUser.username"/>
        </el-form-item>
        <el-form-item v-if="!sysUser.id" label="密码" prop="password">
          <el-input v-model="sysUser.password" type="password"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="sysUser.name"/>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="sysUser.phone"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="分配角色" :visible.sync="dialogRoleVisible">
      <el-form label-width="80px">
        <el-form-item label="用户名">
          <el-input disabled :value="sysUser.username"></el-input>
        </el-form-item>

        <el-form-item label="角色列表">
          <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
          <div style="margin: 15px 0;"></div>
          <el-checkbox-group v-model="originalRoleList" @change="handleCheckedChange">
            <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id">{{role.roleName}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="assignRole" size="small">保存</el-button>
        <el-button @click="dialogRoleVisible = false" size="small">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="分配岗位" :visible.sync="dialogPostVisible">
      <el-form label-width="80px">
        <el-form-item label="用户名">
          <el-input disabled :value="sysUser.username"></el-input>
        </el-form-item>

        <el-form-item label="岗位列表">
          <div style="margin: 15px 0;"></div>
          <el-radio-group size="medium" v-model="originalPostId">
            <el-radio v-for="post in allPosts" :key="post.id" :label="post.id">{{post.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="assignPost" size="small">保存</el-button>
        <el-button @click="dialogPostVisible = false" size="small">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="分配部门" :visible.sync="dialogDeptVisible">
      <el-form label-width="80px">
        <el-form-item label="用户名">
          <el-input disabled :value="sysUser.username"></el-input>
        </el-form-item>

        <el-form-item label="选择部门">
          <div style="margin: 15px 0;"></div>
          
          <el-cascader
                expand-trigger="hover"
                :options="allDepts"
                v-model="currentDept"
                clearable
                filterable
                :props="{label:'name',value:'id',children:'children'}"
          ></el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="assignDept" size="small">保存</el-button>
        <el-button @click="cancelDeptDiag()" size="small">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/system/sysUser'
import api_r from '@/api/system/sysRole'
import api_p from '@/api/system/sysPost'
import api_d from '@/api/system/sysDept'
const defaultForm = {
  id: '',
  username: '',
  password: '',
  name: '',
  phone: '',
  status: 1
}
export default {
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: null, // banner列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象
      param: 1, //复选框的初始值
      keyword: '', //表单查询的值

      createTimes: [],

      dialogVisible: false,
      sysUser: defaultForm,
      saveBtnDisabled: false,

      dialogRoleVisible: false,
      originalRoleList: [], //当前被选中用户的角色
      allRoles: [], //所有角色
      allRolesId: [], //所有角色的id
      checkAll: false, //全选

      dialogPostVisible: false,
      originalPostId: 0, //当前被选中用户的岗位
      allPosts: [],

      dialogDeptVisible: false,
      allDepts: [], //全部部门列表
      currentDept: [], //当前用户的部门id
    }
  },

  // 生命周期函数：内存准备完毕，页面尚未渲染
  created() {
    console.log('list created......') 
    this.fetchData()

    this.getAllRoles()
    this.getAllPosts()
    this.getAllDepts()
  },

  // 生命周期函数：内存准备完毕，页面渲染成功
  mounted() {
    console.log('list mounted......')
  },

  methods: {
    //展示部门分配弹窗
    showAllocateDept(row){
      this.dialogDeptVisible = true
      this.sysUser = row
      
      api_d.findTreePath(row.deptId===undefined?0:row.deptId).then(response => {
          this.currentDept = response.data
      })
    },
    //获取所有部门信息
    getAllDepts(){
      api_d.findNodes().then(response => {
        this.allDepts = response.data
      })
    },
    cancelDeptDiag(){
      this.dialogDeptVisible = false
      this.currentDept = []
    },
    assignDept(){
      if(this.currentDept.length === 0){
        this.$message.error('请先选择部门')
      }else{
        api_d.allocateDept(this.sysUser.id,this.currentDept[this.currentDept.length-1]).then(() => {
        this.dialogDeptVisible = false
        this.currentDept = []
        this.$message.success('分配部门成功')
      }).catch(() => {
         this.$message.info('取消修改')
      })}
      
    },
    
    //全选项
    handleCheckAllChange(){
      this.checkAll === false ? true : false

      if(this.checkAll)
        this.originalRoleList = this.allRolesId
      else
        this.originalRoleList = []  
    },
    //分项
    handleCheckedChange() {
      if(this.originalRoleList.length === this.allRoles.length)
        this.checkAll = true
      else
        this.checkAll = false  
    },

    //获取所有的岗位列表
    showAllocatePost(row) {
      this.dialogPostVisible = true
      this.sysUser = row
      this.getThePost(this.sysUser.id)
    },
     //获取当前用户岗位
     getThePost(id) {
      api.getPostById(id).then(response => {
        this.originalPostId = response.data
      })
    },
    //分配岗位
    assignPost() {
      this.$confirm('确认要改变 ' + this.sysUser.username + ' 的岗位吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        return api.allocatePost(this.sysUser.id,this.originalPostId)
      }).then(() => {
        this.dialogPostVisible = false
        this.originalPostId = 0
        this.$message.success('修改角色成功')
      }).catch(() => {
         this.$message.info('取消修改')
      })
    },

    //分配角色
    assignRole() {
      this.$confirm('确认要改变 ' + this.sysUser.username + ' 的职位吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        return api.AssignRole(this.sysUser.id,this.originalRoleList)
      }).then(() => {
        this.dialogRoleVisible = false
        this.checkAll = false
        this.originalRoleList = [] //所有角色的id
        this.$message.success('修改角色成功')
      }).catch(() => {
         this.$message.info('取消修改')
      })
    },

    getAllRoles(){
      api_r.getRoles().then(response => {
        this.allRoles = response.data;
        //获取所有的id集合
        for(let i=0;i<this.allRoles.length;i++){
            this.allRolesId[i] = this.allRoles[i].id
        }
      })
    },
    getAllPosts(){
      api_p.getPosts(null).then(response => {
        this.allPosts = response.data;
      })
    },

    //显示当前用户的所有职位
    showAssignRole(row) {
      this.dialogRoleVisible = true
      this.sysUser = row
      this.getTheRole(this.sysUser.id)
    },
    //获取当前用户所有职位
    getTheRole(id) {
      api.getAllRoleFromUser(id).then(response => {
        this.originalRoleList = response.data
      })
    },

    //更改当前员工状态
    switchStatus(status,id) {
      api.changeStatus(status,id).then(() => {
        this.$message.success('修改状态成功')
      }).catch(() => {
         this.$message.info('修改失败')
      })
    },

    // 当页码发生改变的时候
    changeSize(size) {
      console.log(size)
      this.limit = size
      this.fetchData(1)
    },

    // 加载banner列表数据
    fetchData(page = 1) {
      debugger
      this.page = page
      this.searchObj = {}
      console.log('翻页。。。' + this.page)

      if(this.createTimes && this.createTimes.length ==2) {
        this.searchObj.createTimeBegin = this.createTimes[0]
        this.searchObj.createTimeEnd = this.createTimes[1]
      }
      
      if(!(this.keyword == null || this.keyword == '')){
       
        if(this.param == 1)
          this.searchObj.keyword = this.keyword
        else if(this.param == 2)
          this.searchObj.name = this.keyword
        else if(this.param == 3)
          this.searchObj.phone = this.keyword    
        else
          return
      }
        
      api.getPageList(this.page, this.limit, this.searchObj).then(
        response => {
          //this.list = response.data.list
          this.list = response.data.records
          this.total = response.data.total

          // 数据加载并绑定成功
          this.listLoading = false
        }
      )
    },

    // 重置查询表单
    resetData() {
      this.searchObj = {}
      this.createTimes = []
      this.param = 1
      this.keyword = ''
      this.checkAll = false //全选
      this.fetchData()
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
        return api.deleteUserById(id)
      }).then((response) => {
        this.fetchData(this.page)
        this.$message.success(response.message || '删除成功')
      }).catch(() => {
         this.$message.info('取消删除')
      })
    },

    // -------------
    add(){
      this.dialogVisible = true
      this.sysUser = Object.assign({}, defaultForm)
    },

    edit(id) {
      this.dialogVisible = true
      this.sysUser = this.fetchDataById(id)
    },

    fetchDataById(id) {
      api.getUserById(id).then(response => {
        this.sysUser = response.data
      })
    },

    saveOrUpdate() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.saveBtnDisabled = true // 防止表单重复提交
          if (!this.sysUser.id) {
            this.saveData()
          } else {
            this.updateData()
          }
        }
      })
    },

    // 新增
    saveData() {
      api.addUser(this.sysUser).then(response => {
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.fetchData(this.page)
      })
    },

    // 根据id更新记录
    updateData() {
      api.updateUserById(this.sysUser).then(response => {
        this.$message.success(response.message || '操作成功')
        this.dialogVisible = false
        this.fetchData(this.page)
      })
    }
  }
}
</script>