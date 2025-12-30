<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>在线秒杀系统 - 注册</h2>
        </div>
      </template>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username" class="form-item">
          <el-input v-model="registerForm.username" size="large" />
        </el-form-item>
        <el-form-item label="密码" prop="password" class="form-item">
          <el-input v-model="registerForm.password" type="password" show-password size="large" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname" class="form-item">
          <el-input v-model="registerForm.nickname" size="large" />
        </el-form-item>
        <el-form-item class="form-item">
          <el-button type="primary" :loading="loading" @click="handleRegister" style="width: 100%" size="large">注册</el-button>
        </el-form-item>
        <div class="login-link">
          <router-link to="/login">已有账号？去登录</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  nickname: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = await register(registerForm)
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify(data))
        ElMessage.success('注册成功')
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
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.register-card {
  width: 500px;
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

.login-link {
  text-align: center;
  font-size: 14px;
  margin-top: 20px;
}

.login-link a {
  text-decoration: none;
  color: #409eff;
}
</style>
