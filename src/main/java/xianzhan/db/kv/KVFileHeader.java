package xianzhan.db.kv;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import xianzhan.db.util.BitUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * KV 文件头
 *
 * @author xianzhan
 * @since 2023-03-19
 */
@Slf4j
@Getter
public class KVFileHeader {

    public static final int MAGIC_NUMBER = 0xCAFE1248;

    private final KVConfig kvConfig;

    /**
     * 4 字节魔数
     */
    private final int    magicNumber;
    /**
     * 4 字节类型长度
     */
    private final int    typeLength;
    /**
     * UTF-8 类型名称
     */
    private final String typeName;
    /**
     * 4 字节版本号
     */
    private       int    version;

    KVFileHeader(KVConfig kvConfig) {
        this.kvConfig = kvConfig;
        this.magicNumber = MAGIC_NUMBER;
        // 目前都是 ascii 字符
        this.typeLength = kvConfig.getKvType().getName().getBytes(StandardCharsets.UTF_8).length;
        this.typeName = kvConfig.getKvType().getName();
    }

    public void read(byte[] bytes) {
        var off = 0;
        var magicNumber = BitUtil.getInt(bytes, off);
        if (magicNumber != this.magicNumber) {
            throw new IllegalArgumentException("magic: " + magicNumber + ", expect: " + this.magicNumber);
        }

        off += 4;
        var typeLength = BitUtil.getInt(bytes, off);
        if (typeLength != this.typeLength) {
            throw new IllegalArgumentException("typeLength: " + typeLength + ", expect: " + this.typeLength);
        }

        off += 4;
        var typeNameBytes = Arrays.copyOfRange(bytes, off, off + typeLength);
        var typeName = new String(typeNameBytes, StandardCharsets.UTF_8);
        if (!typeName.equals(this.typeName)) {
            throw new IllegalArgumentException("typeName: " + typeName + ", expect: " + this.typeName);
        }

        off += typeLength;
        // 该版本号可以判断序列化该使用哪个实现
        var version = BitUtil.getInt(bytes, off);
        log.info("KV - read: 读取文件头. version: {}", version);
    }

    /**
     * 写入文件头
     *
     * @param os 待写入的输出流
     * @throws IOException write 异常
     */
    public void write(OutputStream os) throws IOException {
        os.write(BitUtil.getBytes(magicNumber));
        os.write(BitUtil.getBytes(typeLength));
        os.write(typeName.getBytes(StandardCharsets.UTF_8));
        os.write(BitUtil.getBytes(kvConfig.getVersion()));
    }

    public int lastOff() {
        // magicNumber + typeLength + version + typeName
        return 12 + typeLength;
    }
}
