package ru.samsebemehanik.catalog.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.samsebemehanik.catalog.domain.user.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);
}
