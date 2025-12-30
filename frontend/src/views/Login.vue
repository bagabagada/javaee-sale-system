<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>在线秒杀系统 - 登录</h2>
        </div>
      </template>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="0px">
        <el-form-item prop="username" class="form-item">
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" size="large"/>
        </el-form-item>
        <el-form-item prop="password" class="form-item">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password size="large"/>
        </el-form-item>
        <el-form-item class="form-item">
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width: 100%" size="large">登录</el-button>
        </el-form-item>
        <div class="register-link">
          <router-link to="/register">还没有账号？去注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/user'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = await login(loginForm)
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify(data))
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error) {
        // Error handled in interceptor
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 440px;
  padding: 20px;
}

.card-header {
  text-align: center;
  margin-bottom: 10px;
}

.card-header h2 {
    color: #409EFF;
}

.form-item {
    margin-bottom: 25px;
}

.register-link {
  text-align: center;
  font-size: 14px;
  margin-top: 15px;
}

.register-link a {
  text-decoration: none;
  color: #409eff;
}
</style>
