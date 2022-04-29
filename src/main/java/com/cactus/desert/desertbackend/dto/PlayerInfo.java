package com.cactus.desert.desertbackend.dto;

import com.cactus.desert.desertbackend.entity.Player;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EvanLuo42
 * @date 4/24/22 10:59 AM
 */
@Data
public class PlayerInfo {
    private Long playerId;
    private String playerName;
    private String playerEmail;
    private List<PlayerInfo> friends;
    private boolean isAdmin;
    private boolean isActive;

    public PlayerInfo(Player player) {
        this.playerId = player.getPlayerId();
        this.playerName = player.getPlayerName();
        this.playerEmail = player.getPlayerEmail();
        this.friends = player.getFriends().stream().map(PlayerInfo::new).collect(Collectors.toList());
        this.isAdmin = player.isAdmin();
        this.isActive = player.isActive();
    }
}
