**[English] | [中文](./README.zh-CN.md)**

# 1. charles-oa-parent
This moudle contains all the back-end code that implements the system operations on the employee side, including employee management, job management, department management, menu management, and the operation of applications issued to employees. The application template, application process, etc.
## 1.1 For technology
### Basic Framework:
> SpringBoot + MyBatis + MyBatisPlus + Redis + WeChat official account development
### Workflow Engines:
> Activiti
### Testing Platform
> knife4j
### Security
> SpringSecurity
## 1.2 version 
```xml
<java.version>1.8</java.version>
<mybatis-plus.version>3.4.1</mybatis-plus.version>
<mysql.version>8.0.30</mysql.version>
<knife4j.version>3.0.3</knife4j.version>
<activiti.version>7.1.0-M6</activiti.version>
<springboot.version2.3.6.RELEASE</springboot.version>
```

## 1.3 Back-end services start
Start the Redis client first, and modify the configuration file names `application-dev.yml` 
```yml
# Select your own port number to access localhost: 8080
server:
  port: 8800

spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    #change the url, default port is 3306
    url: jdbc:mysql://localhost:3306/charles-oa?useUnicode=true&characterEncoding=UTF-8&useSSL=false   
    username: root          #change the username 
    password: abc123        #change the password 
    driver-class-name: com.mysql.jdbc.Driver

# Modify the port and host according to your needs.
redis:
    host: localhost
    port: 6379

# final part need to modify
wechat:
  mpAppId: ## Fill in based on your own application. 
  mpAppSecret: ##
  # Authorize callback to obtain user information interface address. Free domain name needs to be updated.
  userInfoUrl: ##

```
and then start the server （ServiceOAApplication is the Main launch class）


# 2. oa-wechat-web
This moudle contains all front-end pages displayed after clicking on the WeChat public page, including functions such as making application, processing application, my, details, etc.
## 2.1 version
```json
"node-sass": "^6.0.1",
"sass-loader": "^10.2.0"
"node": "16.19.1"
"element-ui": "2.13.2"
"vue": "^2.6.11"
```
## 2.2 Preparations

```shell
# install npm if version is appear that means ok  
node -v
npm -v
# config npm taobao mirror (for China)
npm config set registry http://registry.npm.taobao.org/

# installation of all modules
npm install
```

## 2.3 Start Project

```shell
npm run serve
```
# 3. vue-element-admin
The template is used in this part, it provided very prowerfull visualization interface. Therefore, I use it for secondary development, adding relevant pages and interfaces to access the back-end server
## 3.1 version
```json
"vue": "2.6.10"
"node": "16.19.1"
```

## 3.2 The front-end service starts
```shell
npm run dev
```
