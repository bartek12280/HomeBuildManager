package com.homebuildmanager.models.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "managers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
