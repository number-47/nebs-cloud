<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <div class="title-container">
        <h3 class="title">{{ $t('login.title') }}</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          :placeholder="$t('login.username')"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          :placeholder="$t('login.password')"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <el-form-item prop="code" class="code-input">
        <el-input
          ref="code"
          v-model="loginForm.code"
          prefix-icon="el-icon-lock"
          :placeholder="$t('login.code')"
          name="code"
          type="text"
          autocomplete="off"
          style="width: 50%"
          @keyup.enter.native="handleLogin"
        />
      </el-form-item>
      <img :src="imageCode" alt="codeImage" class="code-image" @click="getCodeImage" />

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">{{
        $t('login.logIn')
      }}</el-button>

      <div class="tips">
        <span style="margin-right:20px;">username: admin</span>
        <span>password: any</span>
      </div>
    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
import db from '@/utils/localstorage'
import { randomNum } from '@/utils'
import axios from 'axios'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error(this.$t('tips.usernameShouldNotBeEmpty')))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error(this.$t('tips.passwordShouldNotBeEmpty')))
      } else {
        callback()
      }
    }
    return {
      codeUrl: `${process.env.VUE_APP_BASE_API}auth/captcha`,
      loginForm: {
        username: 'mrbird',
        password: '1234qwer'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        code: {
          required: true,
          message: this.$t('rules.require'),
          trigger: 'blur'
        }
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      randomId: randomNum(24, 16),
      imageCode: '0000'
    }
  },
  mounted() {
    db.clear()
    this.getCodeImage()
  },
  methods: {
    getCodeImage() {
      axios({
        method: 'GET',
        url: `${this.codeUrl}?key=${this.randomId}`,
        responseType: 'arraybuffer'
      })
        .then(res => {
          return 'data:image/png;base64,' + btoa(new Uint8Array(res.data).reduce((data, byte) => data + String.fromCharCode(byte), ''))
        })
        .then(res => {
          this.imageCode = res
        })
        .catch(e => {
          if (e.toString().indexOf('429') !== -1) {
            this.$message({
              message: this.$t('tips.tooManyRequest'),
              type: 'error'
            })
          } else {
            this.$message({
              message: this.$t('tips.getCodeImageFailed'),
              type: 'error'
            })
          }
        })
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          const that = this
          this.$login('auth/oauth/token', {
            ...that.loginForm,
            key: this.randomId
          })
            .then(r => {
              const data = r.data
              this.saveLoginData(data)
              this.getUserDetailInfo()
              this.loginSuccessCallback(that.loginForm.username)
            })
            .catch(error => {
              console.error(error)
              that.loading = false
              that.getCodeImage()
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    saveLoginData(data) {
      this.$store.commit('account/setAccessToken', data.access_token)
      this.$store.commit('account/setRefreshToken', data.refresh_token)
      const current = new Date()
      const expireTime = current.setTime(current.getTime() + 1000 * data.expires_in)
      this.$store.commit('account/setExpireTime', expireTime)
    },
    getUserDetailInfo() {
      this.$get('auth/user')
        .then(r => {
          this.$store.commit('account/setUser', r.data.principal)
          this.$message({
            message: this.$t('tips.loginSuccess'),
            type: 'success'
          })
          this.loading = false
          this.$router.push('/')
        })
        .catch(error => {
          this.$message({
            message: this.$t('tips.loginFail'),
            type: 'error'
          })
          console.error(error)
          this.loading = false
        })
    },
    loginSuccessCallback(username) {
      this.$get(`system/user/success/${username}`).catch(e => {
        console.log(e)
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
  .code-input {
    width: 50%;
    display: inline-block;
    vertical-align: middle;
  }
  .code-image {
    display: inline-block;
    vertical-align: sub;
    cursor: pointer;
    margin-left: 10px;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
