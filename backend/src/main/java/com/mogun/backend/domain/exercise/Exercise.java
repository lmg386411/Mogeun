package com.mogun.backend.domain.exercise;

import com.mogun.backend.domain.musclePart.MusclePart;
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
public class Exercise {

    @Id
    @GeneratedValue
    @Column(name = "exec_key", columnDefinition = "TINYINT")
    private int execKey;

    @Column
    private String name;

    @Column(name = "eng_name")
    private String engName;

    @OneToOne
    @JoinColumn(name = "part_key")
    private MusclePart mainPart;

    @Column(name = "image_path")
    private String imagePath;
}
