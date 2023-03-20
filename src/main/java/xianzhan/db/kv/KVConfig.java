package xianzhan.db.kv;

/**
 * kv 配置
 *
 * @author xianzhan
 * @since 2023-03-04
 */
public class KVConfig {

    public static final String PREFIX = "kv-java-";

    private String kvType;

    public KVConfig() {
    }

    public String getKvType() {
        return kvType;
    }

    public void setKvType(String kvType) {
        this.kvType = kvType;
    }

    public String getDataFileName(String name) {
        return PREFIX + getKvType() + "-" + name + ".data";
    }
}
