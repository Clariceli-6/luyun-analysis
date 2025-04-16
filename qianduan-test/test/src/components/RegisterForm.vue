<template>
  <div id="vanta-bg" class="register-container">
    <div class="register">
      <h2>注册</h2>

      <!-- 注册方式选择 -->
      <div class="register-method">
        <div
          class="circle-button"
          :class="{ active: registerMethod === 'email' }"
          @click="setRegisterMethod('email')"
        ></div>
        <span>邮箱注册</span>
        <div
          class="circle-button"
          :class="{ active: registerMethod === 'phone' }"
          @click="setRegisterMethod('phone')"
        ></div>
        <span>手机号注册</span>
      </div>

      <!-- 注册表单 -->
      <form @submit.prevent="register">
        <div>
          <label for="username">设置用户名</label>
          <input type="text" v-model="username" id="username" required placeholder="请输入用户名" />
        </div>

        <div v-if="registerMethod === 'phone'">
          <label for="phone_number">输入手机号</label>
          <input type="text" v-model="phone_number" id="phone_number" required placeholder="请输入手机号" />
        </div>

        <div v-if="registerMethod === 'email'">
          <label for="email">输入邮箱</label>
          <input type="email" v-model="email" id="email" required placeholder="请输入邮箱" />
        </div>

        <div>
          <label for="password">设置6-18位登录密码</label>
          <input type="password" v-model="password" id="password" required placeholder="请输入密码" />
        </div>

        <div>
          <label for="password_verify">再次输入登录密码</label>
          <input type="password" v-model="password_verify" id="password_verify" required placeholder="请再次输入密码" />
        </div>

        <div>
          <label for="agreement" class="agreement">
            <input type="checkbox" id="agreement" v-model="agreed" />
            我已阅读并同意《用户服务协议》
          </label>
        </div>

        <button type="submit" :disabled="isLoading || !agreed" class="submit-btn">注册</button>
      </form>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p>已有账户？<router-link to="/login">登录</router-link></p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

// 引入 VANTA 和 three.js
/* global VANTA */
export default {
  data() {
    return {
      username: '',
      password: '',
      password_verify: '',
      email: '',
      phone_number: '',
      errorMessage: '',
      isLoading: false,
      registerMethod: 'email',
      agreed: false,
      vantaEffect: null,
    };
  },
  mounted() {
    // 使用 VANTA.GLOBE 特效替代 VANTA.NET
    this.vantaEffect = VANTA.GLOBE({
      el: '#vanta-bg',
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: 200.0,
      minWidth: 200.0,
      scale: 1.0,
      scaleMobile: 1.0,
      color: 0x4f69c5, // 特效颜色
      backgroundColor: 0x0a1d33, // 背景颜色
    });
  },
  beforeUnmount() {
    // 销毁特效以释放资源
    if (this.vantaEffect) this.vantaEffect.destroy();
  },
  methods: {
    setRegisterMethod(method) {
      this.registerMethod = method;
    },
    async register() {
      this.isLoading = true;

      if (this.password.length < 6 || this.password.length > 18) {
        this.errorMessage = '密码长度必须在6到18位之间';
        this.isLoading = false;
        return;
      }

      if (this.password !== this.password_verify) {
        this.errorMessage = '两次输入的密码不一致';
        this.isLoading = false;
        return;
      }

      if(this.registerMethod === 'phone')
      {
        const response = await axios.get('/center/existPhoneNumber', {
          params:{
            phoneNumber: this.phone_number,
          }
        });

        if(response.data.data){
          alert("手机号已存在，请更换手机号");
          this.isLoading = false;
          return;
        }
      }
      else
      {
        const response = await axios.get('/center/existEmail', {
          params:{
            email: this.email,
          }
        });

        if(response.data.data){
          alert("邮箱已存在，请更换邮箱");
          this.isLoading = false;
          return;
        }        
      }

      
      const registerUrl =
        this.registerMethod === 'phone'
          ? 'http://localhost:8080/register/phone'
          : 'http://localhost:8080/register/email';

      const requestData =
        this.registerMethod === 'phone'
          ? {
              username: this.username,
              phone_number: this.phone_number,
              password: this.password,
              password_verify: this.password_verify,
            }
          : {
              username: this.username,
              email: this.email,
              password: this.password,
              password_verify: this.password_verify,
            };

      fetch(registerUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData),
      })
        .then(() => {
          alert('注册成功！');
          this.$router.push('/login');
        })
        .catch(() => {
          this.errorMessage = '注册失败，请重试';
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
  },
};
</script>

<style scoped>
html,
body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100vh;
}

.register-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.register {
  width: 450px;
  padding: 40px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.3);  /* 透明度 0.3 */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  color: white;
  z-index: 1;
  box-sizing: border-box; /* 确保边框和内边距被包含在宽度内 */
}

.register h2 {
  text-align: center;
  font-size: 28px;
  margin-bottom: 20px;
}

.register input {
  width: 100%;  /* 输入框宽度占满容器 */
  padding: 12px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 15px;
  background-color: rgba(255, 255, 255, 0.3);
  color: white;
  box-sizing: border-box; /* 确保输入框的宽度计算包括边框 */
}

.register-method {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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

.register-method span {
  color: white;
  font-size: 18px;
}

.register button {
  width: 100%;  /* 确保按钮宽度占满容器 */
  padding: 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 18px;
  cursor: pointer;
  transition: opacity 0.3s;
  box-sizing: border-box; /* 确保按钮宽度计算包括边框 */
}

.register button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error-message {
  color: red;
  font-size: 14px;
  text-align: center;
  margin-top: 10px;
}
</style>
