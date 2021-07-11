package ru.itis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

//        DataSource dataSource = new SimpleDataSource(properties);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.password"));
        config.setDriverClassName(properties.getProperty("db.driver"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.pool-size")));

        DataSource dataSource = new HikariDataSource(config);

        AccountsRepository accountsRepository = new AccountsRepositoryJdbcImpl(dataSource);

        System.out.println(accountsRepository.findAllByFirstNameOrLastNameLike("ев"));
        System.out.println("Everything is OK");
    }
}
