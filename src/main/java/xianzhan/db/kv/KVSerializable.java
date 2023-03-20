package xianzhan.db.kv;

/**
 * 数据序列化接口
 *
 * @author xianzhan
 * @since 2023-03-19
 */
public interface KVSerializable<T, F> {

    /**
     * 从 F 反序列化成 T
     *
     * @param f 存储数据
     * @param t Java 实例
     */
    void read(F f, T t);

    /**
     * 从 Java 实例序列化成 F
     *
     * @param t Java 实例
     * @param f 保存数据的 F
     */
    void write(T t, F f);
}
