package com.clonecoding.pinterest.pin.entity;

import com.clonecoding.pinterest.entity.TimeStamped;
import com.clonecoding.pinterest.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String context;

    @Column
    private Long likeCount;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
