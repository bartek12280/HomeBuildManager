package com.homebuildmanager.models.Order;

import com.homebuildmanager.databaseConnection.HibernateUtil;
import com.homebuildmanager.models.HouseProject.HouseProject;
import com.homebuildmanager.models.User.Client;
import com.homebuildmanager.models.User.Team;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "estimated_completion_time")
    private double estimatedCompletionTime;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "house_project_id", nullable = false)
    private HouseProject houseProject;

    public void setEstimatedCompletionTime() {
        estimatedCompletionTime = this.calculateEstimatedCompletionTime();
    }

    public int calculateEstimatedCompletionTime() {
        return 12 * (houseProject.getArea() / (25 * team.countSize()));
    }

    public void setTeam() {
        this.team = assignTeam();
    }

    public Team assignTeam() {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT t FROM Team t LEFT JOIN FETCH t.orders";
            Query query = session.createQuery(hql, Team.class);
            List<Team> teams = query.getResultList();

            Team teamWithLeastOrders = teams.stream()
                    .min((team1, team2) -> Integer.compare(team1.getOrders().size(), team2.getOrders().size()))
                    .orElse(null);

            if (teamWithLeastOrders != null) {
                teamWithLeastOrders.getOrders().add(this);
                this.setTeam(teamWithLeastOrders);

                return teamWithLeastOrders;
            } else {
                throw new IllegalStateException("No teams available for assignment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(HouseOrder houseOrder) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(houseOrder);
            transaction.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<HouseOrder> fetchClientOrders(Client client) {
        List<HouseOrder> orders = null;
        try (Session session = HibernateUtil.getSession()) {
            Query query = session.createQuery(
                    "SELECT ho FROM HouseOrder ho " +
                            "JOIN FETCH ho.client " +
                            "JOIN FETCH ho.team " +
                            "JOIN FETCH ho.houseProject " +
                            "WHERE ho.client.id = :clientId", HouseOrder.class);
            query.setParameter("clientId", client.getId());
            orders = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
