/*
 * @Description: Pagination组件单独构建配置
 */
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';
export default defineConfig({
  base: './',
  plugins: [vue()],
  css: {
    preprocessorOptions: {
      scss: {}
    }
  },
  resolve: {
    alias: {
      '@': path.join(__dirname, './src')
    }
  },
  build: {
    outDir: 'dist-testNpm',
    lib: {
      entry: path.resolve(__dirname, 'src/components/testNpm/export.js'),
      name: 'TestNpm',
      fileName: format => `testNpm.${format}.js`,
      formats: ['es', 'umd', 'cjs']
    },
    rollupOptions: {
      // 确保外部化处理那些你不想打包进库的依赖
      external: ['vue', 'element-plus'],
      output: {
        // 在 UMD 构建模式下为这些外部化的依赖提供一个全局变量
        globals: {
          vue: 'Vue',
          'element-plus': 'ElementPlus'
        },
        // 禁用命名导出和默认导出同时使用的警告
        exports: 'named'
      }
    },
    cssCodeSplit: true,
    minify: 'terser'
  }
});
