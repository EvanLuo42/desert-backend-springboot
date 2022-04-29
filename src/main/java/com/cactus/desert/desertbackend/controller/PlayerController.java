package com.cactus.desert.desertbackend.controller;

import com.cactus.desert.desertbackend.Result;
import com.cactus.desert.desertbackend.dto.PlayerInfo;
import com.cactus.desert.desertbackend.dto.TokenDetail;
import com.cactus.desert.desertbackend.entity.Player;
import com.cactus.desert.desertbackend.form.FieldError;
import com.cactus.desert.desertbackend.form.GetPlayerByIdForm;
import com.cactus.desert.desertbackend.form.LoginForm;
import com.cactus.desert.desertbackend.form.RegisterForm;
import com.cactus.desert.desertbackend.service.PlayerService;
import com.cactus.desert.desertbackend.util.I18nUtil;
import com.cactus.desert.desertbackend.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author EvanLuo42
 * @date 4/24/22 8:35 AM
 */
@RestController
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(path = "/players", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllPlayer() {
        Result result = new Result();
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage(I18nUtil.getMessage("player.getAllPlayersSuccess"));
        result.setData(playerService.getAllPlayers());
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(name = "/player/friends", method = RequestMethod.GET)
    public ResponseEntity<Result> getPlayerFriends(@RequestHeader("Authorization") Optional<String> token) {
        Result result = new Result();

        if (token.isEmpty()) {
            result.setStatus(Result.Status.ERROR);
            result.setMessage(I18nUtil.getMessage("common.invalidToken"));
            result.setData(null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        String playerName = token.map(TokenUtil::getPlayerNameFromToken).orElse(null);

        List<PlayerInfo> friends = playerService.getPlayerByName(playerName)
                .map(PlayerInfo::getFriends)
                .orElse(null);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage(I18nUtil.getMessage("player.getPlayerFriendsSuccess"));
        result.setData(friends);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(path = "/player/{playerId}", method = RequestMethod.GET)
    public ResponseEntity<Result> getPlayerById(@PathVariable Long playerId) {
        GetPlayerByIdForm form = new GetPlayerByIdForm();
        form.setPlayerId(playerId);
        Result result = new Result();

        if (playerService.validateForm(form).isEmpty()) {
            Optional<PlayerInfo> playerInfo = playerService.getPlayerById(playerId);
            return playerInfo
                    .map(info -> {
                        result.setStatus(Result.Status.SUCCESS);
                        result.setMessage(I18nUtil.getMessage("player.getPlayerSuccess"));
                        result.setData(playerService.getAllPlayers());
                        return ResponseEntity.ok().body(result);
                    })
                    .orElseGet(() -> {
                        result.setStatus(Result.Status.ERROR);
                        result.setMessage(I18nUtil.getMessage("player.playerNotFound"));
                        result.setData(null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
                    });
        }

        result.setStatus(Result.Status.ERROR);
        result.setMessage(I18nUtil.getMessage("common.invalidForm"));
        result.setData(playerService.validateForm(form));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @RequestMapping(path = "/player/register", method = RequestMethod.POST)
    public ResponseEntity<Result> register(@RequestBody RegisterForm form) {
        List<FieldError> formValidate = playerService.validateForm(form);
        Result result = new Result();
        if (formValidate.isEmpty()) {
            Player player = new Player();
            player.setPlayerName(form.getPlayerName());
            player.setPlayerPassword(BCrypt.hashpw(form.getPlayerPassword(), BCrypt.gensalt()));
            player.setPlayerEmail(form.getPlayerEmail());

            if (!playerService.createPlayer(player)) {
                result.setStatus(Result.Status.ERROR);
                result.setMessage(I18nUtil.getMessage("player.existPlayer"));
                result.setData(null);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage(I18nUtil.getMessage("player.registerPlayerSuccess"));
            result.setData(Optional.of(player).map(PlayerInfo::new).get());
            return ResponseEntity.ok().body(result);
        }

        result.setStatus(Result.Status.ERROR);
        result.setMessage(I18nUtil.getMessage("common.invalidForm"));
        result.setData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @RequestMapping(path = "/player/login", method = RequestMethod.POST)
    public ResponseEntity<Result> login(@RequestBody LoginForm form) {
        List<FieldError> formValidate = playerService.validateForm(form);
        Result result = new Result();

        if (formValidate.isEmpty()) {
            if (playerService.loginPlayer(form.getPlayerName(), form.getPlayerPassword())) {
                TokenDetail tokenDetail = new TokenDetail();
                Optional<PlayerInfo> playerInfo = playerService.getPlayerByName(form.getPlayerName());
                if (playerInfo.map(PlayerInfo::isActive).orElse(false)) {
                    tokenDetail.setPlayerName(playerInfo.map(PlayerInfo::getPlayerName).orElse(null));
                    tokenDetail.setAdmin(playerInfo.map(PlayerInfo::isAdmin).orElse(false));

                    result.setStatus(Result.Status.SUCCESS);
                    result.setMessage(I18nUtil.getMessage("player.loginPlayerSuccess"));
                    result.setData(tokenDetail);
                    return ResponseEntity.ok().body(result);
                }

                result.setStatus(Result.Status.ERROR);
                result.setMessage(I18nUtil.getMessage("player.playerIsBanned"));
                result.setData(null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            result.setStatus(Result.Status.ERROR);
            result.setMessage(I18nUtil.getMessage("player.playerNamePasswordWrong"));
            result.setData(null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        result.setStatus(Result.Status.ERROR);
        result.setMessage(I18nUtil.getMessage("common.invalidForm"));
        result.setData(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
