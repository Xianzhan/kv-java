package xianzhan.db.kv.hash;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xianzhan.db.kv.KV;
import xianzhan.db.kv.KVConfig;

/**
 * @author xianzhan
 * @see HashKV
 * @since 2023-03-21
 */
public class HashKVTest {

    private KV kv;

    @BeforeEach
    public void before() {
        var config = new KVConfig();
        kv = new HashKV(config);
    }

    @AfterEach
    public void after() throws Exception {
        kv.close();
    }

    @Test
    public void testPut() {
        for (var i = 0; i < 2023; i++) {
            kv.put("key" + i, "value" + i);
        }
    }

    @Test
    public void testGet() {
        for (var i = 0; i < 2023; i++) {
            var key = "key" + i;
            var value = kv.get(key);
            Assertions.assertEquals("value" + i, value);
        }
    }
}
