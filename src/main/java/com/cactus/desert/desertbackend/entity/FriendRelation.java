package com.cactus.desert.desertbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author EvanLuo42
 * @date 5/3/22 3:57 PM
 */
@Entity
@Table(name = "friend_relation")
@Getter
@Setter
public class FriendRelation {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue
    private Long id;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "friend_id", nullable = false)
    private Long friendId;
}
