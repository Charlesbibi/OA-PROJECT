  **[English] | [中文](README.zh-CN.md)**

# 1. common
All configurations and extracted public parts included in this package. For example, MyBatis configuration classes, SpringSecurity configuration classes and etc.. Of course, there are custom encryptors MD5 and custom unified results return and other basic configuration.

# 2. model
In this package, all entity classes are included. </br>
The class in **vo** package is used to wrapper searching information send by forn-end. </br>
The class in **system** package are related with administrator side. </br>
The class in **process** package show all require entity of wechat side.

# 3. service-oa
This package is the main package which contain all functionality. </br>
Be carefull!!! In the path src/main/resources contain application-dev.yml and application.yml. This two files are the configuration of whole project.</br>
### In the application.yml:</br>
```yml
# This one you need to configure by yourself. Everyone's WeChat is different.
wechat:
  mpAppId: ## according to your application
  mpAppSecret: ## according to your application
  # Authorization callback to get user information interface address Free domains need to be updated
  userInfoUrl: ##
```
