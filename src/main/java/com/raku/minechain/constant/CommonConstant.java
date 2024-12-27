package com.raku.minechain.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 通用枚举类
 * @Author Raku
 * @Date 2024/12/26
 */
public class CommonConstant {
    /**
     * 插件前缀
     */
    public static final String PLUGIN_PREFIX = "§7[§a连锁挖矿§7] §f";
    /**
     * 默认连锁方块
     */
    public static final List<String> DEFAULT_BLOCK = Arrays.asList(
            "OAK_LOG", "SPRUCE_LOG", "BIRCH_LOG", "JUNGLE_LOG", "ACACIA_LOG",
            "DARK_OAK_LOG", "MANGROVE_LOG", "CHERRY_LOG",
            "COAL_ORE", "DEEPSLATE_COAL_ORE", "IRON_ORE", "DEEPSLATE_IRON_ORE",
            "COPPER_ORE", "DEEPSLATE_COPPER_ORE", "GOLD_ORE", "DEEPSLATE_GOLD_ORE",
            "REDSTONE_ORE", "DEEPSLATE_REDSTONE_ORE", "EMERALD_ORE",
            "DEEPSLATE_EMERALD_ORE", "LAPIS_ORE", "DEEPSLATE_LAPIS_ORE",
            "DIAMOND_ORE", "DEEPSLATE_DIAMOND_ORE", "NETHER_GOLD_ORE",
            "NETHER_QUARTZ_ORE"
    );
    /**
     * 默认连锁工具
     */
    public static final List<String> DEFAULT_TOOLS = Arrays.asList(
            "WOODEN_AXE", "WOODEN_PICKAXE",
            "STONE_AXE", "STONE_PICKAXE",
            "GOLDEN_AXE", "GOLDEN_PICKAXE",
            "IRON_AXE", "IRON_PICKAXE",
            "DIAMOND_AXE", "DIAMOND_PICKAXE"
    );
}
