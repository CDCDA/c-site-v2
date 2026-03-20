import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';
import viteCompression from 'vite-plugin-compression';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import { visualizer } from 'rollup-plugin-visualizer';
import { ViteImageOptimizer } from 'vite-plugin-image-optimizer';
// import importToCDN from 'vite-plugin-cdn-import';

export default defineConfig({
  base: './',
  plugins: [
    vue(),
    // importToCDN({
    //   modules: [
    //     {
    //       name: 'vue',
    //       var: 'Vue',
    //       path: 'https://registry.npmmirror.com/vue/3.5.13/files/dist/vue.global.prod.js'
    //     },
    //     {
    //       name: 'vue-demi',
    //       var: 'VueDemi',
    //       path: 'https://registry.npmmirror.com/vue-demi/0.14.10/files/lib/index.iife.js'
    //     },
    //     {
    //       name: 'vue-router',
    //       var: 'VueRouter',
    //       path: 'https://registry.npmmirror.com/vue-router/4.0.3/files/dist/vue-router.global.prod.js'
    //     },
    //     {
    //       name: 'pinia',
    //       var: 'Pinia',
    //       path: 'https://registry.npmmirror.com/pinia/2.1.4/files/dist/pinia.iife.prod.js'
    //     },
    //     {
    //       name: 'element-plus',
    //       var: 'ElementPlus',
    //       path: 'https://registry.npmmirror.com/element-plus/2.8.8/files/dist/index.full.js',
    //       css: 'https://registry.npmmirror.com/element-plus/2.8.8/files/dist/index.css'
    //     },
    //     {
    //       name: 'three',
    //       var: 'THREE',
    //       path: 'https://registry.npmmirror.com/three/0.126.1/files/build/three.min.js'
    //     },
    //     {
    //       name: 'echarts',
    //       var: 'echarts',
    //       path: 'https://registry.npmmirror.com/echarts/5.4.1/files/dist/echarts.min.js'
    //     },
    //     {
    //       name: 'echarts-gl',
    //       var: 'echartsGL',
    //       path: 'https://registry.npmmirror.com/echarts-gl/2.0.9/files/dist/echarts-gl.min.js'
    //     },
    //     {
    //       name: 'axios',
    //       var: 'axios',
    //       path: 'https://registry.npmmirror.com/axios/1.2.1/files/dist/axios.min.js'
    //     },
    //     {
    //       name: 'lodash',
    //       var: '_',
    //       path: 'https://registry.npmmirror.com/lodash/4.17.21/files/lodash.min.js'
    //     },
    //     {
    //       name: 'moment',
    //       var: 'moment',
    //       path: 'https://registry.npmmirror.com/moment/2.30.1/files/moment.js'
    //     },
    //     {
    //       name: 'swiper',
    //       var: 'Swiper',
    //       path: 'https://registry.npmmirror.com/swiper/11.2.6/files/swiper-bundle.min.js',
    //       css: 'https://registry.npmmirror.com/swiper/11.2.6/files/swiper-bundle.min.css'
    //     }
    //   ]
    // }),
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
    }),
    ViteImageOptimizer({
      png: { quality: 70 },
      jpeg: { quality: 70 },
      webp: { quality: 70 }
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
    outDir: 'c-site',
    reportCompressedSize: false, // 禁用，提高构建速度
    sourcemap: false, // 生产环境关闭 sourcemap
    commonjsOptions: {
      include: /node_modules|lib/
    },
    cssCodeSplit: true,
    assetsInlineLimit: 4096, // 小于 4kb 的转 base64
    rollupOptions: {
      input: 'index.html',
      output: {
        // 静态资源打包做处理
        chunkFileNames: 'static/js/[name]-[hash].js',
        entryFileNames: 'static/js/[name]-[hash].js',
        assetFileNames: 'static/[ext]/[name]-[hash].[ext]'
        // manualChunks(id) {
        //   if (id.includes('node_modules')) {
        //     // 把所有 node_modules 打成一个 vendor.js
        //     // 这样首屏只需要下载 index.js 和 vendor.js，对 1Mbps 带宽最友好
        //     return 'vendor';
        //   }
        // }
        // manualChunks(id) {
        //   // 处理第三方库
        //   if (id.includes('node_modules')) {
        //     return id.toString().split('node_modules/')[1].split('/')[0].toString();
        //   }
        // }
      }
    },
    minify: 'terser',
    terserOptions: {
      // 清除console和debugger
      compress: {
        // drop_console: true,
        drop_debugger: true
      }
    }
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
        target: 'http://localhost:7000',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/dev-api/, '')
      },
      // 服务器图片接口
      '/img': {
        target: 'https://120.48.127.181/file',
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
        target: 'https://api.moonshot.cn',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/chatApi/, '')
      },
      '/ip-api': {
        target: 'http://ip-api.com',
        changeOrigin: true,
        rewrite: p => p.replace(/^\/ip-api/, '')
      }
    }
  }
});
