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
      <main class="content" v-if="activeMenu === 'monitor'">
        <!-- 上排模块 -->
        <div class="top-row">
          <!-- 地图模块 -->
          <div class="module">
            <div id="mapContainer" style="width: 100%; height: 100%; min-height: 300px;"></div>
          </div>

          <!-- 右侧模块（展示滚动数据） -->
          <div class="module">
            <div class="table-container">
              <table class="data-table">
                <thead>
                  <tr>
                    <th style="width: 170px;">设备编号</th>
                    <th>设备类别</th>
                    <th>输出时间</th>
                    <th>输出结果</th>
                    <th>感知对象数量</th>
                    <th>异常状态</th>
                  </tr>
                </thead>
                <tbody
                  ref="scrollBody"
                  @mouseenter="pauseScroll"
                  @mouseleave="resumeScroll"
                >
                  <tr
                    v-for="(device, index) in devices"
                    :key="index"
                    @mouseenter="hoverIndex = index"
                    @mouseleave="hoverIndex = null"
                    :class="{ 'hovered-row': hoverIndex === index }"
                  >
                    <td>{{ device.deviceId }}</td>
                    <td>{{ device.deviceType }}</td>
                    <td>{{ device.timestampOfDevOut }}</td>
                    <td>{{ device.timestampOfDetOut }}</td>
                    <td>{{ device.objectiveNum }}</td>
                    <td>{{ device.error }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- 下排模块 -->
        <div class="bottom-row">
          <div class="module">
            <div id="chartContainer" style="width: 100%; height: 300px; color: white;"></div>
          </div>

          <div class="module" style="max-height: 400px; overflow-y: auto;">
            <table style="width: 100%; border-collapse: collapse; color: white; font-size: 14px;">
              <thead>
                <tr style="background-color: rgba(255, 255, 255, 0.1);">
                  <th style="padding: 8px; border-bottom: 1px solid #ccc;">rcu编号</th>
                  <th style="padding: 8px; border-bottom: 1px solid #ccc;">输出时间</th>
                  <th style="padding: 8px; border-bottom: 1px solid #ccc;">设备类型</th>
                  <th style="padding: 8px; border-bottom: 1px solid #ccc;">状态</th>
                </tr>
              </thead>
              <tbody>
                <tr 
                  v-for="(device, index) in devices.filter(device => device.error === '异常')" 
                  :key="index" 
                  :style="{ backgroundColor: index % 2 === 0 ? 'rgba(255,255,255,0.05)' : 'transparent' }"
                >
                  <td style="padding: 8px; border-bottom: 1px solid #444;">{{ device.rcuId }}</td>
                  <td style="padding: 8px; border-bottom: 1px solid #444;">{{ device.timestampOfDevOut }}</td>
                  <td style="padding: 8px; border-bottom: 1px solid #444;">{{ device.deviceType }}</td>
                  <td style="padding: 8px; border-bottom: 1px solid #444;">
                    <span :style="{ color: device.error === '异常' ? '#ff4d4f' : '#52c41a' }">
                      {{ device.error }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>


          <!-- 目标对象统计模块 -->
          <div class="module object-chart-module">
            <div class="module-title">目标对象数量</div>
            <div ref="objectChart" class="chart-container"></div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>

/*获取当前系统时间
data() {
  return {
    activeMenu: "monitor",
    isPaused: false,
    hoverIndex: null,
    devices: [],
    pollingInterval: null, // 轮询定时器引用
    currentTime: Math.floor(Date.now() / 1000), // 使用当前时间戳，单位是秒
  };
},

async loadDevices() {
  const startTime = this.currentTime; // 使用当前的时间戳

  try {
    const response = await axios.post("http://localhost:8081/visual/scroll", {
      start_time: startTime,
      interval: 60,
    });

    const rawDevices = response.data.data || [];

    if (!Array.isArray(rawDevices)) {
      console.error("设备数据不是数组：", rawDevices);
      return;
    }

    this.devices = rawDevices.map(device => ({
      deviceId: device.device_id,
      deviceType: this.mapDeviceType(device.device_type),
      timestampOfDevOut: this.formatTimestamp(device.timestamp_of_dev_out),
      timestampOfDetOut: this.formatTimestamp(device.timestamp_of_det_out),
      objectiveNum: device.objective_num,
      error: device.error === 1 ? "异常" : "正常",
    }));

    // 成功后更新时间
    this.currentTime += 60; // 每次请求后，currentTime 向后推移 60 秒
    console.log("当前轮询起始时间：", this.currentTime);
  } catch (error) {
    console.error("设备数据加载失败:", error);
  }
},=
*/


import axios from "axios";
import * as echarts from "echarts";
/* global AMap */
export default {
  name: "DataMonitorPage",
  data() {
    return {
      activeMenu: "monitor",
      isPaused: false,
      hoverIndex: null,
      devices: [0, 1, 2, 3, 4],
      pollingInterval: null, // 轮询定时器引用
      currentTime: 1722476647, // 初始时间戳，按需修改
      objectives: [],
    };
  },
  created() {
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
  mounted() {
    this.$nextTick(() => {
      this.loadDevices(); // 初始加载
      this.initMapScript();
      this.startAutoScroll();
      this.initObjectChart(); // 初始化目标对象统计图
      this.loadObjects();

      // 设置每60秒轮询一次
      this.pollingInterval = setInterval(() => {
        this.loadDevices();
        this.loadObjects();
      }, 60000);
    });
  },
  beforeUnmount() {
    // 清除定时器
    clearInterval(this.pollingInterval);
  },
  methods: {

    renderPieChart() {
      const chartDom = document.getElementById('chartContainer');
      if (!chartDom) return;

      const myChart = echarts.init(chartDom);

      // 统计不同设备类型数量
      const typeCount = {};
      for (const device of this.devices) {
        const type = device.deviceType || "未知";
        typeCount[type] = (typeCount[type] || 0) + 1;
      }

      const chartData = Object.keys(typeCount).map(key => ({
        name: key,
        value: typeCount[key],
      }));

      const option = {
        title: {
          text: '设备类型分布',
          left: 'center',
          top: 10,
          textStyle: { fontSize: 16, color: '#fff' },
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          bottom: '0%',
          left: 'center',
          textStyle: { color: '#fff' },
        },
        series: [
          {
            name: '设备类型',
            type: 'pie',
            radius: ['30%', '60%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: 'bold',
                color: '#fff',
              }
            },
            labelLine: {
              show: false
            },
            data: chartData
          }
        ]
      };

      myChart.setOption(option);
    },

    // 动态加载高德地图
    initMapScript() {
      const script = document.createElement("script");
      script.type = "text/javascript";
      script.src = "https://webapi.amap.com/maps?v=1.4.8&key=af97913ee5942f1c1f16bc41e704f766";
      script.onload = () => {
        if (this.activeMenu === "monitor") {
          this.initMap();
        }
      };
      document.head.appendChild(script);
    },

    // 初始化地图
    initMap() {
      const map = new AMap.Map("mapContainer", {
        zoom: 12,
        center: [106.484, 29.523],
        resizeEnable: true,
        mapStyle: "amap://styles/blue",
      });

      new AMap.Marker({
        position: [106.484, 29.523],
        map: map,
        title: "重庆高新区",
      });
    },

    // 设备数据请求
    async loadDevices() {
      const startTime = this.currentTime;
      const token = localStorage.getItem('token');

      try {
        const response = await axios.post("http://localhost:8081/visual/scroll", {
          start_time: startTime,
          interval: 60,
        },
        {
        headers: {
              'token': token // 这里字段名要和后端 JwtProperties 配的匹配
            }
      });

        const rawDevices = response.data.data || [];

        if (!Array.isArray(rawDevices)) {
          console.error("设备数据不是数组：", rawDevices);
          return;
        }

        this.devices = rawDevices.map(device => ({
          rcuId: device.rcu_id,
          deviceId: device.device_id,
          deviceType: this.mapDeviceType(device.device_type),
          timestampOfDevOut: this.formatTimestamp(device.timestamp_of_dev_out),
          timestampOfDetOut: this.formatTimestamp(device.timestamp_of_det_out),
          objectiveNum: device.objective_num,
          error: device.error ? "异常" : "正常",
        }));

        // 成功后更新时间
        this.currentTime += 60; // 每次请求后，currentTime 向后推移 60 秒
        console.log("当前轮询起始时间：", this.currentTime);
      } catch (error) {
        console.error("设备数据加载失败:", error);
      }

      this.renderPieChart();
    },

    // 表格自动滚动
    startAutoScroll() {
      const body = this.$refs.scrollBody;
      if (!body) return;

      let scrollPos = 0;
      const scrollStep =0.4;

      const scroll = () => {
        if (!this.isPaused && body.scrollHeight > body.clientHeight) {
          if (body.scrollHeight - body.clientHeight <= scrollPos) {
            scrollPos = 0;
          } else {
            scrollPos += scrollStep;
          }
          body.scrollTop = scrollPos;
        }
        requestAnimationFrame(scroll);
      };

      requestAnimationFrame(scroll);
    },

    pauseScroll() {
      this.isPaused = true;
    },

    resumeScroll() {
      this.isPaused = false;
    },

    // 时间格式化
formatTimestamp(ts) {
  if (ts === undefined) {
    return "无数据1";
  }
  else if(ts === null){
    return "无数据2";
  }
  else if(isNaN(Number(ts)))
  {
    return "无数据3";
  }

  const date = new Date(Number(ts) * 1000);
  if (isNaN(date.getTime())) {
    return "无效时间";
  }

  const Y = date.getFullYear();
  const M = (date.getMonth() + 1).toString().padStart(2, "0");
  const D = date.getDate().toString().padStart(2, "0");
  const h = date.getHours().toString().padStart(2, "0");
  const m = date.getMinutes().toString().padStart(2, "0");

  return `${Y}-${M}-${D} ${h}:${m}`;
},



    // 初始化图表
    initObjectChart() {
      const chartDom = this.$refs.objectChart;
        if (chartDom) {
          this.objectChartInstance = echarts.init(chartDom);
        }
      },
      
    updateObjectiveTypeStats(objectStats) {

      console.log("更新柱状图");
      const stats = {};

      // 重置：每次都重新统计
      objectStats.forEach(item => {
        const label = this.mapObjectiveType(item.type);
        stats[label] = item.count; // 直接覆盖当前数据
      });

      // 更新到组件中
      this.objectiveTypeStats = stats;

      // 设置柱状图的配置项
      const option = {
        tooltip: { trigger: "axis" },
        xAxis: {
          type: "category",
          data: Object.keys(stats), // 使用类型名称作为X轴数据
          axisLabel: { color: "#fff" },
        },
        yAxis: {
          type: "value",
          axisLabel: { color: "#fff" },
        },
        series: [
          {
            name: "目标对象数量",
            type: "bar",
            data: Object.values(stats), // 使用统计数量作为Y轴数据
            itemStyle: {
              color: "#3398DB",
              borderRadius: [6, 6, 0, 0], // 柱状图圆角
            },
          },
        ],
        grid: {
          left: "5%",
          right: "5%",
          bottom: "10%",
          containLabel: true,
        },
      };

      // if (this.objectChartInstance) {
      //   this.objectChartInstance.setOption(option); // 刷新图表
      // }

      if (this.objectChartInstance) {
        this.objectChartInstance.setOption(option, {
          notMerge: true, // 不合并旧数据，强制清空旧图表内容
          replaceMerge: ['series'], // 替换整个 series
        });
      }
      
    },


    async loadObjects() {
      const token = localStorage.getItem('token');
      console.log("轮询触发了 loadObject");

      try {
        const response = await axios.post("http://localhost:8081/visual/object", {
          start_time: this.currentTime,
          interval: 60,
        }, {
          headers: {
            'token': token
          }
        });

        const objectStats = response.data.data || [];

        if (!Array.isArray(objectStats)) {
          console.error("目标对象数据格式不正确:", objectStats);
          return;
        }

        // if (objectStats.length === 0) {
        //   console.warn("本轮目标对象数据为空，跳过图表更新");
        //   return;
        // }

        this.updateObjectiveTypeStats(objectStats); // 更新目标对象统计图
      } catch (error) {
        console.error("目标对象数据加载失败:", error);
      }
    },



    // 设备类型转换
    mapDeviceType(type) {
      const map = {
        0: "未知来源",
        1: "融合结果",
        2: "摄像头",
        3: "毫秒波雷达",
        4: "激光雷达",
      };
      return map[type] || `类型${type}`;
    },

    
    // 目标对象类型转换
    mapObjectiveType(type) {
      const map = {
        0: '行人',
        1: '自行车',
        2: '乘用车',
        3: '摩托车',
        4: '特殊用车',
        5: '公交车',
        6: '有轨道车',
        7: '卡车',
        8: '三轮车',
        9: '交通信号灯',
        10: '交通标识',
        15: '动物',
        60: '路障',
        61: '交通锥',
        254: '其他类型',
        255: '未获取',
      };

      if (map[type] === undefined) {
        console.warn(`目标对象类型 ${type} 没有映射，返回 '未定义类型'`);
      }

      return map[type] || `类型${type}`;
    },
  },
};
</script>



<style scoped>
/* 公共样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html, body {
  height: 100%;
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
.header h1::before { left: 0; }
.header h1::after { right: 0; }

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
}
.sidebar-menu {
  list-style: none;
}
.sidebar-menu li {
  padding: 12px 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
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
.top-row {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 20px;
  margin-bottom: 20px;
}
.bottom-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
.module {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid #2d5a9d;
  border-radius: 10px;
  padding: 15px;
  height: 352px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

/* 表格样式 */
.data-table {
  width: 100%;
  border-collapse: collapse;
  color: #fff;
  table-layout: fixed;
}

/* 列宽分配 (30-10-20-20-10-10) */
.data-table th:nth-child(1),
.data-table td:nth-child(1) { width: 30%; } /* 设备编号 */
.data-table th:nth-child(2),
.data-table td:nth-child(2) { width: 10%; } /* 设备类别 */
.data-table th:nth-child(3),
.data-table td:nth-child(3) { width: 20%; } /* 输出时间 */
.data-table th:nth-child(4),
.data-table td:nth-child(4) { width: 20%; } /* 输出结果 */
.data-table th:nth-child(5),
.data-table td:nth-child(5) { width: 10%; } /* 感知数量 */
.data-table th:nth-child(6),
.data-table td:nth-child(6) { width: 10%; } /* 异常状态 */

/* 表头样式 */
.data-table th {
  padding: 14px 8px;
  background: rgba(0, 102, 255, 0.2);
  position: sticky;
  top: 0;
  text-align: center; /* 所有表头默认居中 */
  font-weight: 500;
}

/* 强制输出结果列居中 */
.data-table th:nth-child(4),
.data-table td:nth-child(4) { 
  text-align: center !important; 
  padding-left: 0;
}

/* 数据单元格 */
.data-table td {
  padding: 12px 8px;
  border-bottom: 1px solid #2d5a9d;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 强制居中列 */
.data-table td:nth-child(1),  /* 设备编号 */
.data-table td:nth-child(2),  /* 设备类别 */
.data-table td:nth-child(3),  /* 输出时间 */
.data-table td:nth-child(4),  /* 输出结果 */
.data-table td:nth-child(5),  /* 感知数量 */
.data-table td:nth-child(6) { /* 异常状态 */
  text-align: center;
  padding-left: 0 !important;
}

/* 滚动容器 */
.data-table tbody {
  display: block;
  max-height: 290px;
  overflow-y: overlay;  /* 让滚动条在内容溢出时显示 */
}

/* Webkit 浏览器（如 Chrome, Safari 等） */
.data-table tbody::-webkit-scrollbar {
  width: 8px;  /* 设置滚动条宽度 */
}

.data-table tbody::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.3); /* 设置滑块的颜色为半透明 */
  border-radius: 4px;  /* 设置滑块圆角 */
}

.data-table tbody::-webkit-scrollbar-track {
  background: transparent; /* 设置滚动条轨道透明 */
}

.data-table thead tr,
.data-table tbody tr {
  display: table;
  width: 100%;
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 8px; /* 设置滚动条宽度 */
}

::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.3); /* 设置滑块的颜色为半透明 */
  border-radius: 4px;  /* 设置滑块圆角 */
}

::-webkit-scrollbar-track {
  background: transparent; /* 设置滚动条轨道透明 */
}

/* Firefox 浏览器样式 */
html {
  scrollbar-width: thin;  /* 设置滚动条宽度 */
  scrollbar-color: rgba(0, 0, 0, 0.3) transparent; /* 设置滚动条滑块颜色和轨道透明 */
}

/* 目标对象统计模块样式*/
.object-chart-module {
  background-color: #0f1c2f;
  border-radius: 8px;
  padding: 12px;
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;
}

.module-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #fff;
}

.chart-container {
  flex: 1;
  width: 100%;
  height: 300px;
}

.hovered-row {
  background-color: rgba(255, 255, 255, 0.12);
  color: #fff;
  font-weight: bold;
  transition: all 0.2s ease;
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
