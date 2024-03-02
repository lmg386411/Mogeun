package com.mogun.backend.domain.user;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userKey;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private char gender;

    @Column(name = "is_leaved")
    private char isLeaved;

    @Column(name = "join_time")
    private LocalDateTime joinTime;

    @Builder
    public User(int userKey, String email, String password, String name, char gender) {
        this.userKey = userKey;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.isLeaved = 'J';
        this.joinTime = LocalDateTime.now();
    }

    public void setIsLeaved(char state) {
        this.isLeaved = state;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Seongmin 닉네임 setter 추가
    public void setName(String name) {
        this.name = name;
    }


    // ---- 비지니스 로직 ---- //
    @Override
    public boolean equals(Object other) {

        User user = (User)other;
        if(!this.email.equals(user.getEmail()))
            return false;

        if(this.userKey != user.getUserKey())
            return false;

        return true;
    }
}
