package com.raku.minechain;

import com.raku.minechain.command.MainCommand;
import com.raku.minechain.constant.CommonConstant;
import com.raku.minechain.constant.ConfigConstant;
import com.raku.minechain.listener.PlayerListener;
import com.raku.minechain.listener.ServerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 插件主类
 *
 * @Author Raku
 * @Date 2024/12/26
 */
public final class MineChain extends JavaPlugin {
    /**
     * 常变量定义
     */
    private static MineChain instance;
    private String maxActiveNum;
    private String database;
    private String dbUrl;
    private String dbUsername;
    private String dbPassWord;
    private static Connection connection;

    /**
     * 插件启动初始化
     */
    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        getLogger().info("开始加载插件...");

        // 加载并更新配置文件
        instance = this;
        this.saveDefaultConfig();
        this.loadConfig();
        // 注册所需组件
        this.registerListeners();
        this.registerCommands();

        long stop = System.currentTimeMillis();
        getLogger().info(String.format("插件启动成功，耗时%s毫秒。", stop - start));
    }

    /**
     * 插件关闭
     */
    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * 使用单例模式获取插件实例
     * @return 实例
     */
    public static synchronized MineChain getInstance() {
        return instance;
    }

    /**
     * 使用单例模式获取数据库连接实例
     * @return 实例
     */
    public static synchronized Connection getConnection() {
        return connection;
    }

    /* ============================================================================================================= */

    /**
     * 注册插件监听器
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ServerListener(), this);
    }

    /**
     * 注册插件命令
     */
    private void registerCommands() {
        PluginCommand command = Bukkit.getPluginCommand("minechain");
        if (command != null) {
            command.setExecutor(new MainCommand());
            command.setTabCompleter(new MainCommand());
        } else {
            getLogger().severe("插件命令注册失败");
        }
    }

    /**
     * 加载配置文件
     */
    private void loadConfig() {
        this.reloadConfig();
        FileConfiguration config = this.getConfig();
        // 最大连锁方块数 - Begin
        this.maxActiveNum = config.getString("max-active-num");
        assert this.maxActiveNum != null;
        if (this.maxActiveNum.length() >= 4 || Integer.parseInt(this.maxActiveNum) >= 200) {
            getLogger().severe("最大连锁方块数超过200可能对服务器造成极大负担");
        }
        // 最大连锁方块数 - End
        // 数据库配置 - Begin
        this.database = config.getBoolean("database.enabled") ? config.getString("database.type") : null;
        if (Objects.nonNull(this.database)) {
            this.dbUrl = config.getString("database.url");
            this.dbUsername = config.getString("database.username");
            this.dbPassWord = config.getString("database.password");
            this.loadDatabase();
        } else {
           this.initialLocalStorage();
        }
        // 数据库配置 - End
    }

    /**
     * 测试数据库连接
     */
    private void loadDatabase() {
        try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassWord)) {
            boolean valid = connection.isValid(3);
            getLogger().info(valid ?
                    "数据库连接成功建立，且处于可用状态" :
                    "数据库连接成功建立，但是数据库目前处于不可用状态");
        } catch (SQLException ex) {
            getLogger().severe("数据库连接失败，原因是: " + ex.getMessage());
        }
    }

    /**
     * 当数据库设置为空时，使用本地Yml文件作为数据存储工具
     */
    private void initialLocalStorage() {
        File dataFolder = instance.getDataFolder();
        // 创建数据库文件夹
        File databaseFolder = new File(dataFolder, ConfigConstant.CONFIG_STORAGE_FOLDER);
        if (!databaseFolder.exists() && databaseFolder.mkdir()) {
            instance.getLogger().info("本地数据库文件夹已创建: " + databaseFolder.getAbsolutePath());
        }
        // 初始化文件创建
        File blocks = new File(databaseFolder, ConfigConstant.CONFIG_ALLOW_BLOCKS + CommonConstant.COMMON_FILE_SUFFIX);
        File tools = new File(databaseFolder, ConfigConstant.CONFIG_ALLOW_TOOLS + CommonConstant.COMMON_FILE_SUFFIX);
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
            instance.getLogger().severe("创建数据文件时发生错误: " + ex.getMessage());
        }
        instance.getLogger().info("本地存储初始化完成");
    }

    /**
     * 当默认文件不存在时，需要用默认数据填充创建的空文件
     * - 填充默认允许的连锁方块
     */
    private void fillDefaultBlocks(FileConfiguration config) {
        config.set(ConfigConstant.CONFIG_ALLOW_BLOCKS, CommonConstant.COMMON_DEFAULT_BLOCK);
    }

    /**
     * 当默认文件不存在时，需要用默认数据填充创建的空文件
     * - 填充默认允许的连锁工具
     */
    private void fillDefaultTools(FileConfiguration config) {
        config.set(ConfigConstant.CONFIG_ALLOW_TOOLS, CommonConstant.COMMON_DEFAULT_TOOLS);
    }
}
