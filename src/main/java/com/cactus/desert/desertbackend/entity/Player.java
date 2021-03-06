package com.cactus.desert.desertbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author EvanLuo42
 * @date 4/24/22 8:19 AM
 */
@Entity
@Table(name = "player")
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue
    @Column(name = "player_id", unique = true)
    private Long playerId;

    @Column(name = "player_name", unique = true, nullable = false)
    private String playerName;

    @Column(name = "player_password", nullable = false)
    private String playerPassword;

    @Column(name = "player_email", nullable = false, unique = true)
    private String playerEmail;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;
}
