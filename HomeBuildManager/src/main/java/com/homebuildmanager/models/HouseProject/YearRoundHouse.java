package com.homebuildmanager.models.HouseProject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "year_round_house")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearRoundHouse extends HouseProject {

    @Enumerated(EnumType.STRING)
    @Column(name = "insulation_type", nullable = false)
    private InsulationType insulationType;
}
