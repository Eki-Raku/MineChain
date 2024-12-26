package com.raku.minechain;

import com.raku.minechain.command.MainCommand;
import com.raku.minechain.constant.CommonConstant;
import com.raku.minechain.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
        // 注册所需组件
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
}
