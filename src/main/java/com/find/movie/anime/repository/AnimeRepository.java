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

    //@Query("SELECT a FROM Anime a JOIN a.genres g WHERE g.name = :genreName")
    @Query(value = "SELECT a.* FROM Anime a JOIN anime_genres ag ON a.id = ag.anime_id JOIN genre g ON ag.genre_id = g.id WHERE g.name = :genreName", nativeQuery = true)

    List<Anime> findAnimeByGenre(@Param("genreName") String genreName);
}

