package com.mogun.backend.domain.userLog.userWeightLog;

import com.mogun.backend.domain.user.User;
import lombok.Builder;

import javax.persistence.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class UserWeightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_key")
    private Long logKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "weight_before")
    private float weightBefore;

    @Column(name = "weight_after")
    private float weightAfter;

    @Column(name = "changed_time")
    private LocalDateTime changedTime;

    @Builder
    public UserWeightLog(Long logKey, User user, float weightBefore, float weightAfter) {
        this.logKey = logKey;
        this.user = user;
        this.weightBefore = weightBefore;
        this.weightAfter = weightAfter;
        this.changedTime = LocalDateTime.now(Clock.system(ZoneId.of("Asia/Seoul")));
    }
}
