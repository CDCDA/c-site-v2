import com.pw.PServerV3Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Set;

// 使用 Spring Boot 的单元测试注解
@SpringBootTest(classes = PServerV3Application.class)  // 启动完整的应用上下文
@Slf4j
class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate; // 或 RedisTemplate<String, String>

    @Test
    void testSetAndGet() {
        redisTemplate.opsForValue().set("testKey", "hello");
        String value = (String) redisTemplate.opsForValue().get("testKey");
        System.out.println(value); // 输出 hello
    }

    @Test
    void testHash() {
        redisTemplate.delete("testHash");
        redisTemplate.opsForHash().put("testHash", "field1", 1);
        Integer value = (Integer) redisTemplate.opsForHash().get("testHash", 1);
        redisTemplate.opsForHash().put("testHash", "field2", 2);
        redisTemplate.opsForHash().increment("testHash", "field1", 1);
        Map<String, String> entries = redisTemplate.opsForHash().entries("testHash");
        log.info(entries.toString());
    }

    @Test
    void testList() {
        redisTemplate.delete("testList");
        redisTemplate.opsForList().leftPush("testList", "1");
        redisTemplate.opsForList().leftPush("testList", "1");
        redisTemplate.opsForList().leftPush("testList", "2");
        log.info(redisTemplate.opsForList().range("testList", 0, -1).toString());
        redisTemplate.opsForList().remove("testList", 1, "1");
        log.info(redisTemplate.opsForList().range("testList", 0, -1).toString());
        redisTemplate.opsForList().leftPop("testList");

        log.info(redisTemplate.opsForList().range("testList", 0, -1).toString());
    }

    @Test
    void testSet() {
        // SADD friends:1001 Alice Bob
        redisTemplate.opsForSet().add("friends:1001", "Alice", "Bob");
        log.info(redisTemplate.opsForSet().members("friends:1001").toString());
// SADD friends:1002 Bob Charlie
        redisTemplate.opsForSet().add("friends:1002", "Bob", "Charlie");
        log.info(redisTemplate.opsForSet().members("friends:1002").toString());
// SISMEMBER friends:1001 Alice
        Boolean isMember = redisTemplate.opsForSet().isMember("friends:1001", "Alice");  // true
        log.info(isMember.toString());
// SINTER friends:1001 friends:1002 (交集)
        Set<String> common = redisTemplate.opsForSet().intersect("friends:1001", "friends:1002");
        log.info(common.toString());
// common: ["Bob"]

// SMEMBERS friends:1001
        Set<String> members = redisTemplate.opsForSet().members("friends:1001");
        log.info(members.toString());
    }
}