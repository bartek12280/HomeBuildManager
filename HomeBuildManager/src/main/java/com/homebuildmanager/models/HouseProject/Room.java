package com.homebuildmanager.models.HouseProject;

import com.homebuildmanager.databaseConnection.HibernateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;


import javax.persistence.*;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(name = "area", nullable = false)
    private int area;

    @Column(name = "color", nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "house_project_id", nullable = false)
    private HouseProject houseProject;


    public static boolean updateRoom(Long roomId, RoomType newRoomType, int newArea, String newColor) {
        Transaction transaction = null;

        if (newRoomType == null) {
            return false;
        }

        if (newArea <= 0) {
            return false;
        }

        if (newColor == null || newColor.trim().isEmpty()) {
            return false;
        }

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            String hql = "UPDATE Room r SET r.roomType = :roomType, r.area = :area, r.color = :color WHERE r.id = :roomId";
            Query query = session.createQuery(hql);
            query.setParameter("roomType", newRoomType);
            query.setParameter("area", newArea);
            query.setParameter("color", newColor);
            query.setParameter("roomId", roomId);

            int result = query.executeUpdate();

            transaction.commit();
            return result > 0;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

}
