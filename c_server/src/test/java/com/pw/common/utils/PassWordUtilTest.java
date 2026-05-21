package com.pw.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class PassWordUtilTest {

    @Test
    public void testEncodePassword() {
        String rawPassword = "123456";
        String encodedPassword = PassWordUtil.encodePassword(rawPassword);

        // 验证加密后不为空
        assertNotNull(encodedPassword);
        // 验证加密后的密码与原密码不同
        assertNotEquals(rawPassword, encodedPassword);
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后: " + encodedPassword);
    }

    @Test
    public void testMatches_CorrectPassword() {
        String rawPassword = "admin123";
        String encodedPassword = PassWordUtil.encodePassword(rawPassword);

        // 验证正确密码匹配成功
        assertTrue(PassWordUtil.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testMatches_WrongPassword() {
        String rawPassword = "admin123";
        String encodedPassword = PassWordUtil.encodePassword(rawPassword);

        // 验证错误密码匹配失败
        assertFalse(PassWordUtil.matches("wrongPassword", encodedPassword));
    }

    @Test
    public void testSamePasswordDifferentEncoded() {
        String rawPassword = "testPassword";

        // 相同密码每次加密结果不同（因为BCrypt使用盐）
        String encoded1 = PassWordUtil.encodePassword(rawPassword);
        String encoded2 = PassWordUtil.encodePassword(rawPassword);

        assertNotEquals(encoded1, encoded2);

        // 但都能匹配原密码
        assertTrue(PassWordUtil.matches(rawPassword, encoded1));
        assertTrue(PassWordUtil.matches(rawPassword, encoded2));
    }

    @Test
    public void testEmptyPassword() {
        String rawPassword = "";
        String encodedPassword = PassWordUtil.encodePassword(rawPassword);

        assertNotNull(encodedPassword);
        assertTrue(PassWordUtil.matches(rawPassword, encodedPassword));
    }

    // ==================== AES可逆加密测试 ====================

    @Test
    public void testAesEncrypt() {
        String original = "敏感数据123";
        String encrypted = PassWordUtil.encrypt(original);

        assertNotNull(encrypted);
        assertNotEquals(original, encrypted);
        System.out.println("原始内容: " + original);
        System.out.println("AES加密后: " + encrypted);
    }

    @Test
    public void testAesDecrypt() {
        String original = "敏感数据456";
        String encrypted = PassWordUtil.encrypt(original);
        String decrypted = PassWordUtil.decrypt(encrypted);

        assertEquals(original, decrypted);
        System.out.println("加密: " + encrypted);
        System.out.println("解密: " + decrypted);
    }

    @Test
    public void testAesEncryptDecrypt_SpecialChars() {
        String original = "密码!@#$%^&*()中文测试123";
        String encrypted = PassWordUtil.encrypt(original);
        String decrypted = PassWordUtil.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    @Test
    public void getDecrypt() {
        String original = "$2a$10$Tc0aUseDizhP1yArlBibqusbPSD2.K0H10RFaJU28vK2UyWHdq9Gy";
        String decrypted = PassWordUtil.decrypt(original);
        log.info("解密后: {}", decrypted);
    }
}
