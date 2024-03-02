package com.mogun.backend.domain.routine.userRoutinePlan;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoutinePlan {

    @Id
    @Column(name = "routine_plan_key")
    @GeneratedValue
    private int routinePlanKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    User user;

    @ManyToOne
    @JoinColumn(name = "routine_key")
    UserRoutine userRoutine;

    @ManyToOne
    @JoinColumn(name = "exec_key")
    private Exercise exercise;

    @Column(name = "set_amount")
    private int setAmount;

}
