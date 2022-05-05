package com.cactus.desert.desertbackend.service.impl;

import com.cactus.desert.desertbackend.dto.PlayerInfo;
import com.cactus.desert.desertbackend.entity.FriendRelation;
import com.cactus.desert.desertbackend.entity.Player;
import com.cactus.desert.desertbackend.form.FieldError;
import com.cactus.desert.desertbackend.form.Form;
import com.cactus.desert.desertbackend.repository.FriendRepository;
import com.cactus.desert.desertbackend.repository.PlayerRepository;
import com.cactus.desert.desertbackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.ArrayList;
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
    private final FriendRepository friendRepository;
    private final Validator validator;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, FriendRepository friendRepository, Validator validator) {
        this.playerRepository = playerRepository;
        this.friendRepository = friendRepository;
        this.validator = validator;
    }


    @Override
    public Optional<PlayerInfo> getPlayerById(Long playerId) {
        return playerRepository.findByPlayerId(playerId)
                .map(PlayerInfo::new);
    }

    @Override
    public Optional<PlayerInfo> getPlayerByName(String playerName) {
        return playerRepository.findByPlayerName(playerName)
                .map(PlayerInfo::new);
    }

    @Override
    public List<PlayerInfo> getAllPlayerFriends(Long playerId) {
        List<PlayerInfo> friends = new ArrayList<>();
        playerRepository.findByPlayerId(playerId)
                .map(player -> {
                    friendRepository.getFriendRelationsByPlayerId(playerId).map(f -> {
                        f.forEach(friend -> friends.add(getPlayerById(friend.getFriendId()).get()));
                        return friends;
                    });
                    return friends;
                });
        return friends;
    }

    @Override
    public boolean addFriend(Long friendId, String playerName) {
        Optional<Player> player = playerRepository.findByPlayerName(playerName);
        Optional<Player> friend = playerRepository.findById(friendId);

        if (friend.isEmpty()) {
            return false;
        }

        return player.map(p -> {
            if (friendRepository.existsByFriendIdAndPlayerId(friendId, p.getPlayerId())) return false;
            if (friendId.equals(player.get().getPlayerId())) return false;
            FriendRelation friendRelation = new FriendRelation();
            friendRelation.setFriendId(friendId);
            friendRelation.setPlayerId(p.getPlayerId());
            friendRepository.save(friendRelation);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean removeFriend(Long friendId, String playerName) {
        Optional<Player> player = playerRepository.findByPlayerName(playerName);
        Optional<Player> friend = playerRepository.findById(friendId);

        if (friend.isEmpty()) {
            return false;
        }

        return player.map(p -> {
            if (friendRepository.existsByFriendIdAndPlayerId(friendId, p.getPlayerId())) return false;
            FriendRelation friendRelation = new FriendRelation();
            friendRelation.setFriendId(friendId);
            friendRelation.setPlayerId(p.getPlayerId());
            friendRepository.delete(friendRelation);
            return true;
        }).orElse(false);
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
    public boolean validateLogin(String playerName, String password) {
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
