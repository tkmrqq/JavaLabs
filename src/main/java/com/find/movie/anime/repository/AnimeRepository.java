package com.find.movie.anime.repository;

import com.find.movie.anime.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Anime findByNameIgnoreCase(String name);

    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g.name = :genreName")
    List<Anime> findAnimesByGenre(@Param("genreName") String genreName);
}

