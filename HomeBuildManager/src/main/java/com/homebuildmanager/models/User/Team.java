package com.homebuildmanager.models.User;

import com.homebuildmanager.databaseConnection.HibernateUtil;
import com.homebuildmanager.models.HouseProject.HouseProject;
import com.homebuildmanager.models.Order.HouseOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.jdbc.Work;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    private Manager manager;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Worker> workers = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<HouseOrder> orders = new ArrayList<>();


    public int countSize() {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT COUNT(w) FROM Worker w WHERE w.team.id = :id";
            Query query = session.createQuery(hql, Long.class);
            query.setParameter("id", this.id);

            Long teamSize = (long) query.getSingleResult();

            return teamSize.intValue()+1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
