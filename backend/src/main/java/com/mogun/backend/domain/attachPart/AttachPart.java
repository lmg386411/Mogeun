package com.mogun.backend.domain.attachPart;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.musclePart.MusclePart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttachPart implements Serializable {

    @Id
    @Column(name = "attach_part_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attachPartKey;

    @ManyToOne
    @JoinColumn(name = "exec_key")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "part_key")
    private MusclePart musclePart;

    @Column(name = "attach_direction")
    private char attachDirection;

    @Column(name = "muscle_category")
    private char muscleCategory;
}
