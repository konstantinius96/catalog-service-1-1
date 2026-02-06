package ru.samsebemehanik.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samsebemehanik.catalog.domain.favourite.Favourite;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    List<Favourite> findAllByUserId(Long userId);
}
