package ru.itis;

import ru.itis.models.Account;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Properties properties = new Properties();

        try {
            properties.load(new FileReader("resources/application.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        DataSource dataSource = new SimpleDataSource(properties);

        AccountsRepository accountsRepository = new AccountsRepositoryJdbcImpl(dataSource);

        Scanner scanner = new Scanner(System.in);
        System.out.println(accountsRepository.findAllByFirstNameOrLastNameLike(""));
        System.out.println("Everything is OK");
    }
}
