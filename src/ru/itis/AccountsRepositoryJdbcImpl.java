package ru.itis;

import ru.itis.models.Account;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AccountsRepositoryJdbcImpl implements AccountsRepository {

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from account order by id";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_FIRST_NAME = "select * from account where first_name = ?";

    //language=SQL
    private static final String SQL_SELECT_ROW_BY_ID = "select * from account where id = ?";

    //language=SQL
    private static final String SQL_INSERT_INTO_ACCOUNT = "insert into account(id, first_name, last_name, age) values(?,?,?,?)";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_NAME_LIKE = "select * from account where first_name like ? or last_name like ?";

    private final DataSource dataSource;

    public AccountsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final Function<ResultSet, Account> map = row -> {
        try {
            int id = row.getInt("id");
            String firstName = row.getString("first_name");
            String lastName = row.getString("last_name");
            int age = row.getInt("age");

            return new Account(id, firstName, lastName, age);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    };

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try {
            Connection connection;

            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            Statement statement;

            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            ResultSet rows;

            try {
                rows = statement.executeQuery(SQL_SELECT_ALL);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return accounts;
    }

    @Override
    public List<Account> findAllByFirstName(String searchFirstName) {
        List<Account> accounts = new ArrayList<>();
        try {
            Connection connection;

            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            PreparedStatement statement;

            try {
                statement = connection.prepareStatement(SQL_SELECT_ALL_BY_FIRST_NAME);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            statement.setString(1, searchFirstName);

            ResultSet rows;

            try {
                rows = statement.executeQuery();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return accounts;
    }

    @Override
    public Account findByID(Integer searchId) {
        Account account = null;
        try {
            Connection connection;

            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            PreparedStatement statement;

            try {
                statement = connection.prepareStatement(SQL_SELECT_ROW_BY_ID);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            statement.setInt(1, searchId);

            ResultSet rows;

            try {
                rows = statement.executeQuery();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            if (rows.next()) {
                account = map.apply(rows);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return account;
    }

    @Override
    public List<Account> findAllByFirstNameOrLastNameLike(String searchNameLike) {
        searchNameLike = "%" + searchNameLike + "%";
        List<Account> accounts = new ArrayList<>();
        try {
            Connection connection;

            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            PreparedStatement statement;

            try {
                statement = connection.prepareStatement(SQL_SELECT_ALL_BY_NAME_LIKE);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            statement.setString(1, searchNameLike);
            statement.setString(2, searchNameLike);

            ResultSet rows;

            try {
                rows = statement.executeQuery();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return accounts;
    }

    @Override
    public void save(Account account) {
        try {
            Connection connection;

            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            PreparedStatement statement;

            try {
                statement = connection.prepareStatement(SQL_INSERT_INTO_ACCOUNT);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

            statement.setInt(1, account.getId());
            statement.setString(2, account.getFirstName());
            statement.setString(3, account.getLastName());
            statement.setInt(4, account.getAge());

            ResultSet rows;

            try {
                statement.execute();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
