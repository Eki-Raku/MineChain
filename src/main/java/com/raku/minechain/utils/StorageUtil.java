package com.raku.minechain.utils;

import com.raku.minechain.MineChain;
import com.raku.minechain.constant.CommonConstant;
import com.raku.minechain.constant.ConfigConstant;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 数据存储相关方法类
 * - 负责管理各种数据的获取、写入等操作
 *
 * @Author Raku
 * @Date 2024/12/27
 */
public class StorageUtil {
    /**
     * 常量定义
     */
    private static StorageUtil storageUtil;
    private final MineChain instance;
    private final Connection connection;

    /**
     * 私有构造函数，防止外部直接实例化
     */
    private StorageUtil(Connection connection) {
        this.instance = MineChain.getInstance();
        this.connection = connection;
    }

    /**
     * 获取单例实例
     *
     * @return 本类实例
     */
    public static synchronized StorageUtil getInstance() {
        if (storageUtil == null) {
            storageUtil = new StorageUtil(MineChain.getConnection());
        }
        return storageUtil;
    }

    /**
     * 向存储介质中写入数据
     *
     * @param data 待写入的数据
     * @param dest 写入的位置（数据库中即是Table，yml中即是Section）
     * @return     是否成功
     */
    public <Data, Dest> boolean write(Data data, Dest dest) {
        if (Objects.isNull(this.connection)) {
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(new File(ConfigConstant.CONFIG_STORAGE_FOLDER));
                config.set(String.valueOf(dest), data);
                return true;
            } catch (Exception ex) {
                instance.getLogger().severe("数据写入失败: " + ex.getMessage());
            }
        } else {
            // todo 连接数据库时的写入操作
        }
        return false;
    }

    /**
     * 从存储介质中查询数据
     *
     * @param tableName 表名
     * @param fieldName 列名
     * @return          查询结果
     */
    public Object read(String tableName, String fieldName) {
        if (Objects.isNull(this.connection)) {
            try {
                File dataFile = new File(instance.getDataFolder(), ConfigConstant.CONFIG_STORAGE_FOLDER
                        .concat("\\").concat(tableName).concat(CommonConstant.COMMON_FILE_SUFFIX));
                FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
                return config.get(fieldName);
            } catch (Exception ex) {
                instance.getLogger().severe("数据查询失败: " + ex.getMessage());
            }
        } else {
            // todo 连接数据库时的读取操作
        }
        return null;
    }

    /* ============================================================================================================= */

}
