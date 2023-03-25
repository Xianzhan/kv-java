package xianzhan.db.kv;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * kv 配置
 *
 * @author xianzhan
 * @since 2023-03-04
 */
@Slf4j
public class KVConfig {

    private static final String PREFIX = "kv-java-";

    /**
     * KV 实现类型
     */
    private KVType       kvType;
    /**
     * KV 文件头
     */
    private KVFileHeader kvFileHeader;
    /**
     * KV 版本号
     */
    private int          version;
    private boolean      init;

    public KVConfig() {
    }

    public KVConfig setKvType(KVType kvType) {
        this.kvType = kvType;
        return this;
    }

    public KVType getKvType() {
        checkInit();
        return kvType;
    }

    public KVFileHeader getKvFileHeader() {
        checkInit();
        return kvFileHeader;
    }

    public KVConfig setVersion(int version) {
        this.version = version;
        return this;
    }

    public int getVersion() {
        checkInit();
        return version;
    }

    public String getDataFileName(String name) {
        checkInit();
        return getDir() + File.separator + PREFIX + getKvType() + "-" + name + ".data";
    }

    public KVConfig init() {
        if (init) {
            throw new IllegalStateException("配置已初始化");
        }
        this.init = true;

        if (kvType == null) {
            throw new IllegalArgumentException("kvType 未配置");
        }

        initDir();
        initHeader();
        return this;
    }

    private void initDir() {
        Path dir = Path.of(getDir());
        if (Files.exists(dir)) {
            return;
        }

        log.info("KV - initDir: 初始化创建目录. dir: {}", dir);
        try {
            int nameCount = dir.getNameCount();
            if (nameCount > 1) {
                Files.createDirectories(dir);
            } else {
                Files.createDirectory(dir);
            }
        } catch (IOException e) {
            throw new RuntimeException("创建目录异常 dir: " + dir, e);
        }
    }

    private void initHeader() {
        this.kvFileHeader = new KVFileHeader(this);
    }

    public KV build() {
        checkInit();
        try {
            Constructor<? extends KV> constructor = kvType.getClazz().getDeclaredConstructor(KVConfig.class);
            return constructor.newInstance(this);
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException e
        ) {
            throw new RuntimeException(e);
        }
    }

    private String getDir() {
        return "data" + File.separator + kvType.getName();
    }

    private void checkInit() {
        if (!init) {
            throw new IllegalStateException("配置未初始化");
        }
    }
}
