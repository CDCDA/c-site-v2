package com.pw;

import com.pw.common.utils.PassWordUtil;

/**
 * 密码加密/解密测试用例
 * 
 * 运行方式：直接运行 main 方法
 */
public class PasswordTest {

    public static void main(String[] args) {
        // 测试1：密码加密
        testEncodePassword();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // 测试2：密码验证
        testVerifyPassword();
    }

    /**
     * 测试用例1：密码加密
     */
    public static void testEncodePassword() {
        System.out.println("【测试1：密码加密】");
        
        String rawPassword = "1";
        
        // 加密密码
        String encodedPassword = PassWordUtil.encodePassword(rawPassword);
        
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后:   " + encodedPassword);
        System.out.println("加密长度: " + encodedPassword.length());
        
        // BCrypt 每次加密结果不同（包含随机盐值）
        String encodedPassword2 = PassWordUtil.encodePassword(rawPassword);
        System.out.println("再次加密: " + encodedPassword2);
        System.out.println("两次加密结果不同: " + !encodedPassword.equals(encodedPassword2));
    }

    /**
     * 测试用例2：密码验证
     */
    public static void testVerifyPassword() {
        System.out.println("【测试2：密码验证】");
        
        String rawPassword = "myPassword123";
        String encodedPassword = PassWordUtil.encodePassword(rawPassword);
        
        // 验证正确密码
        boolean isMatch = PassWordUtil.matches(rawPassword, encodedPassword);
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密密码: " + encodedPassword);
        System.out.println("正确密码验证: " + (isMatch ? "✓ 通过" : "✗ 失败"));
        
        // 验证错误密码
        String wrongPassword = "wrongPassword";
        boolean isWrongMatch = PassWordUtil.matches(wrongPassword, encodedPassword);
        System.out.println("错误密码: " + wrongPassword);
        System.out.println("错误密码验证: " + (!isWrongMatch ? "✓ 拒绝" : "✗ 异常"));
        
        // 验证之前加密的密码（模拟数据库存储）
        String storedHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH";
        System.out.println("\n【验证已存储的哈希】");
        System.out.println("存储的哈希: " + storedHash);
        System.out.println("验证 'test123': " + PassWordUtil.matches("test123", storedHash));
    }
}
