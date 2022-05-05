package com.cactus.desert.desertbackend.repository;

import com.cactus.desert.desertbackend.entity.FriendRelation;
import com.cactus.desert.desertbackend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author EvanLuo42
 * @date 5/3/22 4:09 PM
 */
public interface FriendRepository extends JpaRepository<FriendRelation, Long>, JpaSpecificationExecutor<Player> {
    Optional<List<FriendRelation>> getFriendRelationsByPlayerId(Long playerId);
    boolean existsByFriendIdAndPlayerId(Long friendId, Long playerId);
}
