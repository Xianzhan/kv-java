package xianzhan.db.util;

/**
 * 大端字节工具类
 *
 * @author xianzhan
 * @since 2023-03-19
 */
public class BitUtil {

    public static int getInt(byte[] b, int off) {
        return    ((b[off + 3] & 0xFF))
                + ((b[off + 2] & 0xFF) << 8)
                + ((b[off + 1] & 0xFF) << 16)
                + ((b[off])            << 24);
    }

    public static void putInt(byte[] b, int off, int val) {
        b[off + 3] = (byte) (val       );
        b[off + 2] = (byte) (val >>>  8);
        b[off + 1] = (byte) (val >>> 16);
        b[off    ] = (byte) (val >>> 24);
    }

    public static byte[] getBytes(int val) {
        var bytes = new byte[Integer.BYTES];
        putInt(bytes, 0, val);
        return bytes;
    }
}
