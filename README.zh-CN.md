**[中文] | [English](./README.md)**
# 1. charles-oa-parent
该模块包含了所有的后台代码，用于实现员工端的系统操作，包括员工管理、职位管理、部门管理、菜单管理，以及向员工发放申请的操作。申请模板、申请流程等。
## 1.1 技术方面
### 基本框架：
> SpringBoot + MyBatis + MyBatisPlus + Redis + 微信公众号开发
### 工作流引擎:
> Activiti
### 测试平台：
> knife4j
### 安全性：
> SpringSecurity
## 1.2 版本号
```xml
<java.version>1.8</java.version>
<mybatis-plus.version>3.4.1</mybatis-plus.version>
<mysql.version>8.0.30</mysql.version>
<knife4j.version>3.0.3</knife4j.version>
<activiti.version>7.1.0-M6</activiti.version>
<springboot.version2.3.6.RELEASE</springboot.version>
```
## 1.3 后端启动
启动Redis客户端 再启动服务器
# 2. oa-wechat-web
本模块包含点击微信公众号后显示的所有前台页面，包括申请、处理、我的、详情等功能。
## 2.1 版本号
```json
"node-sass": "^6.0.1",
"sass-loader": "^10.2.0"
"node": "16.19.1"
"element-ui": "2.13.2"
"vue": "^2.6.11"
```
## 2.2 准备工作

```shell
# 下载npm 如果能显示版本号就是ok的
node -v
npm -v
# 换镜像源
npm config set registry http://registry.npm.taobao.org/

# 下载所有所需模块
npm install
```

## 2.3 启动微信端服务

```shell
npm run serve
```
# 3. vue-element-admin
这部分使用的模板，它提供了非常专业的可视化界面。因此，我使用它进行二次开发，添加相关页面和接口来访问后端服务器。
## 3.1 版本号
```json
"vue": "2.6.10"
"node": "16.19.1"
```
## 3.2 前端启动
```shell
npm run dev
```

