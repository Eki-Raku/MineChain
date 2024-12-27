package com.raku.minechain.utils;

import com.raku.minechain.MineChain;
import com.raku.minechain.constant.CommonConstant;
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
    private MineChain instance;
    private Connection connection;

    /**
     * 私有构造函数，防止外部直接实例化
     */
    private StorageUtil(Connection connection) {
        this.instance = MineChain.getInstance();
        this.connection = connection;
        if (Objects.isNull(this.connection)) {
            this.initialLocalStorage();
        }
    }

    /**
     * 获取单例实例
     *
     * @param connection 数据库连接
     * @return           本类实例
     */
    public static synchronized StorageUtil get(Connection connection) {
        if (storageUtil == null) {
            storageUtil = new StorageUtil(connection);
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
                FileConfiguration config = YamlConfiguration.loadConfiguration(new File(CommonConstant.STORAGE_FOLDER));
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
     * @param tableName  表名
     * @param conditions 查询条件
     * @return           查询结果
     */
    public Object read(String tableName, ArrayList<String> conditions) {
        if (Objects.isNull(this.connection)) {
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(new File(CommonConstant.STORAGE_FOLDER));
                return config.get(tableName);
            } catch (Exception ex) {
                instance.getLogger().severe("数据查询失败: " + ex.getMessage());
            }
        } else {
            // todo 连接数据库时的读取操作
        }
        return null;
    }

    /* ============================================================================================================= */

    /**
     * 当数据库设置为空时，使用本地Yml文件作为数据存储工具
     */
    private void initialLocalStorage() {
        File dataFolder = instance.getDataFolder();
        // 创建数据库文件夹
        File databaseFolder = new File(dataFolder, "data");
        if (!databaseFolder.exists() && databaseFolder.mkdir()) {
            instance.getLogger().info(CommonConstant.PLUGIN_PREFIX + "本地数据库文件夹已创建: " + databaseFolder.getAbsolutePath());
        }
        // 初始化文件创建
        File blocks = new File(databaseFolder, "blocks.yml");
        File tools = new File(databaseFolder, "tools.yml");
        try {
            boolean b = blocks.createNewFile();
            if (b) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(blocks);
                this.fillDefaultBlocks(config);
                config.save(blocks);
            }
            boolean t = tools.createNewFile();
            if (t) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(tools);
                this.fillDefaultTools(config);
                config.save(tools);
            }
        } catch (IOException ex) {
            instance.getLogger().severe(CommonConstant.PLUGIN_PREFIX + "创建数据文件时发生错误: " + ex.getMessage());
        }
        instance.getLogger().info(CommonConstant.PLUGIN_PREFIX + "本地存储初始化完成");
    }

    /**
     * 当默认文件不存在时，需要用默认数据填充创建的空文件
     * - 填充默认允许的连锁方块
     */
    private void fillDefaultBlocks(FileConfiguration config) {
        config.set("blocks", CommonConstant.DEFAULT_BLOCK);
    }

    /**
     * 当默认文件不存在时，需要用默认数据填充创建的空文件
     * - 填充默认允许的连锁工具
     */
    private void fillDefaultTools(FileConfiguration config) {
        config.set("tools", CommonConstant.DEFAULT_TOOLS);
    }
}
