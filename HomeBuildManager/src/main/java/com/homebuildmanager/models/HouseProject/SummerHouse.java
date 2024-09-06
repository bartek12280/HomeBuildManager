package com.homebuildmanager.models.HouseProject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "summer_house")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummerHouse extends HouseProject {

    @ElementCollection
    @CollectionTable(name = "summer_house_equipment", joinColumns = @JoinColumn(name = "house_project_id"))
    @Fetch(FetchMode.JOIN)
    @Column(name = "equipment")
    private List<String> equipment;
}
