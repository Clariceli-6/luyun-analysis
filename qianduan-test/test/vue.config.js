/* 
  const { defineConfig } = require('@vue/cli-service')
  module.exports = defineConfig({
    transpileDependencies: true
  })
*/

const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    proxy: {
      '/visual/scroll': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/register': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        pathRewrite: {
          '^/register': '/register',
        },
      },
    },
  },
})
