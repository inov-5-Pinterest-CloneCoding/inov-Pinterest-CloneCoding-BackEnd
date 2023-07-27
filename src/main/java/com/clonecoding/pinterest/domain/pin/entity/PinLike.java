package com.clonecoding.pinterest.domain.pin.entity;

import com.clonecoding.pinterest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PinLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PinLike(User user, Pin pin) {
        this.user = user;
        this.pin = pin;
    }
}
