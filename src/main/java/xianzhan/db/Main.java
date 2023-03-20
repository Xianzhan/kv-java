package xianzhan.db;

import xianzhan.db.kv.KVConfig;
import xianzhan.db.kv.hash.HashKV;

/**
 * 启动类
 *
 * @author xianzhan
 * @since 2023-03-04
 */
public class Main {

    public static void main(String[] args) {
        var config = new KVConfig();
        try (var kv = new HashKV(config)) {
            kv.put("1", "one");
            kv.put("2", "two");
//
            var v = kv.get("1");
            System.out.println(v);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
