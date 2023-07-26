package com.clonecoding.pinterest.global.S3.entity;

import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
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

    @Column
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Image(String pinImageUrl,User user, String title, String content){
        this.pinImageUrl = pinImageUrl;
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public Image(String pinImageUrl) {
        this.pinImageUrl = pinImageUrl;
    }
}
