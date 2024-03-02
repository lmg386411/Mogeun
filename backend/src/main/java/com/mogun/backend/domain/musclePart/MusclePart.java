package com.mogun.backend.domain.musclePart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusclePart {

    @Id
    @GeneratedValue
    @Column(name = "part_key")
    private int partKey;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "image_path")
    private String imagePath;

}
