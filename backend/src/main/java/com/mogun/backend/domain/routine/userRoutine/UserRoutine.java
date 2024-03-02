package com.mogun.backend.domain.routine.userRoutine;

import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class UserRoutine {

    @Id
    @Column(name = "routine_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routineKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "routine_name")
    private String routineName;

    @Column
    private int count;

    @Column(name = "is_deleted")
    private char isDeleted;

    public void setIsDeleted(char isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }
}
