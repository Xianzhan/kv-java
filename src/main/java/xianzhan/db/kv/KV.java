package xianzhan.db.kv;

/**
 * key/value 接口
 *
 * @author xianzhan
 * @since 2023-03-04
 */
public interface KV extends AutoCloseable {

    /**
     * 添加/覆盖 k 上的 v
     *
     * @param k   key
     * @param v   value
     * @return null/oldValue
     */
    String put(String k, String v);

    /**
     * 删除 k 上的 v
     *
     * @param k   key
     * @return null/oldValue
     */
    String del(String k);

    /**
     * 获取 k 上的 v
     *
     * @param k   key
     * @return null/oldValue
     */
    String get(String k);
}
