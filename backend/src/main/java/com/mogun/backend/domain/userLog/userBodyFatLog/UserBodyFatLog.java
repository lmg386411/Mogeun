package com.mogun.backend.domain.userLog.userBodyFatLog;

import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBodyFatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_key")
    private Long logKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "body_fat_before")
    private float bodyFatBefore;

    @Column(name = "body_fat_after")
    private float bodyFatAfter;

    @Column(name = "changed_time")
    private LocalDateTime changedTime;

    @Builder
    public UserBodyFatLog(Long logKey, User user, float bodyFatBefore, float bodyFatAfter) {
        this.logKey = logKey;
        this.user = user;
        this.bodyFatBefore = bodyFatBefore;
        this.bodyFatAfter = bodyFatAfter;
        this.changedTime = LocalDateTime.now(Clock.system(ZoneId.of("Asia/Seoul")));
    }
}
