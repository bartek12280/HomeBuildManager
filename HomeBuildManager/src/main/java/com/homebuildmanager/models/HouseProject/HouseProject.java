package com.homebuildmanager.models.HouseProject;

import com.homebuildmanager.databaseConnection.HibernateUtil;
import com.homebuildmanager.models.Order.HouseOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "house_projects")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HouseProject {

    private static final double BASE_PRICE = 150000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "area", nullable = false)
    private int area;

    @Column(name = "construction_cost", nullable = false)
    private double constructionCost;

    @OneToMany(mappedBy = "houseProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    @OneToOne(mappedBy = "houseProject", cascade = CascadeType.ALL)
    private BrickHouse brickHouse;

    @OneToOne(mappedBy = "houseProject", cascade = CascadeType.ALL)
    private WoodenHouse woodenHouse;

    @OneToMany(mappedBy = "houseProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HouseOrder> orders = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void checkXor() {
        if ((brickHouse != null && woodenHouse != null) || (brickHouse == null && woodenHouse == null)) {
            throw new IllegalStateException("A HouseProject must be either a BrickHouse or a WoodenHouse, but not both.");
        }
    }

    public int getNumberOfRooms() {
        return (rooms != null ? rooms.size() : 0);
    }

    public static LinkedHashSet findAllHouseProjects() {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT h FROM HouseProject h LEFT JOIN FETCH h.rooms ORDER BY h.id ASC";
            return new LinkedHashSet<>(session.createQuery(hql, HouseProject.class).getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
