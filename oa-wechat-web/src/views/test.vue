<template>
  <div>
    <div>账号切换</div>
    <button @click="wjl()" type="default" size="mini">王经理</button>
    <button @click="zhangsan()" type="default" size="mini">张三人事经理</button>
    <button @click="katie()" type="default" size="mini">katie经理</button>
    <button @click="admin()" type="default" size="mini">admin</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    wjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3NjMwNDIx1FEqLU4t8kxRsjKCMPMSc1OBWsqzcpRqAb4pVZBAAAAA.-bW4hJ5UptFiCRsL6jVUa4XbgVwdsfVGfP8gJ-tOVbp7D3JOb8sylbBDLUhTZt6FJsYhlA5uYz8HSRck_pbaCA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zhangsan() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLOwMLQwMzQ301EqLU4t8kxRsjKHMPMSc1OBWqoyEvPSixPzlGoBxO4kDkUAAAA.POtm_j7nFBh0bsMaUvg33LiTZU2Ko8MqdsbKs9XW9BWRWAcJQx5RmlTF-PfFcBKv3X4w634ZOXlm9g3gyNlY8g  '
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    katie() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLOwMDQ0Mzcx1VEqLU4t8kwBiplD2HmJualAPdmJJZmpxoZKtQAH5Yo6RQAAAA.0F0g-S7oZQJkho3B7uruNzNMCSoz42E3bjnKsdXDAd4gdt0BeJxMOkEVrSCkqqlK5KTy57rmQy-kl-bDJETlnQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLOwMDQ0MzMw1lEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQAHMLXYQgAAAA.xNPx0oFBEtVt2OYVu_wYGu-HRhZ1kHuZD9t9Ok7qpRHDChVnfVkEvJeD9uKTIE3Yqycp31bNke9bS6juL7TzCw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
