package com.homebuildmanager.models.HouseProject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "brick_houses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrickHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "material_type", nullable = false)
    private String materialType;

    @OneToOne
    @JoinColumn(name = "house_project_id")
    private HouseProject houseProject;
}
