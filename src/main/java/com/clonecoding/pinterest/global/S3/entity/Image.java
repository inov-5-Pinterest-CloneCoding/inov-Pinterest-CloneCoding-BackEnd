package com.clonecoding.pinterest.global.S3.entity;

import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
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


//    @OneToMany(mappedBy = "pin", fetch = LAZY, cascade = CascadeType.ALL)
//    @OrderBy("createdDate desc ")
//    private List<Comment> commentList;
//
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();
//
//    @Column
//    private int likeCount;
//
//    public void setLikeCount(int likeCount) {
//        this.likeCount = likeCount;
//    }
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PostLike> likes = new ArrayList<>();

//    public void modifyPin(String pinImageUrl,String title, String content){
//        this.pinImageUrl = pinImageUrl;
//        this.title = title;
//        this.content = content;
//    }