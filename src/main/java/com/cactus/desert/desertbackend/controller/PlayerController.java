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
    private final I18nUtil i18nUtil;
    private final TokenUtil tokenUtil;

    @Autowired
    public PlayerController(PlayerService playerService, I18nUtil i18nUtil, TokenUtil tokenUtil) {
        this.playerService = playerService;
        this.i18nUtil = i18nUtil;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping(path = "/players")
    public ResponseEntity<Result> getAllPlayer() {
        Result result = new Result();
        return result.success(i18nUtil.getMessage("player.getAllSuccess"),
                playerService.getAllPlayers());
    }

    @GetMapping(path = "/player/{playerId}/friends")
    public ResponseEntity<Result> getPlayerFriends(@PathVariable Long playerId) {
        Result result = new Result();

        return playerService.getAllPlayerFriends(playerId)
                .map(friend -> result.success(i18nUtil.getMessage("player.getFriendsSuccess"), friend))
                .orElse(result.notFound(i18nUtil.getMessage("player.notFound")));
    }

    @GetMapping(path = "/player/{playerId}")
    public ResponseEntity<Result> getPlayerById(@PathVariable Long playerId) {
        GetPlayerByIdForm form = new GetPlayerByIdForm();
        form.setPlayerId(playerId);
        List<FieldError> formValidate = playerService.validateForm(form);
        Result result = new Result();

        if (formValidate.isEmpty()) {
            return playerService.getPlayerById(playerId)
                    .map(info -> result.success(i18nUtil.getMessage("player.getByIdSuccess"), info))
                    .orElse(result.notFound(i18nUtil.getMessage("player.notFound")));
        }

        return result.formError(i18nUtil.getMessage("common.invalidForm"),
                formValidate);
    }

    @PostMapping(path = "/player/register")
    public ResponseEntity<Result> register(@RequestBody RegisterForm form) {
        List<FieldError> formValidate = playerService.validateForm(form);
        Result result = new Result();
        if (formValidate.isEmpty()) {
            Player player = new Player();
            player.setPlayerName(form.getPlayerName());
            player.setPlayerPassword(BCrypt.hashpw(form.getPlayerPassword(), BCrypt.gensalt()));
            player.setPlayerEmail(form.getPlayerEmail());
            player.setActive(true);
            player.setAdmin(false);

            if (!playerService.createPlayer(player)) {
                return result.conflict(i18nUtil.getMessage("player.exist"));
            }

            return result.success(i18nUtil.getMessage("player.registerSuccess"),
                    Optional.of(player).map(PlayerInfo::new).get());
        }
        return result.formError(i18nUtil.getMessage("common.invalidForm"), formValidate);
    }

    @PostMapping(path = "/player/login")
    public ResponseEntity<Result> login(@RequestBody LoginForm form) {
        List<FieldError> formValidate = playerService.validateForm(form);
        Result result = new Result();

        if (formValidate.isEmpty()) {
            if (playerService.loginPlayer(form.getPlayerName(), form.getPlayerPassword())) {
                Optional<PlayerInfo> playerInfo = playerService.getPlayerByName(form.getPlayerName());
                if (playerInfo.map(PlayerInfo::isActive).orElse(false)) {
                    TokenDetail tokenDetail = new TokenDetail();
                    tokenDetail.setPlayerName(playerInfo.map(PlayerInfo::getPlayerName).orElse(null));
                    tokenDetail.setAdmin(playerInfo.map(PlayerInfo::isAdmin).orElse(false));
                    String token = tokenUtil.generateToken(tokenDetail);

                    return result.success(i18nUtil.getMessage("player.loginSuccess"), token);
                }

                return result.unAuthorized(i18nUtil.getMessage("player.banned"));
            }

            return result.unAuthorized(i18nUtil.getMessage("player.nameOrPasswordWrong"));
        }

        return result.formError(i18nUtil.getMessage("common.invalidForm"), formValidate);
    }
}
