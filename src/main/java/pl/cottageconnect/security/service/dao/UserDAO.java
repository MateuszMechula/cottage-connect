package pl.cottageconnect.security.service.dao;

import pl.cottageconnect.security.domain.User;

import java.util.Optional;

public interface UserDAO {

    Optional<User> findByEmail(String username);

    void save(User user);
}
