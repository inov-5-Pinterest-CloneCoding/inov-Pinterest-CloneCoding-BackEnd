package com.clonecoding.pinterest.global.S3.entity;

import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Image extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String pinImageUrl;

    @Column
    private String title;

    @Lob
    @Column(columnDefinition = "text")
    private String content;

    @Column
    private String username;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Image(String pinImageUrl) {
        this.pinImageUrl = pinImageUrl;
    }
}
