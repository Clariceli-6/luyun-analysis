<template>
  <div class="app-container">
    <!-- 顶部标题栏 -->
    <header class="header-container">
      <div class="header">
        <h1>路云数据解析系统</h1>
      </div>
    </header>

    <!-- 主内容区 -->
    <div class="main-content">

      <!-- 左侧菜单栏 -->
      <aside class="sidebar">
        <ul class="sidebar-menu">
          <router-link to="/visual" custom v-slot="{ navigate, href, isActive }">
            <li :class="{ active: isActive }" :href="href" @click="navigate">
              <i class="icon">📊</i>数据监控
            </li>
          </router-link>

          <router-link to="/query" custom v-slot="{ navigate, href, isActive }">
            <li :class="{ active: isActive }" :href="href" @click="navigate">
              <i class="icon">🔍</i>数据查询
            </li>
          </router-link>

          <router-link
              v-if="role !== 0"
              to="/export"
              custom
              v-slot="{ navigate, href, isActive }"
            >
              <li :class="{ active: isActive }" :href="href" @click="navigate">
                <i class="icon">📤</i>数据导出
              </li>
            </router-link>

          <router-link to="/center" custom v-slot="{ navigate, href, isActive }">
            <li :class="{ active: isActive }" :href="href" @click="navigate">
              <i class="icon">👤</i>用户中心
            </li>
          </router-link>
        </ul>
      </aside>

    <!-- 内容区域 -->
    <main class="content">

      <!-- 用户中心模块 -->
      <div class="module user-center" v-if="activeMenu === 'center'">

        <div class="user-profile">
          <h3 style="text-align: center; color: #00f7ff; margin-bottom: 30px;">用户基本信息</h3>
          <div class="user-info">
            
            <div class="info-item">
             <label>用户名：</label> 
              <span>{{ userInfo.username }}</span>
            </div>
            <div class="info-item">
              <label>角色：</label>
              <span>
                {{
                  this.role === 0 ? "普通用户" : 
                  this.role === 1 ? "管理员" : 
                  this.role === 2 ? "超级管理员" : "未知角色"
                }}
              </span>
            </div>
            <div class="info-item">
              <label>电话：</label>
              <span>{{ userInfo.phone_number || "未设置" }}</span>
            </div>
            <div class="info-item">
              <label>邮箱：</label>
              <span>{{ userInfo.email || "未设置" }}</span>
            </div>
          </div>
        </div>

        <div class="user-actions">
          <button @click="showEditDialog = true" class="action-btn edit-btn">
            编辑信息
          </button>
          <button
            @click="showPasswordDialog = true"
            class="action-btn change-pwd-btn"
          >
            修改密码
          </button>
          <button @click="logout" class="action-btn logout-btn">
            退出登录
          </button>
        </div>

<!-- 编辑信息对话框 -->
        <div v-if="showEditDialog" class="dialog-overlay">
          <div class="dialog-content">
            <h3>编辑用户信息</h3>
            <div class="form-group">
              <label>用户名：</label>
              <input v-model="editForm.username" type="text" />
            </div>
            <div class="form-group">
              <label>电话：</label>
              <input v-model="editForm.phone_number" type="tel" />
            </div>
            <div class="form-group">
              <label>邮箱：</label>
              <input v-model="editForm.email" type="email" />
            </div>
            <div class="dialog-actions">
              <button @click="saveUserInfo" class="confirm-btn">保存</button>
              <button @click="showEditDialog = false" class="cancel-btn">
                取消
              </button>
            </div>
          </div>
        </div>

        <!-- 修改密码对话框 -->
        <div v-if="showPasswordDialog" class="dialog-overlay">
          <div class="dialog-content">
            <h3>修改密码</h3>
            <div class="form-group">
              <label>旧密码：</label>
              <input v-model="passwordForm.oldPassword" type="password" />
            </div>
            <div class="form-group">
              <label>新密码：</label>
              <input v-model="passwordForm.newPassword" type="password" />
            </div>
            <div class="form-group">
              <label>确认密码：</label>
              <input v-model="passwordForm.confirmPassword" type="password" />
            </div>
            <div class="dialog-actions">
              <button @click="changePassword" class="confirm-btn">
                确认
              </button>
              <button @click="showPasswordDialog = false" class="cancel-btn">
                取消
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 如果是超级管理员（role == 2）显示用户创建界面 -->
<div v-if="role == 2" class="module create-user">
<div class="user-profile">
  <h2 style="text-align: center; color: #00f7ff; margin-bottom: 30px;">创建新用户 / 管理员</h2>
  <div class="user-info">
    <div class="info-item">
      <label>用户名：</label>
      <input v-model="newUserForm.username" type="text" />
    </div>
    <div class="info-item">
      <label>密码：</label>
      <input v-model="newUserForm.password" type="password" />
    </div>
    <div class="info-item">
      <label>电话：</label>
      <input v-model="newUserForm.phone_number" type="tel" />
    </div>
    <div class="info-item">
      <label>邮箱：</label>
      <input v-model="newUserForm.email" type="email" />
    </div>
    <div class="info-item">
      <label>角色：</label>
      <select v-model="newUserForm.role">
        <option value="0">普通用户</option>
        <option value="1">管理员</option>
      </select>
    </div>
  </div>
  <div class="dialog-actions" style="justify-content: center; margin-top: 30px;">
    <button @click="createUser" class="confirm-btn">创建账号</button>
  </div>
</div>
</div>

    </main>
  </div>

</div>
</template>

<script>
import axios from "axios";
import MD5 from 'crypto-js/md5';

export default {
data() {
  return {
    activeMenu: "center",
    userInfo: {
      username: '',
      role: '',
      phone_number: '',
      email: '',
      avatar: '',
      createTime: ''
    },
    newUserForm: {
      username: '',
      password: '',
      phone_number: '',
      email: '',
      role: '0',  
    },
    showEditDialog: false,
    showPasswordDialog: false,
    editForm: {
      username: '',
      phone_number: '',
      email: ''
    },
    passwordForm: {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  };
},
 created() {
  this.fetchUserInfo();
  const token = localStorage.getItem('token');
  if (token) {
    try {
      const token = localStorage.getItem('token');
      const payloadBase64 = token.split('.')[1]; // JWT 是三段结构：header.payload.signature
      const payload = JSON.parse(atob(payloadBase64));
      const role = payload.role; // 或 payload[JwtClaimsConstant.USERNAME]

      console.log("角色：", role);
      this.role = role; // 将角色存储在组件的 data 中
      
    } catch (e) {
      console.error('token 解析失败', e);
    }
}
},
computed: {

},
methods: {
  logout() {
    localStorage.removeItem('token'); // 清除 token
    this.$router.push('/login');      // 跳转到登录页（确保路由中定义了这个路径）
  },

  async createUser() {
    const token = localStorage.getItem('token');

    try {
        await axios.post('/center/createUser', {
        username: this.newUserForm.username,
        password: this.newUserForm.password,
        phone_number: this.newUserForm.phone_number,
        email: this.newUserForm.email,
        role: this.newUserForm.role,
      },
      {
        headers: {
              'token': token // 这里字段名要和后端 JwtProperties 配的匹配
            }
      });

      alert('用户创建成功');
      this.newUserForm = {
        username: '',
        password: '',
        phone_number: '',
        email: '',
        role: '1'
      };
    } catch (error) {
      console.error('创建用户失败:', error);
      alert(error.response?.data?.message || '创建用户失败');
    }
  },

  async saveUserInfo() {
    const token = localStorage.getItem('token');
    const payloadBase64 = token.split('.')[1]; // JWT 是三段结构：header.payload.signature
    const payload = JSON.parse(atob(payloadBase64));
    const userId = payload.userId; // 或 payload[JwtClaimsConstant.USERNAME]

    if(this.editForm.phone_number != "" && this.editForm.phone_number != null)
    {
      const response = await axios.get('/center/existPhoneNumber', {
          params:{
            phoneNumber: this.editForm.phone_number,
          }
        })

      if(response.data.data){
        alert("手机号已存在，请更换手机号");
        return;
      }
    }

    if(this.editForm.email != "" && this.editForm.email != null)
    {
      const response2 = await axios.get('/center/existEmail', {
        params:{
          email: this.editForm.email,
        }
      })

    if(response2.data.data){
      alert("邮箱已存在，请更换邮箱");
      return;
    }
    }

    try {
      await axios.post('/center/updateInfo', {
        user_id: userId,
        username: this.editForm.username,
        phone_number: this.editForm.phone_number,
        email: this.editForm.email,
      }, {
        headers: {
          'token': token,
        }
      });

      this.showEditDialog = false;
      alert('用户信息修改成功');
    } catch (error) {
      console.error('修改用户信息失败:', error);
      alert(error.response?.data?.message || '修改用户信息失败');
    }
  },

  // UTC时间转换
  formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    const options = {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      fractionalSecondDigits: 3,
      timeZone: "Asia/Shanghai",
    };
    return date.toLocaleString("zh-CN", options);
  },


  // 获取用户信息
  async fetchUserInfo() {
    try {
      const token = localStorage.getItem('token');
      const payloadBase64 = token.split('.')[1]; // JWT 是三段结构：header.payload.signature
      const payload = JSON.parse(atob(payloadBase64));
      const userId = payload.userId; // 或 payload[JwtClaimsConstant.USERNAME]
      console.log('USER_ID: ', userId);
      
      const response = await axios.get('/center/get', {
        headers: {
          'token': token,
        },
        params:{
          userId: userId,
        }
      });

      this.userInfo = response.data.data;
      // 初始化编辑表单
      this.editForm = {
        username: response.data.username,
        password: response.data.password,
        phone_number: response.data.phone_number,
        email: response.data.email,
        role: response.data.role,

      };
    } catch (error) {
      console.error('获取用户信息失败:', error);
    }
  },

  // 修改密码
  async changePassword() {
    const token = localStorage.getItem('token');
    const payloadBase64 = token.split('.')[1]; // JWT 是三段结构：header.payload.signature
    const payload = JSON.parse(atob(payloadBase64));
    const userId = payload.userId; // 或 payload[JwtClaimsConstant.USERNAME]
      
      const response = await axios.get('/center/get', {
        headers: {
          'token': token,
        },
        params:{
          userId: userId,
        }
      });

      // 获取旧密码
      const old_password = response.data.data.password;
      console.log("test: " + old_password)

    if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
      console.error('新密码和确认密码不一致');
      alert('新密码和确认密码不一致');
      return;
    }

    const hashedInput = MD5(this.passwordForm.oldPassword).toString();
    if (hashedInput !== old_password) {
      console.error('旧密码错误');
      alert('旧密码错误');
      return;
    }
    
    try {
      await axios.post('/center/changePassword', {
        user_id: userId,
        password: this.passwordForm.newPassword,
      }, {
        headers: {
          'token': token,
        }
      });

      this.showPasswordDialog = false;
      this.passwordForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      };

      alert('密码修改成功');
    } catch (error) {
      console.error('修改密码失败:', error);
      alert(error.response?.data?.message || '修改密码失败');
    }
  },
},
};
</script>



<style scoped>
* {
margin: 0;
padding: 0;
box-sizing: border-box;
}

html,
body {
height: 100%;
margin: 0;
padding: 0;
font-family: 'Microsoft YaHei', sans-serif;
}

.app-container {
display: flex;
flex-direction: column;
height: 100vh;
background: #030e1d;
color: #fff;
}

.header-container {
height: 60px;
background: linear-gradient(90deg, #0a1020, #1a4a8d);
border-bottom: 1px solid #2d5a9d;
display: flex;
justify-content: center;
align-items: center;
}

.header h1 {
font-size: 24px;
letter-spacing: 4px;
text-shadow: 0 0 10px #3a6db0;
position: relative;
padding: 0 40px;
}

.header h1::before,
.header h1::after {
content: '';
position: absolute;
top: 50%;
width: 30px;
height: 30px;
background: url('data:image/svg+xml;utf8,<svg viewBox="0 0 24 24" fill="%233a6db0" xmlns="http://www.w3.org/2000/svg"><path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2z"/></svg>');
transform: translateY(-50%);
}

.header h1::before {
left: 0;
}

.header h1::after {
right: 0;
}

.main-content {
display: flex;
flex: 1;
overflow: hidden;
}

.sidebar {
width: 220px;
background: rgba(15, 20, 30, 0.8);
border-right: 1px solid #2d5a9d;
padding: 20px 0;
overflow-y: auto;
}

.sidebar-menu {
list-style: none;
}

.sidebar-menu li {
padding: 12px 20px;
cursor: pointer;
transition: all 0.3s ease;
display: flex;
align-items: center;
}

.sidebar-menu li:hover {
background: rgba(0, 102, 255, 0.1);
}

.sidebar-menu li.active {
background: rgba(0, 102, 255, 0.2);
border-left: 3px solid #00f7ff;
}

.sidebar-menu li .icon {
margin-right: 10px;
color: #00f7ff;
font-size: 18px;
}


.content {
flex: 1;
padding: 20px;
overflow: auto;
}
/*.module {
background: rgba(255, 255, 255, 0.05);
padding: 30px;
border-radius: 10px;
box-shadow: 0 0 10px rgba(0, 247, 255, 0.1);
max-width: 800px;
margin: auto;
}*/
.module {
background: rgba(255, 255, 255, 0.05);
border: 1px solid #2d5a9d;
border-radius: 10px;
padding: 30px;
box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
margin-bottom: 20px;
max-width: 800px;
height:fit-content;
margin: auto;
}
/* 用户信息展示 */
.user-profile {
display: flex;
flex-direction: column;
align-items: center;
}

.user-info {
width: 100%;
}

.info-item {
margin-bottom: 15px;
display: flex;
justify-content: space-between;
border-bottom: 1px solid #2d5a9d;
padding-bottom: 5px;
}

.info-item label {
color: #00f7ff;
font-weight: bold;
}

.info-item input,
.info-item select {
background: transparent;
border: 1px solid #2d5a9d;
color: #fff;
padding: 6px 10px;
border-radius: 5px;
width: 60%;
}

/* 普通按钮样式（非渐变） */
.action-btn,
.confirm-btn,
.cancel-btn {
background: #174c85;
border: none;
color: #fff;
padding: 10px 20px;
margin: 5px;
border-radius: 8px;
font-weight: bold;
cursor: pointer;
transition: 0.3s;
}

.action-btn:hover,
.confirm-btn:hover,
.cancel-btn:hover {
background: #0056b3;
}

/* 弹窗样式 */
.dialog-overlay {
position: fixed;
top: 0;
left: 0;
width: 100%;
height: 100%;
background: rgba(0, 0, 0, 0.7);
display: flex;
justify-content: center;
align-items: center;
z-index: 999;
}

.dialog-content {
background: #0f1a2b;
padding: 30px;
border-radius: 10px;
width: 400px;
box-shadow: 0 0 15px rgba(0, 247, 255, 0.3);
}

.dialog-content h3 {
text-align: center;
color: #00f7ff;
margin-bottom: 20px;
}

.form-group {
margin-bottom: 15px;
display: flex;
flex-direction: column;
}

.form-group label {
color: #00f7ff;
font-size: 14px;
margin-bottom: 5px;
}

.form-group input {
padding: 8px;
border: 1px solid #2d5a9d;
background-color: transparent;
color: #fff;
border-radius: 5px;
}

/* 创建用户模块 */
.create-user .info-item {
margin-bottom: 20px;
display: flex;
justify-content: space-between;
align-items: center;
}

.create-user input,
.create-user select {
width: 60%;
padding: 8px;
border: 1px solid #2d5a9d;
background-color: #1a1a1a;
color: #fff;
border-radius: 6px;
}

.create-user select option {
background-color: #1a1a1a;
color: #fff;
}

.create-user h2 {
font-size: 20px;
font-weight: bold;
color: #00f7ff;
margin-bottom: 25px;
text-align: center;
}

.create-user .dialog-actions {
display: flex;
  justify-content: center;}

.sidebar-menu li {
color: #fff;
text-decoration: none;
padding: 8px 16px;
cursor: pointer;
}

.sidebar-menu li {
color: #fff;
text-decoration: none;
padding: 8px 16px;
cursor: pointer;
}


.sidebar-menu li.active {
background-color: #444;
font-weight: bold;
border-left: 4px solid #42b983;
}


</style>
