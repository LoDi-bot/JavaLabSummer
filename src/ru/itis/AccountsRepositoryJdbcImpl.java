package ru.itis;

import ru.itis.models.Account;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AccountsRepositoryJdbcImpl implements AccountsRepository {

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from account order by id";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_FIRST_NAME = "select * from account where first_name = ?";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from account where id = ?";

    //language=SQL
    private static final String SQL_INSERT_INTO_ACCOUNT = "insert into account(id, first_name, last_name, age) values(?,?,?,?)";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_NAME_LIKE = "select * from account where first_name like ? or last_name like ?";

    //language=SQL
    private static final String SQL_UPDATE_BY_ID = "update account set first_name = ?, last_name = ?, age = ? where id = ?";


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
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(SQL_SELECT_ALL)) {
            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return accounts;
    }

    @Override
    public List<Account> findAllByFirstName(String FirstName) {
        List<Account> accounts = new ArrayList<>();
        ResultSet rows = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_BY_FIRST_NAME)) {

            statement.setString(1, FirstName);

            rows = statement.executeQuery();

            while (rows.next()) {
                accounts.add(map.apply(rows));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (rows != null) {
                try {
                    rows.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return accounts;
    }

    @Override
    public Optional<Account> findByID(Integer Id) {
        ResultSet row = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            statement.setInt(1, Id);

            row = statement.executeQuery();

            if (row.next()) {
                return Optional.of(map.apply(row));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (row != null) {
                try {
                    row.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Account> findAllByFirstNameOrLastNameLike(String NameLike) {
        NameLike = "%" + NameLike + "%";
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

            statement.setString(1, NameLike);
            statement.setString(2, NameLike);

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
                statement.executeQuery();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(Account account) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BY_ID)) {

            statement.setString(1, account.getFirstName());
            statement.setString(2, account.getLastName());
            statement.setInt(3, account.getAge());
            statement.setInt(4, account.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Exception in <Update>");
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
