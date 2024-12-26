package com.raku.minechain.domain.entity;

import lombok.Data;

import java.util.UUID;

/**
 * 在线玩家实体类
 *
 * @Author Raku
 * @Date 2024/12/26
 */
@Data
public class OnlinePlayer {
    /**
     * 玩家唯一标识
     */
    private UUID uniqueId;
    /**
     * 玩家登录名
     */
    private String name;
}
