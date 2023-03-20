package xianzhan.db.kv;

import xianzhan.db.util.BitUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * KV 文件头
 *
 * @author xianzhan
 * @since 2023-03-19
 */
public class KVFileHeader {

    public static final int MAGIC_NUMBER = 0xCAFE1248;

    /**
     * 4 字节魔数
     */
    private int    magicNumber;
    /**
     * 4 字节类型长度
     */
    private int    typeLength;
    /**
     * UTF-8 类型名称
     */
    private String typeName;
    /**
     * 4 字节版本号
     */
    private int    version;

    public static KVFileHeader read(byte[] bytes) {
        var off = 0;
        var header = new KVFileHeader();
        var magicNumber = BitUtil.getInt(bytes, off);
        if (magicNumber != MAGIC_NUMBER) {
            throw new IllegalArgumentException("magic: " + magicNumber + ", expect: " + MAGIC_NUMBER);
        }
        header.magicNumber = magicNumber;

        off += 4;
        var typeLength = BitUtil.getInt(bytes, off);
        header.typeLength = typeLength;

        off += 4;
        var typeNameBytes = Arrays.copyOfRange(bytes, off, off + typeLength);
        header.typeName = new String(typeNameBytes, StandardCharsets.UTF_8);

        off += typeLength;
        header.version = BitUtil.getInt(bytes, off);

        return header;
    }

    public int lastOff() {
        // magicNumber + typeLength + version + typeName
        return 12 + typeLength;
    }


    private KVFileHeader() {

    }
}
