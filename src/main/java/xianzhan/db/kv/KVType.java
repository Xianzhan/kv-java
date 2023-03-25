package xianzhan.db.kv;

import lombok.Getter;
import xianzhan.db.kv.hash.HashKV;

/**
 * KV 类型
 *
 * @author xianzhan
 * @since 2023-03-25
 */
@Getter
public enum KVType {

    HASH(HashKV.class, "hash");

    /**
     * KV 实现
     */
    private final Class<? extends KV> clazz;
    /**
     * 实现名称
     */
    private final String              name;

    KVType(Class<? extends KV> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }
}
