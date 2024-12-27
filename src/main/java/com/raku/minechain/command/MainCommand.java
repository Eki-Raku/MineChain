package com.raku.minechain.command;

import com.raku.minechain.MineChain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 插件基础命令类
 * @Author Raku
 * @Date 2024/12/26
 */
public class MainCommand implements CommandExecutor, TabCompleter {
    /**
     * 常变量定义
     */
    private static final MineChain INSTANCE = MineChain.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
