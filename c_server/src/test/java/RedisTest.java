import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

// 使用 Spring Boot 的单元测试注解
@SpringBootTest  // 启动完整的应用上下文
class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate; // 或 RedisTemplate<String, String>

    @Test
    void testSetAndGet() {
        redisTemplate.opsForValue().set("testKey", "hello");
        String value = redisTemplate.opsForValue().get("testKey");
        System.out.println(value); // 输出 hello
    }
}