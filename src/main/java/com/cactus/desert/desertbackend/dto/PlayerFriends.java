package com.cactus.desert.desertbackend.dto;

import lombok.Data;

import java.util.List;

/**
 * @author EvanLuo42
 * @date 4/29/22 9:44 AM
 */
@Data
public class PlayerFriends {
    private List<PlayerInfo> friends;

    public PlayerFriends(List<PlayerInfo> friends) {
        this.friends = friends;
    }
}
