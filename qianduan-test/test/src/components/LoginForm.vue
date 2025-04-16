<template>
  <div class="login-container">
    <div class="login">
      <h2>登录</h2>

      <!-- 登录方式选择 -->
      <div class="login-method">
        <div 
          class="circle-button" 
          :class="{ active: loginMethod === 'email' }" 
          @click="setLoginMethod('email')"
        ></div>
        <span>邮箱登录</span>
        <div 
          class="circle-button" 
          :class="{ active: loginMethod === 'phone' }" 
          @click="setLoginMethod('phone')"
        ></div>
        <span>手机号登录</span>
      </div>

      <!-- 登录表单 -->
      <form @submit.prevent="handleLogin">
        <div v-if="loginMethod === 'phone'">
          <label for="phone">手机号</label>
          <input 
            type="text" 
            v-model="account" 
            id="phone" 
            required
            placeholder="请输入11位手机号"
            maxlength="11"
          />
        </div>

        <div v-else>
          <label for="email">邮箱</label>
          <input 
            type="email" 
            v-model="account" 
            id="email" 
            required
            placeholder="请输入邮箱地址"
          />
        </div>

        <div>
          <label for="password">密码</label>
          <input 
            type="password" 
            v-model="password" 
            id="password" 
            required
            placeholder="请输入6-18位密码"
            maxlength="18"
          />
        </div>

        <button 
          type="submit" 
          :disabled="isLoading" 
          class="submit-btn"
        >
          {{ isLoading ? '登录中...' : '立即登录' }}
        </button>
      </form>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p>没有账户？<router-link to="/register">立即注册</router-link></p>
    </div>
  </div>
</template>

<script>
/* global VANTA */
export default {
  data() {
    return {
      account: '',
      password: '',
      errorMessage: '',
      isLoading: false,
      loginMethod: 'email', // 默认邮箱登录
      vantaEffect: null, // 用于存储特效实例
    };
  },
  mounted() {
    // 初始化 VANTA.GLOBE 特效
    this.vantaEffect = VANTA.GLOBE({
      el: '.login-container',  // 设置特效渲染区域
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: 200.0,
      minWidth: 200.0,
      scale: 1.0,
      scaleMobile: 1.0,
      color: 0x4f69c5,
      backgroundColor: 0xa1d33,
    });
  },
  beforeUnmount() {
    // 在组件销毁时销毁特效
    if (this.vantaEffect) {
      this.vantaEffect.destroy();
    }
  },
  methods: {
    setLoginMethod(method) {
      this.loginMethod = method;
      this.account = ''; // 切换方式时清空账号
    },
    
    validateInputs() {
      // 手机号格式验证
      if (this.loginMethod === 'phone' && !/^1[3-9]\d{9}$/.test(this.account)) {
        this.errorMessage = '请输入有效的手机号码';
        return false;
      }
      
      // 邮箱格式验证
      if (this.loginMethod === 'email' && !/^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(this.account)) {
        this.errorMessage = '请输入有效的邮箱地址';
        return false;
      }

      // 密码长度验证
      if (this.password.length < 6 || this.password.length > 18) {
        this.errorMessage = '密码长度需为6-18位';
        return false;
      }

      return true;
    },

    async handleLogin() {
      if (!this.validateInputs()) {
        this.isLoading = false;
        return;
      }

      this.isLoading = true;
      this.errorMessage = '';

      try {
        const url = this.loginMethod === 'phone' 
          ? 'http://localhost:8081/login/phone' 
          : 'http://localhost:8081/login/email';

        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            // 注意：这里的字段名需要与后端接口一致，即需要和apifox上写的字段名一样
            [this.loginMethod === 'phone' ? 'phone_number' : 'email']: this.account,
            password: this.password
          })
        });

        const data = await response.json();

        if (response.ok) {
        if(data.status)
        {
          // 登录成功处理
          localStorage.setItem('token', data.data.token);
          this.$router.push('/visual');
        }
        else
        {
          this.errorMessage = data.message || '登录失败';
        }
        } else {
          this.errorMessage = data.message || '登录失败';
        }
      } catch (error) {
        this.errorMessage = '网络请求异常，请稍后重试';
      } finally {
        this.isLoading = false;
      }
    }
  }
};
</script>

<style scoped>
/* 确保容器宽度和计算方式一致 */
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background: url('@/image/space_background.webp') no-repeat center center;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login {
  width: 450px;
  padding: 40px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  color: white;
  box-sizing: border-box; /* 确保包括边框和内边距 */
}

.login h2 {
  text-align: center;
  font-size: 28px;
  margin-bottom: 20px;
}

.login input {
  width: 100%; /* 输入框宽度占满容器 */
  padding: 12px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 15px;
  background-color: rgba(255, 255, 255, 0.3);
  color: white;
  box-sizing: border-box; /* 确保包括边框和内边距 */
}

.submit-btn {
  width: 100%; /* 确保按钮宽度占满容器 */
  padding: 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: opacity 0.3s;
  box-sizing: border-box; /* 确保包括边框和内边距 */
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-method {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.error-message {
  color: #ff4757;
  text-align: center;
  margin: 10px 0;
}

.circle-button {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: 2px solid #007bff;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.circle-button.active {
  background-color: #007bff;
}
</style>
