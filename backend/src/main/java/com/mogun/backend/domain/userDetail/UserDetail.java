package com.mogun.backend.domain.userDetail;

import com.mogun.backend.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetail {

    @Id
    @Column(name = "user_key")
    private int userKey;

    @Column
    private float weight;

    @Column
    private float height;

    @Column(name = "muscle_mass")
    private float muscleMass;

    @Column(name = "body_fat")
    private float bodyFat;

    @MapsId // @Id로 지정한 컬럼을 @OneToOne 혹은 @ManyToOne 관계를 매핑
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_key")
    private User user;

    @Builder
    public UserDetail(User user, float weight, float height, float muscleMass, float bodyFat) {
        this.userKey = user.getUserKey();
        this.user = user;
        this.weight = weight;
        this.height = height;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setMuscleMass(float muscleMass) {
        this.muscleMass = muscleMass;
    }

    public void setBodyFat(float bodyFat) {
        this.bodyFat = bodyFat;
    }
}
