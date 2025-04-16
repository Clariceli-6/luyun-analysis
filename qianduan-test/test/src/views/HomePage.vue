<template>
  <div class="home-page">
    <div class="cosmos-bg"></div>
    
    <div class="container">
      <!-- 头部导航 -->
      <header class="header">
        <h2 class="title">路云数据解析系统</h2>
        <nav class="auth-buttons">
          <router-link to="/login" class="auth-btn">登录</router-link>
          <router-link to="/register" class="auth-btn">注册</router-link>
        </nav>
      </header>

      <!-- 主内容区 -->
      <main class="hero">
        <h2 class="slogan floating">路云数据解析系统</h2>
        <h1>未来数据中枢</h1>
        
        <!-- 数据地球 -->
        <div class="data-globe floating">
          <div class="particles"></div>
        </div>

        <!-- 数据流展示 -->
        <div class="data-stream">
          <div class="data-card">
            <h3>数据查询</h3>
            <p class="counter">1,234,567 TB</p>
          </div>
          <div class="data-card">
            <h3>数据监控</h3>
            <p class="counter">256</p>
          </div>
          <div class="data-card">
            <h3>分析任务</h3>
            <p class="counter">89,432</p>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

// 动态计数器
const initCounters = () => {
  document.querySelectorAll('.counter').forEach(counter => {
    const target = parseInt(counter.textContent.replace(/,/g, ''))
    let current = 0
    
    const updateCounter = () => {
      current += Math.ceil(target / 100)
      if(current > target) current = target
      counter.textContent = current.toLocaleString()
      if(current < target) requestAnimationFrame(updateCounter)
    }
    
    requestAnimationFrame(updateCounter)
  })
}

// 粒子效果
const createParticles = () => {
  const particles = document.querySelector('.particles')
  for(let i = 0; i < 50; i++) {
    const particle = document.createElement('div')
    particle.style.cssText = `
      position: absolute;
      width: 2px;
      height: 2px;
      background: rgba(0, 255, 255, ${Math.random() * 0.5 + 0.3});
      border-radius: 50%;
      left: ${Math.random() * 100}%;
      top: ${Math.random() * 100}%;
      animation: particle ${Math.random() * 4 + 3}s linear infinite;
    `
    particles.appendChild(particle)
  }
}

onMounted(() => {
  initCounters()
  createParticles()
})
</script>

<style scoped>
/* 保持原有样式不变 */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Segoe UI', sans-serif;
 }

.home-page {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Segoe UI', sans-serif;
  background: #0a0e17;
  color: #fff;
  min-height: 100vh;
  overflow-x: hidden;
}

.cosmos-bg {
  position: fixed;
  width: 100vw;
  height: 100vh;
  background: 
    radial-gradient(circle at 50% 50%, 
    rgba(18, 28, 45, 0.8) 20%,
    rgba(10, 14, 23, 1) 100%);
  z-index: -1;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0rem;
  height: 88px;
  background: linear-gradient(90deg, transparent, #1a4a8d, transparent);
}

.title {
  font-size: 1.0rem;
  background: linear-gradient(45deg, #00f7ff, #0066ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 0 20px rgba(0, 103, 255, 0.5);
  letter-spacing: 2px;
}

.auth-buttons {
  display: flex;
  gap: 1.5rem;
}

.auth-btn {
  padding: 0.8rem 1.5rem;
  border: 1px solid rgba(0, 255, 255, 0.3);
  border-radius: 4px;
  background: rgba(0, 102, 255, 0.1);
  color: #00f7ff;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
  text-decoration: none;
}

.auth-btn:hover {
  background: rgba(0, 102, 255, 0.3);
  box-shadow: 0 0 15px rgba(0, 103, 255, 0.5);
}

.hero {
  text-align: center;
  margin: 5rem 0;
}

.slogan {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  background: linear-gradient(45deg, #00f7ff, #0066ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.data-globe {
  width: 600px;
  height: 250px;
  margin: 1rem auto;
  position: relative;
  background-size: contain;
}

.particles {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.data-stream {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 3rem;
  margin: 0rem 0;
}

.data-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(0, 255, 255, 0.1);
  padding: 3rem;
  border-radius: 8px;
  backdrop-filter: blur(10px);
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

.floating {
  animation: float 6s ease-in-out infinite;
}
</style>