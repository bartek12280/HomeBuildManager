package com.homebuildmanager.GUI;

import com.homebuildmanager.databaseConnection.HibernateUtil;
import com.homebuildmanager.models.HouseProject.HouseProject;
import com.homebuildmanager.models.HouseProject.Room;
import com.homebuildmanager.models.HouseProject.RoomType;
import com.homebuildmanager.models.Order.HouseOrder;
import com.homebuildmanager.models.Order.OrderStatus;
import com.homebuildmanager.models.User.Client;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static com.homebuildmanager.models.HouseProject.Room.updateRoom;
import static com.homebuildmanager.models.Order.HouseOrder.fetchClientOrders;

public class MainGUI extends JFrame {


    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Client loggedClient;
    private HouseProject selectedHouseProject;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainGUI() {
        setTitle("Home Build Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(createLoginPanel(), "LoginScreen");
        setContentPane(mainPanel);
        showLoginScreen();
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        loginField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("OK");
        registerButton = new JButton("Rejestracja");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Login"), gbc);

        gbc.gridx = 1;
        panel.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Hasło"), gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(registerButton, gbc);

        gbc.gridx = 1;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginField.getText();
                String password = new String(passwordField.getPassword());
                handleLogin(username, password);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainGUI.this, "Rejestracja nowego użytkownika");
            }
        });

        return panel;
    }

    private JPanel createMainScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton houseProjectsButton = new JButton("Projekty domów");
        JButton myOrdersButton = new JButton("Moje zamówienia");
        JButton adminPanel2Button = new JButton("Panel administratora");
        JButton logoutButton = new JButton("Wyloguj się");

        panel.add(houseProjectsButton, gbc);
        panel.add(myOrdersButton, gbc);
        panel.add(adminPanel2Button, gbc);
        panel.add(logoutButton, gbc);

        houseProjectsButton.addActionListener(e -> showHouseProjectsScreen());
        myOrdersButton.addActionListener(e -> showMyOrdersScreen());
        adminPanel2Button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Panel administratora"));
        logoutButton.addActionListener(e -> {
            showLoginScreen();
        });

        return panel;
    }

    private JPanel createHouseProjectsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        LinkedHashSet<HouseProject> houseProjects = HouseProject.findAllHouseProjects();

        for (HouseProject project : houseProjects) {
            JPanel projectPanel = new JPanel(new BorderLayout());

            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + project.getName() + ".jpg"));

            Image scaledImage = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));

            JLabel titleLabel = new JLabel(project.getName());
            JLabel descriptionLabel = new JLabel("Powierzchnia domu: " + project.getArea());

            JButton selectButton = new JButton("Wybierz");
            selectButton.addActionListener(e -> selectedHouseProject = project);
            selectButton.addActionListener(e -> showOrderFormScreen());

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.add(titleLabel);
            textPanel.add(descriptionLabel);
            textPanel.add(selectButton);

            projectPanel.add(imageLabel, BorderLayout.WEST);
            projectPanel.add(textPanel, BorderLayout.CENTER);

            panel.add(projectPanel);
            panel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createOrderFormScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Ulica"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextField streetField = new JTextField(15);
        panel.add(streetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Miasto"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        JTextField cityField = new JTextField(10);
        panel.add(cityField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Kod pocztowy"), gbc);

        gbc.gridx = 3;
        JTextField postalCodeField = new JTextField(6);
        panel.add(postalCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Numer Telefonu"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JTextField phoneField = new JTextField(10);
        phoneField.setText(loggedClient.getPhoneNumber());
        panel.add(phoneField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JButton confirmButton = new JButton("Zatwierdź");
        panel.add(confirmButton, gbc);

        gbc.gridx = 3;
        JButton cancelButton = new JButton("Anuluj");
        panel.add(cancelButton, gbc);

        gbc.gridx = 0;
        JButton showRoomsButton = new JButton("Lista pokoi");
        panel.add(showRoomsButton, gbc);

        confirmButton.addActionListener(e -> {
            if (validateFields(streetField, cityField, postalCodeField, phoneField)) {
                showOrderSummaryScreen(streetField.getText(), cityField.getText(), postalCodeField.getText(), phoneField.getText());
            }
        });
        showRoomsButton.addActionListener(e -> showRoomListScreen());
        cancelButton.addActionListener(e -> showMainScreen());

        return panel;
    }

    private JPanel createRoomListScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<Room> rooms = selectedHouseProject.getRooms();

        for (Room room : rooms) {
            JPanel roomPanel = new JPanel(new BorderLayout());

            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + "pokoj" + room.getId() + ".jpg"));
            System.out.println(room.getId());
            Image scaledImage = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));

            JLabel titleLabel = new JLabel(room.getRoomType().name());
            JLabel descriptionLabel = new JLabel("Powierzchnia: " + room.getArea() + "m², Kolor: " + room.getColor());

            JButton editButton = new JButton("Edytuj");
            editButton.addActionListener(e -> showRoomEditorScreen(room));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.add(titleLabel);
            textPanel.add(descriptionLabel);

            roomPanel.add(imageLabel, BorderLayout.WEST);
            roomPanel.add(textPanel, BorderLayout.CENTER);
            roomPanel.add(editButton, BorderLayout.EAST);

            panel.add(roomPanel);
            panel.add(Box.createVerticalStrut(10));
        }

        JButton exitButton = new JButton("Wyjdź");
        exitButton.addActionListener(e -> showOrderFormScreen());
        panel.add(exitButton);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createRoomEditorScreen(Room room) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Typ pokoju"), gbc);

        gbc.gridx = 1;
        JTextField roomTypeField = new JTextField(room.getRoomType().name(), 10);
        panel.add(roomTypeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Powierzchnia"), gbc);

        gbc.gridx = 1;
        JTextField roomAreaField = new JTextField(String.valueOf(room.getArea()), 10);
        panel.add(roomAreaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Kolor"), gbc);

        gbc.gridx = 1;
        JTextField roomColorField = new JTextField(room.getColor(), 10);
        panel.add(roomColorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JButton saveButton = new JButton("Zapisz");
        panel.add(saveButton, gbc);

        gbc.gridx = 1;
        JButton cancelButton = new JButton("Anuluj");
        panel.add(cancelButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                if (updateRoom(room.getId(), RoomType.valueOf(roomTypeField.getText()), Integer.parseInt(roomAreaField.getText()), roomColorField.getText()))
                    JOptionPane.showMessageDialog(this, "Zmiany zapisane.");
                else {
                    JOptionPane.showMessageDialog(this, "Nie dokonano zmian.");
                    showRoomListScreen();
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, "wprowadzono nieprawidłowe dane.");
                showRoomListScreen();
            }
        });
        cancelButton.addActionListener(e -> showRoomListScreen());

        return panel;
    }

    private JPanel createOrderSummaryScreen(String street, String city, String postalCode, String phoneNumber) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        HouseOrder houseOrder = new HouseOrder();
        houseOrder.setOrderDate(LocalDate.now());
        houseOrder.setOrderStatus(OrderStatus.NEW);
        houseOrder.setAddress(city + ", " + postalCode + ", " + street);
        houseOrder.setClient(loggedClient);
        houseOrder.setHouseProject(selectedHouseProject);
        houseOrder.setTeam();
        houseOrder.setEstimatedCompletionTime();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Dane Klienta:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(new JLabel(loggedClient.getName() + " " + loggedClient.getSurname()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Ulica:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(new JLabel(street), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Miasto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(new JLabel(city), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Kod pocztowy:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(new JLabel(postalCode), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Numer telefonu:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(new JLabel(phoneNumber), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Szacowany czas realizacji:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(new JLabel((int) houseOrder.getEstimatedCompletionTime() + " miesiecy"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton confirmButton = new JButton("Zatwierdź");
        panel.add(confirmButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        JButton cancelButton = new JButton("Anuluj");
        panel.add(cancelButton, gbc);

        confirmButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Zamówienie zostało złożone!");
            HouseOrder.save(houseOrder);

            showMyOrdersScreen();
        });
        cancelButton.addActionListener(e -> showOrderFormScreen());

        return panel;
    }

    private JPanel createMyOrdersScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<HouseOrder> houseOrders = fetchClientOrders(loggedClient);

        if (houseOrders.isEmpty()) {
            JLabel noOrdersLabel = new JLabel("Brak zamówień");
            panel.add(noOrdersLabel);
        } else {
            for (HouseOrder order : houseOrders) {
                JPanel orderPanel = new JPanel(new BorderLayout());

                JLabel imageLabel = new JLabel();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + order.getHouseProject().getName() + ".jpg"));
                Image scaledImage = imageIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));

                JLabel projectLabel = new JLabel("Projekt: " + order.getHouseProject().getName());
                JLabel statusLabel = new JLabel("Status zamówienia: " + order.getOrderStatus());
                JLabel dateLabel = new JLabel("Data zamówienia: " + order.getOrderDate());
                JLabel addressLabel = new JLabel("Adres budowy: " + order.getAddress());

                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.add(projectLabel);
                textPanel.add(statusLabel);
                textPanel.add(dateLabel);
                textPanel.add(addressLabel);

                orderPanel.add(imageLabel, BorderLayout.WEST);
                orderPanel.add(textPanel, BorderLayout.CENTER);

                panel.add(orderPanel);
                panel.add(Box.createVerticalStrut(10));
            }
        }
        JButton backButton = new JButton("Powrót do menu głównego");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.addActionListener(e -> showMainScreen());

        panel.add(Box.createVerticalStrut(20));
        panel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private void showLoginScreen() {
        cardLayout.show(mainPanel, "LoginScreen");
    }

    private void showMainScreen() {
        if (mainPanel.getComponentCount() == 1) {
            mainPanel.add(createMainScreen(), "MainScreen");
        }

        cardLayout.show(mainPanel, "MainScreen");
    }

    private void showHouseProjectsScreen() {
        mainPanel.add(createHouseProjectsScreen(), "HouseProjectsScreen");
        cardLayout.show(mainPanel, "HouseProjectsScreen");
    }

    private void showOrderFormScreen() {
        mainPanel.add(createOrderFormScreen(), "OrderFormScreen");
        cardLayout.show(mainPanel, "OrderFormScreen");
    }

    private void showRoomListScreen() {
        mainPanel.add(createRoomListScreen(), "RoomListScreen");
        cardLayout.show(mainPanel, "RoomListScreen");
    }

    private void showRoomEditorScreen(Room room) {
        mainPanel.add(createRoomEditorScreen(room), "RoomEditorScreen");
        cardLayout.show(mainPanel, "RoomEditorScreen");
    }

    private void showOrderSummaryScreen(String street, String city, String postalCode, String phoneNumber) {
        mainPanel.add(createOrderSummaryScreen(street, city, postalCode, phoneNumber), "OrderSummaryScreen");
        cardLayout.show(mainPanel, "OrderSummaryScreen");
    }

    private void showMyOrdersScreen() {
        mainPanel.add(createMyOrdersScreen(), "MyOrdersScreen");
        cardLayout.show(mainPanel, "MyOrdersScreen");
    }

    public static Client authenticateUser(String username, String password) {
        Client client = null;
        try (Session session = HibernateUtil.getSession()) {
            Query<Client> query = session.createQuery("FROM Client WHERE username = :username", Client.class);
            query.setParameter("username", username);
            client = query.uniqueResult();

            if (client != null && client.getPassword().equals(password)) {
                return client;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleLogin(String username, String password) {
        Client client = authenticateUser(username, password);
        loggedClient = client;
        if (client != null) {
            JOptionPane.showMessageDialog(this, "Zalogowano pomyślnie!");
            showMainScreen();
        } else {
            JOptionPane.showMessageDialog(this, "Błędny login lub hasło");
        }
    }

    private boolean validateFields(JTextField streetField, JTextField cityField, JTextField postalCodeField, JTextField phoneField) {
        if (streetField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pole 'Ulica' nie może być puste", "Błąd walidacji", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cityField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pole 'Miasto' nie może być puste", "Błąd walidacji", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!postalCodeField.getText().matches("\\d{2}-\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Pole 'Kod pocztowy' musi mieć format xx-xxx", "Błąd walidacji", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!phoneField.getText().matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this, "Pole 'Numer Telefonu' musi składać się z 9 cyfr", "Błąd walidacji", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
