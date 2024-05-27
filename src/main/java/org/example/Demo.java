package org.example;

import java.sql.SQLException;

public class Demo {
    public static void main(String[] args) {
        try {
            ClientService clientService = new ClientService();
            clientService.deleteById(11L);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
