package com.mogun.backend.domain.userLog.userMuscleMassLog;

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
public class UserMuscleMassLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_key")
    private Long logKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "muscle_mass_before")
    private float muscleMassBefore;

    @Column(name = "muscle_mass_after")
    private float muscleMassAfter;

    @Column(name = "changed_time")
    private LocalDateTime changedTime;

    @Builder
    public UserMuscleMassLog(Long logKey, User user, float muscleMassBefore, float muscleMassAfter) {
        this.logKey = logKey;
        this.user = user;
        this.muscleMassBefore = muscleMassBefore;
        this.muscleMassAfter = muscleMassAfter;
        this.changedTime = LocalDateTime.now(Clock.system(ZoneId.of("Asia/Seoul")));
    }
}
