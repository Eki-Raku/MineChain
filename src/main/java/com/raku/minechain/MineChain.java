package com.raku.minechain;

import com.raku.minechain.command.MainCommand;
import com.raku.minechain.constant.CommonConstant;
import com.raku.minechain.listener.PlayerListener;
import com.raku.minechain.listener.ServerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 插件主类
 * @Author Raku
 * @Date 2024/12/26
 */
public final class MineChain extends JavaPlugin {
    /**
     * 常变量定义
     */
    private String maxActiveNum;
    private String database;
    private String dbUrl;
    private String dbUsername;
    private String dbPassWord;


    /**
     * 禁止实例化本类造成安全问题
     */
    private MineChain() {
        throw new RuntimeException("私有类无法通过构造方法实例化");
    }

    /**
     * 插件启动初始化
     */
    @Override
    public void onEnable() {
        // 加载并更新配置文件
        this.saveDefaultConfig();
        this.loadConfig();
        // 注册所需组件
        Bukkit.getPluginManager().registerEvents(new ServerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Objects.requireNonNull(this.getCommand("MineChain")).setExecutor(new MainCommand(this));
        Objects.requireNonNull(this.getCommand("MineChain")).setTabCompleter(new MainCommand(this));
        // 启动成功提示
        this.getLogger().info(CommonConstant.PLUGIN_PREFIX + "插件启动成功");
    }

    /**
     * 插件关闭
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * 重载配置文件
     */
    private void loadConfig() {
        this.reloadConfig();
        FileConfiguration config = this.getConfig();
        // 最大连锁方块数 - Begin
        this.maxActiveNum = config.getString("max-active-num");
        assert this.maxActiveNum != null;
        if (this.maxActiveNum.length() >= 4 || Integer.parseInt(this.maxActiveNum) >= 200) {
            this.getLogger().info(CommonConstant.PLUGIN_PREFIX + "最大连锁方块数超过200可能对服务器造成极大负担");
        }
        // 最大连锁方块数 - End
        // 数据库配置 - Begin
        this.database = config.getBoolean("database.enabled") ? config.getString("database.type") : null;
        if (Objects.nonNull(this.database)) {
            this.dbUrl = config.getString("database.url");
            this.dbUsername = config.getString("database.username");
            this.dbPassWord = config.getString("database.password");
            this.loadDatabase();
        }
        // 数据库配置 - End
    }

    /**
     * 测试数据库连接
     */
    private void loadDatabase() {
        try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassWord)) {
            boolean valid = connection.isValid(3);
            this.getLogger().info(valid ?
                    CommonConstant.PLUGIN_PREFIX + "数据库连接成功建立，且处于可用状态" :
                    CommonConstant.PLUGIN_PREFIX + "数据库连接成功建立，但是数据库目前处于不可用状态");
        } catch (SQLException ex) {
            throw new RuntimeException(CommonConstant.PLUGIN_PREFIX + "数据库连接失败，原因是: " + ex.getMessage());
        }
    }
}
