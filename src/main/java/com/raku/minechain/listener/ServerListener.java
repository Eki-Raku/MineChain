package com.raku.minechain.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * 服务器监听器
 * - 只监听服务器在后台进行操作，如感知到玩家上下线、
 * @Author Raku
 * @Date 2024/12/26
 */
public class ServerListener implements Listener {

    /**
     * 玩家进入服务器监听
     * @param event 进入服务器事件
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }
}
