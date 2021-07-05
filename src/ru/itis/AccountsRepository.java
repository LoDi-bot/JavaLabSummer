package ru.itis;

import ru.itis.models.Account;

import java.util.List;

public interface AccountsRepository {
    List<Account> findAll();
    List<Account> findAllByFirstName(String firstName);

    Account findByID(Integer searchId);
    List<Account> findAllByFirstNameOrLastNameLike(String searchNameLike);
    void save(Account account);
}
