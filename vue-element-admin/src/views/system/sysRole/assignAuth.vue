<template>
    <div class="app-container">
      <div style="padding: 20px 20px 0 20px;">
        授权角色：{{ $route.query.roleName }}
      </div>
      <el-tree
        style="margin: 20px 0"
        ref="tree"
        :data="allMenuList"
        node-key="id"
        show-checkbox
        default-expand-all
        :props="defaultProps"
      />
      <div style="padding: 20px 20px;">
        <el-button :loading="loading" type="primary" icon="el-icon-check" size="mini" @click="save">保存</el-button>
        <el-button @click="$router.push({path:`/system/sysRole`})" size="mini" icon="el-icon-refresh-right">返回</el-button>
      </div>
    </div>
  </template>
  <script>
    import api from '@/api/system/sysMenu'
    export default {
      name: 'roleAuth',
  
      data() {
        return {
          loading: false, // 用来标识是否正在保存请求中的标识, 防止重复提交
          sysMenuList: [], // 当前用户的角色列表
          allMenuList: [], //所有角色列表
          defaultProps: {
            children: 'children',
            label: 'name'
          },
        };
      },
  
      created() {
        this.fetchData()
      },
  
      methods: {
        /*
        初始化
        */
        fetchData() {
          api.findNodes().then(result => {
            this.allMenuList = result.data
          })

          const roleId = this.$route.query.id
          api.toAssign(roleId).then(respond => {
            const sysMenuList = respond.data
            this.sysMenuList = sysMenuList
            this.$refs.tree.setCheckedKeys(this.sysMenuList)
          })
        },
  
        /*
        保存权限列表
        */
        save() {
          // debugger
          // //获取到当前子节点
          // //const checkedNodes = this.$refs.tree.getCheckedNodes()
          // //获取到当前子节点及父节点
          const allCheckedNodes = this.$refs.tree.getCheckedNodes(false, true);
          let idList = allCheckedNodes.map(node => node.id);
          
          api.doAssign(idList,this.$route.query.id).then(result => {
            this.loading = false        
            this.$router.push('/system/sysRole')
            this.$message.success(result.$message || '分配权限成功')
          }).catch(() => {
              this.$message.info('修改失败')
          })
          // let assginMenuVo = {
          //   roleId: this.$route.query.id,
          //   menuIdList: idList
          // }
          // this.loading = true
          // api.doAssign(assginMenuVo).then(result => {
          //   this.loading = false
          //   this.$message.success(result.$message || '分配权限成功')
          //   this.$router.push('/system/sysRole');
          // })
        }
      }
    };
  </script>