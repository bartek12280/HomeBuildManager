package com.homebuildmanager.models.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private WorkerSpecialization specialization;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
