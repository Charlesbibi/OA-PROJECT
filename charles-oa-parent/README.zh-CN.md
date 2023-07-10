**[中文] | [English](README.md)**

# 1. common
在这个包里面包含的所有configuration和提取出来的公共部分。比如说MyBatis的配置类、SpringSecurity的配置类等等。当然其中还有自定义加密器MD5和自定义的统一结果返回等基础性配置。

# 2. model
在这个包中包含了所有的实体类</br>
在**vo**包中的类用于封装forn-end发送的搜索信息。</br>
在**system**包中的类与管理员端相关。</br>
在**service**包中的类显示了微信端所有需要的实体。

# 3. service-oa
在这个包中包含了所有接口功能的实现逻辑</br>
特别要注意的是！！！！</br>
在  src/main/resources 路径下包含这两个配置文件，它们分别是 application-dev.yml 和 application.yml
### 在配置文件 application.yml 中:</br>
```yml
# 这一部分你需要自己配置，每一个人的微信号都不同
wechat:
  mpAppId: ## 根据你的申请填写
  mpAppSecret: ## 根据你的申请填写
  # 授权回调获取用户信息 接口地址 免费域名需要更新
  userInfoUrl: ##
```
