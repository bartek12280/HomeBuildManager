package com.homebuildmanager.models.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person {

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee")
    private Worker worker;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee")
    private Manager manager;

    @PrePersist
    @PreUpdate
    private void checkXor() {
        if ((worker == null && manager == null) || (worker != null && manager != null)) {
            throw new IllegalStateException("An Employee must be either a Worker or a Manager, but not both.");
        }
    }
}
