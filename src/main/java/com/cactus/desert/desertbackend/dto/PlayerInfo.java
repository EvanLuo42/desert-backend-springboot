package com.cactus.desert.desertbackend.dto;

import com.cactus.desert.desertbackend.entity.Player;
import lombok.Data;

/**
 * @author EvanLuo42
 * @date 4/24/22 10:59 AM
 */
@Data
public class PlayerInfo {
    private Long playerId;
    private String playerName;
    private String playerEmail;
    private boolean isAdmin;
    private boolean isActive;

    public PlayerInfo(Player player) {
        this.playerId = player.getPlayerId();
        this.playerName = player.getPlayerName();
        this.playerEmail = player.getPlayerEmail();
        this.isAdmin = player.isAdmin();
        this.isActive = player.isActive();
    }
}
