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
启动Redis客户端, 然后修改名叫 `application-dev.yml` 的配置文件
```yml
# 选择你自己的端口号，默认访问localhost:8080
server:
  port: 8800

spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    #选择自己的url 默认端口是3306
    url: jdbc:mysql://localhost:3306/charles-oa?useUnicode=true&characterEncoding=UTF-8&useSSL=false   
    username: root          #更改成自己的username 
    password: abc123        #更改成自己的password 
    driver-class-name: com.mysql.jdbc.Driver

# 根据自己的需求来改
redis:
    host: localhost
    port: 6379

# 最后一个需要改的地方
wechat:
  mpAppId: ## 根据自己申请的来填写
  mpAppSecret: ##
  # 授权回调获取用户信息接口地址 免费域名需要更新
  userInfoUrl: ##

```
再启动服务器（ServiceOAApplication是主启动类）

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

