package com.clonecoding.pinterest.pin.entity;

import com.clonecoding.pinterest.entity.TimeStamped;
import com.clonecoding.pinterest.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Pin extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pinImageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Pin(Long id, String pinImageUrl, User user) {
        this.id = id;
        this.pinImageUrl = pinImageUrl;
        this.user = user;
    }
}
