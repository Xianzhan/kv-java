package xianzhan.db.kv.hash;

import xianzhan.db.kv.KVConfig;
import xianzhan.db.kv.KVFileHeader;
import xianzhan.db.kv.KVSerializable;
import xianzhan.db.util.BitUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * 哈希序列与反序列
 *
 * @author xianzhan
 * @since 2023-03-19
 */
public class HashKVSerializable implements KVSerializable<LinkedHashMap<String, String>, Path> {

    private final KVConfig kvConfig;

    public HashKVSerializable(KVConfig kvConfig) {
        this.kvConfig = kvConfig;
    }

    @Override
    public void read(Path path, LinkedHashMap<String, String> map) {
        if (Files.notExists(path)) {
            return;
        }

        try {
            var bytes = Files.readAllBytes(path);
            var len = bytes.length;
            var header = KVFileHeader.read(bytes);

            var off = header.lastOff();
            while (off < len) {
                var keyLen = BitUtil.getInt(bytes, off);

                off += 4;
                var keyBytes = Arrays.copyOfRange(bytes, off, off + keyLen);
                var key = new String(keyBytes, StandardCharsets.UTF_8);

                off += keyLen;
                var valLen = BitUtil.getInt(bytes, off);
                off += 4;
                var valBytes = Arrays.copyOfRange(bytes, off, off + valLen);
                var val = new String(valBytes, StandardCharsets.UTF_8);
                off += valLen;

                map.put(key, val);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void write(LinkedHashMap<String, String> map, Path path) {
        try (var bos = Files.newOutputStream(path)) {
            bos.write(BitUtil.getBytes(KVFileHeader.MAGIC_NUMBER));

            var typeNameBytes = kvConfig.getKvType().getName().getBytes(StandardCharsets.UTF_8);
            bos.write(BitUtil.getBytes(typeNameBytes.length));
            bos.write(typeNameBytes);
            bos.write(BitUtil.getBytes(2023));

            map.forEach((k, v) -> {
                try {
                    var kb = k.getBytes(StandardCharsets.UTF_8);
                    bos.write(BitUtil.getBytes(kb.length));
                    bos.write(kb);

                    var vb = v.getBytes(StandardCharsets.UTF_8);
                    bos.write(BitUtil.getBytes(vb.length));
                    bos.write(vb);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
