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
        <!-- 查询模块 -->
        <div class="module" v-if="activeMenu === 'search'">
          <div class="search-filter-box">
            <div class="search-box">
              <h4>
                <button class="toggle-btn" @click="toggleDeviceSearch">
                  <span v-if="isDeviceSearchActive">✔️</span>
                </button>
                设备信息查询
              </h4>
              <div class="id-input">
                <input v-if="isDeviceSearchActive" type="text" placeholder="输入感知设备编号搜索" v-model="searchKeyword" @keyup.enter="searchData">
                <button v-if="isDeviceSearchActive" id="emm" @click="searchData" class="search-btn">搜索</button>
              </div>
            </div>
            <div class="search-box">
              <h4>
                <button class="toggle-btn" @click="toggleTitleSearch">
                  <span v-if="isTitleSearchActive">✔️</span>
                </button>
                检测对象信息查询
              </h4>
              <div v-if="isTitleSearchActive">
                <div class="time-inputs">
                  <div class="time-input">
                    <input type="datetime-local" id="startTime" v-model="startTime" placeholder="选择开始时间" step="1">
                  </div>
                  <div class="time-input">
                    <input type="datetime-local" id="endTime" v-model="endTime" placeholder="选择结束时间" step="1">
                  </div>
                  <button v-if="isTitleSearchActive" @click="searchByTitle" class="search-btn">搜索</button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 空白模块（初始状态下显示空白表格） -->
        <div class="module" v-if="activeMenu === 'search' && !isDeviceSearchActive && !isTitleSearchActive">
          <h4>空白表格，等待数据加载...</h4>
        </div>

        <!-- 查询结果模块：设备信息查询 -->
        <div id="up" class="module" v-if="activeMenu === 'search' && isDeviceSearchActive">
          <table class="data-table">
            <thead>
              <tr>
                <th>感知设备编号</th>
                <th>设备类别</th>
                <th>输出时间</th>
                <th>计算应用时间</th>
                <th>输出结果时间</th>
                <th>感知对象数量</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id">
                <td>{{ item.deviceId }}</td>
                <td>{{ item.deviceType }}</td>
                <td>{{ formatTimestamp(item.timestampOfDevOut) }}</td>
                <td>{{ formatTimestamp(item.timestampOfDetIn) }}</td>
                <td>{{ formatTimestamp(item.timestampOfDetOut) }}</td>
                <td>{{ item.objectiveNum }}</td>
              </tr>
            </tbody>
          </table>

          <div class="pagination">
            <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
            <span>第{{ currentPage }}页/共{{ computedTotalPages }}页</span>
            <button @click="nextPage" :disabled="currentPage === computedTotalPages">下一页</button>
          </div>
        </div>

        <!-- 查询结果模块：检测对象信息查询 -->
        <div class="module" v-if="activeMenu === 'search' && isTitleSearchActive">
          <table class="data-table">
            <thead>
              <tr>
                <th>设备编号</th>
                <th>类型</th>
                <th>经度</th>
                <th>纬度</th>
                <th>速度</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.deviceId">
                <td>{{ item.deviceId }}</td>
                <td>{{ item.type }}</td>
                <td>{{ convertLongitude(item.longitude) }}</td>
                <td>{{ convertLatitude(item.latitude) }}</td>
                <td>{{ item.speed }}</td>
              </tr>
            </tbody>
          </table>

          <div class="pagination">
            <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
            <span>第{{ currentPage }}页/共{{ computedTotalPages }}页</span>
            <button @click="nextPage" :disabled="currentPage >= computedTotalPages">下一页</button>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import * as XLSX from 'xlsx';
import axios from 'axios';

export default {
  data() {
    return {
      activeMenu: 'search',
      searchKeyword: '',
      searchTitle: '',
      currentPage: 1,
      pageSize: 6,
      allData: [],
      filteredData: [],
      total: 0,
      startTime: '',
      endTime: '',
      isDeviceSearchActive: true,  // 初始状态设备信息查询选中
      isTitleSearchActive: false,  // 初始状态检测对象信息查询不选中
    };
  },
  created(){
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
    paginatedData() {
      return this.filteredData || [];
    },
    
    // 修改后的计算属性
    computedTotalPages() { 
      console.log(this.total / this.pageSize);
      if (this.total / this.pageSize <= 1) return 1;
      return Math.ceil(this.total / this.pageSize);
    }
  },

  methods: {
    convertLongitude(rawLongitude) {
      return parseFloat((rawLongitude / 10000000 - 180).toFixed(7));
    },

    convertLatitude(rawLatitude) {
      return parseFloat((rawLatitude / 10000000 - 90).toFixed(7));
    },

    // 切换设备信息查询的按钮状态
    toggleDeviceSearch() {
      if (this.isDeviceSearchActive) {
        this.isDeviceSearchActive = false;
      } else {
        // 切换到设备查询时重置检测对象查询的数据
        this.isDeviceSearchActive = true;
        this.isTitleSearchActive = false;
        this.resetSearchData();  // 新增方法
      }
    },

    // 切换检测对象信息查询的按钮状态
      toggleTitleSearch() {
      if (this.isTitleSearchActive) {
        this.isTitleSearchActive = false;
      } else {
        // 切换到检测对象查询时重置设备查询的数据
        this.isTitleSearchActive = true;
        this.isDeviceSearchActive = false;
        this.resetSearchData();  // 新增方法
      }
    },

    resetSearchData() {
      this.filteredData = [];  // 清空当前数据
      this.total = 0;          // 重置总记录数
      this.currentPage = 1;    // 重置页码
      this.searchKeyword = ''; // 清空搜索关键词
      this.startTime = '';     // 清空开始时间
      this.endTime = '';       // 清空结束时间
    },
  

    // UTC时间转换
    formatTimestamp(timestamp) {
      const date = new Date(timestamp * 1000);
      const Y = date.getFullYear();
      const M = (date.getMonth() + 1).toString().padStart(2, '0');
      const D = date.getDate().toString().padStart(2, '0');
      const h = date.getHours().toString().padStart(2, '0');
      const m = date.getMinutes().toString().padStart(2, '0');

      return `${Y}-${M}-${D} ${h}:${m}`; 
    },

    async searchData() {
    const token = localStorage.getItem('token');
    
    try {
      this.loading = true;
      const response = await axios.post('/query/deviceId', {
        device_id: this.searchKeyword,
        page: this.currentPage,
        page_size: this.pageSize
      },
      {
        headers: {
              'token': token // 这里字段名要和后端 JwtProperties 配的匹配
            }
      });

      const result = response.data;

      if (result.status === 1) {
        const pageResult = result.data;
        this.filteredData = pageResult.records.map(item => ({
          deviceId: item.device_id,
          deviceType: item.device_type,
          timestampOfDevOut: item.timestamp_of_dev_out,
          timestampOfDetIn: item.timestamp_of_det_in,
          timestampOfDetOut: item.timestamp_of_det_out,
          objectiveNum: item.objective_num
        }));
        this.total = result.data.total || 0;
      } else {
        console.error('查询失败:', result.msg);
      }
    } catch (error) {
      console.error('搜索失败:', error);
    } finally{
      this.loading = false;
    }
  },

    async searchByTitle() {
    const token = localStorage.getItem('token');

    try {
      this.loading = true;
      const response = await axios.post('/query/time', {
        start_time: new Date(this.startTime).getTime() / 1000,
        end_time: new Date(this.endTime).getTime() / 1000,
        page: this.currentPage,
        page_size: this.pageSize
      },
      {
        headers: {
          'token': token // 这里字段名要和后端 JwtProperties 配的匹配
        }
      });

      const result = response.data;

      if (result.status === 1) {
        // 成功响应
        this.filteredData = result.data.records.map(item => ({
          deviceId: item.device_id,  // 注意这里应该是 device_id，你之前写的是 devce_id
          type: this.getTypeName(item.type), // 将类型代码转换为可读名称
          longitude: item.longitude,
          latitude: item.latitude,
          speed: item.speed,
        }));
        this.total = result.data.total;
      } else {
        // 失败响应
        console.error('查询失败:', result.msg);
        this.filteredData = [];
        this.total = 0;
        // 可以在这里添加用户提示，比如使用Element UI的Message
        this.$message.error(result.msg || '查询失败');
      }
    } catch (error) {
      console.error('搜索失败:', error);
      this.filteredData = [];
      this.total = 0;
      this.$message.error('请求失败，请检查网络连接');
    } finally {
      this.loading = false;
    }
  },

  // 添加一个方法来转换类型代码为可读名称
  getTypeName(typeCode) {
    const typeMap = {
      0: '未知',
      1: '汽车',
      2: '行人',
      3: '自行车',
      // 根据实际情况添加更多类型
    };
    return typeMap[typeCode] || '未知类型';
  },
    async initPage(){
      this.currentPage = 1;  // 只有初始化时才重置页码
      await this.loadPageData();
    },

    async prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        await this.loadPageData();
      }
    },

    async nextPage() {
      if (this.currentPage < this.computedTotalPages) {
        this.currentPage++;
        console.log(`当前页: ${this.currentPage}, 总页数: ${this.computedTotalPages}`);
        await this.loadPageData()
      } 
    },

      async loadPageData() {
      if (this.isDeviceSearchActive) {
        await this.searchData()
      } else if (this.isTitleSearchActive) {
        await this.searchByTitle()
      }
    },

    exportExcel() {
      const ws = XLSX.utils.json_to_sheet(this.filteredData);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "数据表");
      XLSX.writeFile(wb, "export_data.xlsx");
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

.module {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid #2d5a9d;
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  height:fit-content;
}

.search-filter-box {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 10px;
}

.search-box {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.search-box h4 {
  margin-bottom: 5px;
  font-size: 16px;
  color: #00f7ff;
  
}

#emm {
  margin-left:10px;
  margin-top: 36px;
}

input[type="text"] {
  flex: 1;
  padding: 8px 12px;
  background: rgba(255,255,255,0.1);
  border: 1px solid #2d5a9d;
  color: #fff;
  border-radius: 4px;
  width:400px;
  height: 36px;
}

button {
  padding: 8px 16px;
  background: #2d5a9d;
  border: none;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.3s;
}

button:hover {
  background: #3a6db0;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.data-table th,
.data-table td {
    padding: 10px;
  text-align: left;
  border-bottom:1px solid #2d5a9d;
}

.data-table th {
  background-color: rgba(0, 102, 255, 0.1);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  padding: 6px 12px;
  background: #2d5a9d;
  border: none;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
  margin: 0 5px;
}

.pagination button:disabled {
  background: #555;
  cursor: not-allowed;
}

.pagination span {
  color: #fff;
  align-self: center;
}

.id-input {
  margin-top: -30px;

}

.search-btn {
  padding: 8px 16px;
  margin-top: 1px;
  background: #2d5a9d;
  border: none;
  color: #fff;
  cursor: pointer;
  transition: 0.3s;
}


/* 时间筛选部分的样式 */
.time-inputs {
  display: flex;
  gap: 20px;
  margin-top: 1px;
}

.time-input {
  display: flex;
  flex-direction: column;
  width: 75px;
  gap: 10px;
  flex: 1;
}

.time-input label {
  font-size: 14px;
  color: #00f7ff;
  font-weight: bold;
}

.time-input input[type="datetime-local"] {
  padding: 10px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid #2d5a9d;
  color: #fff;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.time-input input[type="datetime-local"]:focus {
  border-color: #00f7ff;
  outline: none;
}

button.search-btn {
  padding: 10px 18px;
  background: #2d5a9d;
  border: none;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
}

button.search-btn:hover {
  background: #3a6db0;
}

.export-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  padding: 12px 20px;
  background: #2d5a9d;
  border: none;
  color: #fff;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.export-btn:hover {
  background: #3a6db0;
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