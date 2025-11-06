import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';
import viteCompression from 'vite-plugin-compression';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
// import { visualizer } from 'rollup-plugin-visualizer';

export default defineConfig({
  base: './',
  plugins: [
    vue(),
    createSvgIconsPlugin({
      iconDirs: [
        path.resolve(process.cwd(), 'src/assets/svg'),
        path.resolve(process.cwd(), 'src/assets/svg/pixelSvg'),
        path.resolve(process.cwd(), 'src/assets/svg/commonSvg'),
        path.resolve(process.cwd(), 'src/assets/svg/linkSvg'),
        path.resolve(process.cwd(), 'src/assets/svg/techStackSvg'),
        path.resolve(process.cwd(), 'src/assets/svg/audioSvg'),
        path.resolve(process.cwd(), 'src/assets/svg/socialSvg')
      ],
      symbolId: '[dir]-[name]'
    }),
    viteCompression({
      verbose: true,
      disable: false,
      threshold: 10240,
      algorithm: 'gzip',
      ext: '.gz'
    })
    // visualizer({
    //   open: true,
    //   gzipSize: true,
    //   brotliSize: true,
    //   filename: 'visualizer/stats.html'
    // })
  ],
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: '@import "./src/assets/theme/theme.scss";', // 加载全局样式，使用scss特性
        api: 'legacy',
        silenceDeprecations: ['import', 'global-builtin', 'legacy-js-api']
      }
    }
  },
  resolve: {
    alias: {
      // 这里就是需要配置resolve里的别名
      '@': path.join(__dirname, './src'),
      '~': path.join(__dirname, './public')
    }
  },
  optimizeDeps: {
    include: ['@/../lib/vform/designer.umd.js', 'swiper'] //此处路径必须跟main.js中import路径完全一致！
  },
  build: {
    outDir: 'c-site', // 指定打包目录为c-site
    commonjsOptions: {
      include: /node_modules|lib/ //这里记得把lib目录加进来，否则生产打包会报错！！
    },
    cssCodeSplit: true,
    rollupOptions: {
      input: 'index.html',
      output: {
        // 静态资源打包做处理
        chunkFileNames: 'static/js/[name]-[hash].js',
        entryFileNames: 'static/js/[name]-[hash].js',
        assetFileNames: 'static/[ext]/[name]-[hash].[ext]'
        // manualChunks(id) {
        //   // 处理第三方库
        //   if (id.includes('node_modules')) {
        //     return id.toString().split('node_modules/')[1].split('/')[0].toString();
        //   }
        // }
      }
    },
    minify: 'terser'
    // terserOptions: {
    //   // 清除console和debugger
    //   compress: {
    //     drop_console: true,
    //     drop_debugger: true
    //   }
    // }
  },
  server: {
    port: 8000,
    host: '0.0.0.0',
    headers: {
      'Access-Control-Allow-Origin': '*'
    },
    proxy: {
      '/dev-api': {
        // target: 'http://localhost:7000',
        target: 'http://120.48.127.181:7000',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/dev-api/, '')
      },
      // 服务器图片接口
      '/img': {
        target: 'http://120.48.127.181/file',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/img/, '')
      },
      // 地址查询接口
      '/map': {
        target: 'https://api.map.baidu.com',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/map/, '')
      },
      // 音乐搜索接口
      // '/song': {
      //   target: 'https://ml.v.api.aa1.cn',
      //   changeOrigin: true,
      //   rewrite: p => p.replace(/^\/song/, '/music')
      // },
      // 网易云音乐接口
      '/wyy': {
        target: 'http://music.163.com',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/wyy/, '')
      },
      '/chatApi': {
        target: 'https://api.siliconflow.cn',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/chatApi/, '')
      }
    }
  }
});
