package ru.itis;

import ru.itis.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository {
    List<Account> findAll();
    List<Account> findAllByFirstName(String firstName);

    Optional<Account> findByID(Integer Id);
    List<Account> findAllByFirstNameOrLastNameLike(String NameLike);
    void save(Account account);
    void update(Account account);
}
