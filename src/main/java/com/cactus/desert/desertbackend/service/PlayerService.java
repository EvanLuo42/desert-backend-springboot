package com.cactus.desert.desertbackend.service;

import com.cactus.desert.desertbackend.dto.PlayerInfo;
import com.cactus.desert.desertbackend.entity.Player;
import com.cactus.desert.desertbackend.form.FieldError;
import com.cactus.desert.desertbackend.form.Form;

import java.util.List;
import java.util.Optional;

/**
 * @author EvanLuo42
 * @date 4/24/22 10:48 AM
 */
public interface PlayerService {
    Optional<PlayerInfo> getPlayerById(Long playerId);

    List<PlayerInfo> getAllPlayers();

    boolean createPlayer(Player player);

    boolean deletePlayer(Long playerId);

    List<FieldError> validateForm(Form form);
}
