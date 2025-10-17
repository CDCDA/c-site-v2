/*
 * @Description: 
 * @Author: cyd 1205489124@qq.com
 * @Date: 2023-06-27 17:02:51
 * @LastEditTime: 2025-10-09 13:51:07
 */
module.exports = {
  "env": {
    "browser": true,
    "es2021": true,
    "node": true
  },
  "extends": [
    "standard-with-typescript",
    "plugin:vue/vue3-essential"
  ],
  "overrides": [
    {
      "env": {
        "node": true,
        "vue/setup-compiler-macros": true
      },
      "files": [
        ".eslintrc.{js,cjs}"
      ],
      "parserOptions": {
        "sourceType": "script"
      }
    }
  ],
  "parserOptions": {
    "ecmaVersion": "latest",
    "sourceType": "module"
  },
  "plugins": [
    "vue"
  ],
  "rules": {
    "max-len": ["error", { "code": 300 }]
  }
}
