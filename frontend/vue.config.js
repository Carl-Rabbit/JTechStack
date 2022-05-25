module.exports = {
  devServer: {
    port: 8080,   // default frontend port
    proxy: {
      '/api':{
        target: 'http://localhost:22222',    // backend url
        changeOrigin: true,
        secure: true,
        pathRewrite: {
          '^/api': '',
        }
      }
    },
  },
}
