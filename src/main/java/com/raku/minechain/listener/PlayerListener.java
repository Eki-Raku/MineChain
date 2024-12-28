package com.raku.minechain.listener;

import com.raku.minechain.MineChain;
import com.raku.minechain.constant.CommonConstant;
import com.raku.minechain.constant.ConfigConstant;
import com.raku.minechain.utils.StorageUtil;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

/**
 * 玩家行为监听器
 * - 只监听玩家在游戏中主动发起的行为，如破坏方块、潜行等
 * @Author Raku
 * @Date 2024/12/26
 */
public class PlayerListener implements Listener {
    /**
     * 实例注册
     */
    private final MineChain instance = MineChain.getInstance();
    private final StorageUtil storageUtil = StorageUtil.getInstance();

    /**
     * 方块破坏监听
     * - 当玩家即将破坏方块时判断是否是连锁方块
     */
    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            String blockName = event.getBlock().getBlockData().getMaterial().name();
            Set<String> allowBlocks = new HashSet<>(((ArrayList<String>) storageUtil.read(ConfigConstant.CONFIG_ALLOW_BLOCKS, ConfigConstant.CONFIG_ALLOW_BLOCKS)));
            if (allowBlocks.contains(blockName)) {
                LinkedHashSet<Block> blocks = this.bfs(event);
                // 异步执行效率更高
//                Bukkit.getScheduler().runTaskAsynchronously(instance, () -> blocks.forEach(Block::breakNaturally));
                blocks.forEach(Block::breakNaturally);
                String msg = CommonConstant.COMMON_PLUGIN_PREFIX + "破坏了" + blocks.size() + "个" + blockName;
                player.sendMessage(msg);
            }
        }
    }

    /* ============================================================================================================= */

    /**
     * 采用广度优先搜索计算连锁方块
     *
     * @param event 方块破坏事件
     * @return      本次连锁共破坏多少方块
     */
    private LinkedHashSet<Block> bfs(BlockBreakEvent event) {
        Block currBlock = event.getBlock();
        Deque<Block> queue = new LinkedList<>();
        queue.add(currBlock);
        LinkedHashSet<Block> visited = new LinkedHashSet<>();
        int count = 1; // 玩家正在破坏的方块
        visited.add(currBlock);
        int maxChainNum = instance.getConfig().getInt(ConfigConstant.CONFIG_MAX_ACTIVE_NUM);
        while (count < maxChainNum && !queue.isEmpty()) {
            Block block = queue.poll();
            for (int[] direction : CommonConstant.ALL_DIRECTION) { // 一轮处理六向
                Block neighbor = block.getRelative(direction[0], direction[1], direction[2]);
                if (count < maxChainNum && !visited.contains(neighbor) && neighbor.getType() == currBlock.getType()) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    count++;
                }
            }
        }
        return visited;
    }
}
