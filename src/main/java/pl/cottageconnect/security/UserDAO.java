package pl.cottageconnect.security;

import java.util.Optional;

interface UserDAO {

    Optional<User> getUserByUserId(Integer userId);

    Optional<User> findByEmail(String username);

    User save(User user);
}
