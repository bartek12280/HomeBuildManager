package com.homebuildmanager;

import com.homebuildmanager.GUI.MainGUI;
import com.homebuildmanager.databaseConnection.HibernateUtil;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HibernateUtil.getSession();
                MainGUI mainGUI = new MainGUI();
                mainGUI.setVisible(true);
            }
        });
    }
}

