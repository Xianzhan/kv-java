package xianzhan.db.kv.hash;

import xianzhan.db.kv.KV;
import xianzhan.db.kv.KVConfig;
import xianzhan.db.kv.KVSerializable;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 哈希存储
 *
 * @author xianzhan
 * @since 2023-03-04
 */
public class HashKV implements KV {

    /**
     * 存储在硬盘上的文件数量
     */
    private static final int LEN = 10;

    private final KVConfig                                            config;
    private final KVSerializable<LinkedHashMap<String, String>, Path> kvSerializable;
    private final Path[]                                              dataFile;
    private final LinkedHashMap<String, String>[]                     dataMap;

    @SuppressWarnings("unchecked")
    public HashKV(KVConfig config) {
        this.config = config;
        this.dataFile = new Path[LEN];
        this.dataMap = new LinkedHashMap[LEN];
        this.kvSerializable = new HashKVSerializable(config);

        config.setKvType("hash");
    }

    Map<String, String> getSlot(String k) {
        var index = Math.abs(hash(k) % LEN);
        var map = dataMap[index];
        if (map == null) {
            map = new LinkedHashMap<>();
            // 还未加载数据文件，需先加载
            var file = dataFile[index];
            if (file == null) {
                String dataFileName = config.getDataFileName(Integer.toString(index));
                file = Path.of(dataFileName);
                dataFile[index] = file;

                kvSerializable.read(file, map);
                dataMap[index] = map;
            }
        }

        return map;
    }

    @Override
    public String put(String k, String v) {
        Objects.requireNonNull(k);
        return getSlot(k).put(k, v);
    }

    @Override
    public String del(String k) {
        Objects.requireNonNull(k);
        return getSlot(k).remove(k);
    }

    @Override
    public String get(String k) {
        Objects.requireNonNull(k);
        return getSlot(k).get(k);
    }

    private static int hash(String k) {
        return Objects.hashCode(k);
    }

    @Override
    public void close() throws Exception {
        for (var i = 0; i < LEN; i++) {
            var map = dataMap[i];
            if (map != null) {
                var path = dataFile[i];
                kvSerializable.write(map, path);
            }
        }
    }
}
