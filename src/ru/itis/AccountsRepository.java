package ru.itis;

import ru.itis.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository {
    List<Account> findAll();
    List<Account> findAllByFirstName(String firstName);

    Optional<Account> findByID(Integer searchId);
    List<Account> findAllByFirstNameOrLastNameLike(String searchNameLike);
    void save(Account account);
    void update(Account account);
}
