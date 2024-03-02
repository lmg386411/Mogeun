package com.mogun.backend.domain.routine.setDetail;

import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetDetail implements Serializable {

    @Id
    @Column(name = "set_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int setKey;

    @Column(name = "set_number")
    private int setNumber;

    @Column
    private int weight;

    @Column(name = "target_repeat")
    private int targetRepeat;

    @ManyToOne
    @JoinColumn(name = "routine_plan_key")
    private UserRoutinePlan userRoutinePlan;
}
