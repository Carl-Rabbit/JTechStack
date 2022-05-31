module.exports = {
  devServer: {
    port: 8123,   // default frontend port
    proxy: {
      '/api':{
        target: 'http://localhost:22222',    // backend url
        // target: 'http://10.25.141.153:22222',    // backend url
        changeOrigin: true,
        secure: true,
        pathRewrite: {
          '^/api': '',
        }
      }
    },
  },
}
