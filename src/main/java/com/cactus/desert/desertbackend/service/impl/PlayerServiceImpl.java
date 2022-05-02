package com.cactus.desert.desertbackend.service.impl;

import com.cactus.desert.desertbackend.dto.PlayerInfo;
import com.cactus.desert.desertbackend.entity.Player;
import com.cactus.desert.desertbackend.form.FieldError;
import com.cactus.desert.desertbackend.form.Form;
import com.cactus.desert.desertbackend.repository.PlayerRepository;
import com.cactus.desert.desertbackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author EvanLuo42
 * @date 4/24/22 11:03 AM
 */
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final Validator validator;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, Validator validator) {
        this.playerRepository = playerRepository;
        this.validator = validator;
    }


    @Override
    public Optional<PlayerInfo> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .map(PlayerInfo::new);
    }

    @Override
    public Optional<PlayerInfo> getPlayerByName(String playerName) {
        return playerRepository.findByPlayerName(playerName)
                .map(PlayerInfo::new);
    }

    @Override
    public Optional<List<PlayerInfo>> getAllPlayerFriends(Long playerId) {
        return playerRepository.findById(playerId)
                .map(player -> player.getFriends().stream().map(PlayerInfo::new).collect(Collectors.toList()));
    }

    @Override
    public List<PlayerInfo> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(PlayerInfo::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean createPlayer(Player player) {
        if (playerRepository.findByPlayerName(player.getPlayerName()).isEmpty()) {
            playerRepository.save(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePlayer(Long playerId) {
        if (playerRepository.findById(playerId).isPresent()) {
            return false;
        }
        playerRepository.deleteById(playerId);
        return true;
    }

    @Override
    public boolean loginPlayer(String playerName, String password) {
        if (playerRepository.findByPlayerName(playerName).isPresent()) {
            return BCrypt.checkpw(password, playerRepository.findByPlayerName(playerName).get().getPlayerPassword());
        }
        return false;
    }

    @Override
    public List<FieldError> validateForm(Form form) {
        return FieldError.getErrors(validator.validate(form));
    }
}
