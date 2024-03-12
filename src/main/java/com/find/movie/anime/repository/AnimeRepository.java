package com.find.movie.anime.repository;

import com.find.movie.anime.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Anime findByNameIgnoreCase(String name);
}

