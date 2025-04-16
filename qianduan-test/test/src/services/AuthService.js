import axios from 'axios';

const API_URL = 'http://localhost:8081/'; // 修改端口为8081

const setAuthToken = (token) => {
  if (token) {
    localStorage.setItem('authToken', token);
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    localStorage.removeItem('authToken');
    delete axios.defaults.headers.common['Authorization'];
  }
};

const AuthService = {
  // 邮箱注册
  registerWithEmail(username, email, password, password_verify) {
    return axios.post(API_URL + 'register/email', { 
      username, 
      email, 
      password, 
      password_verify 
    });
  },

  // 手机号注册
  registerWithPhone(username, phone_number, password, password_verify) {
    return axios.post(API_URL + 'register/phone', { 
      username,
      phone_number,
      password,
      password_verify
    });
  },

  // 新增邮箱登录
  loginWithEmail(email, password) {
    return axios.post(API_URL + 'login/email', {
      email,
      password
    }).then(response => {
      if (response.data.token) {
        setAuthToken(response.data.token);
      }
      return response.data;
    });
  },

  // 新增手机号登录
  loginWithPhone(phone_number, password) {
    return axios.post(API_URL + 'login/phone', {
      phone_number,
      password
    }).then(response => {
      if (response.data.token) {
        setAuthToken(response.data.token);
      }
      return response.data;
    });
  },

  // 移除旧的通用登录方法
  // 保留其他方法...
  logout() {
    setAuthToken(null);
  },

  verifyToken() {
    return axios.get(API_URL + 'verify-token')
      .then(response => response.data)
      .catch(() => {
        setAuthToken(null);
      });
  },

  deleteUser() {
    return axios.delete(API_URL + 'delete-user');
  }
};

export default AuthService;