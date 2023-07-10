**[English] | [中文](./README.zh-CN.md)**

# 1. charles-oa-parent
This moudle contains all the back-end code that implements the system operations on the employee side, including employee management, job management, department management, menu management, and the operation of applications issued to employees. The application template, application process, etc.
## 1.1 For technology
### Basic Framework:
> SpringBoot + MyBatis + MyBatisPlus + Redis + 微信公众号开发
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
# 3. vue-element-admin
The template is used in this part, it provided very prowerfull visualization interface. Therefore, I use it for secondary development, adding relevant pages and interfaces to access the back-end server
## 3.1 version
```json
"vue": "2.6.10"
"node": "16.19.1"
```
