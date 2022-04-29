package com.cactus.desert.desertbackend.repository;

import com.cactus.desert.desertbackend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author EvanLuo42
 * @date 4/24/22 8:21 AM
 */
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    Optional<Player> findByPlayerName(String playerName);
}
