package com.raku.minechain.utils;

import com.raku.minechain.MineChain;
import com.raku.minechain.constant.CommonConstant;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 数据存储相关方法类
 * - 负责管理各种数据的获取、写入等操作
 * @Author Raku
 * @Date 2024/12/27
 */
public class StorageUtil {
    /**
     * 常量定义
     */
    private MineChain instance;
    private Connection connection;

    /**
     * 禁止无参构造
     */
    private StorageUtil() {
        throw new RuntimeException("该类无法通过无参构造方法实例化");
    }

    /**
     * 有参构造函数，保证每次调用始终都有数据库可用
     */
    public StorageUtil(Connection connection) {
        instance = MineChain.getInstance();
        this.connection = connection;
        if (Objects.isNull(this.connection)) {
            this.initialLocalStorage();;
        }
    }

    /**
     * 当数据库设置为空时，使用本地Yml文件作为数据存储工具
     */
    private void initialLocalStorage() {
        File dataFolder = instance.getDataFolder();
        // 创建数据库文件夹
        File databaseFolder = new File(dataFolder, "database");
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
