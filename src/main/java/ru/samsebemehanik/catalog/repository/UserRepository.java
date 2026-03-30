package ru.samsebemehanik.catalog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.samsebemehanik.catalog.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
