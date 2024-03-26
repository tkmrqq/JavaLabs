package com.find.movie.anime.controller;

import com.find.movie.anime.exception.AnimeNotFoundException;
import com.find.movie.anime.exception.BadRequestException;
import com.find.movie.anime.model.Anime;
import com.find.movie.anime.AnimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@ControllerAdvice
@RestController
public class AnimeController {
    private final AnimeService animeService;

    @Autowired
    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/anime")
    public ResponseEntity<Anime> saveDesc(@RequestParam("title") String title) {
        Anime anime = animeService.getAnimeData(title);
        if (anime != null) {
            return ResponseEntity.ok(anime);
        } else throw new AnimeNotFoundException("Anime not found with title: " + title);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Anime>> getAll() {
        List<Anime> animeList = animeService.getAllAnime();
        return ResponseEntity.ok(animeList);
    }

    @PostMapping("/post")
    public ResponseEntity<Anime> createAnime(@RequestBody Anime request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new BadRequestException("Title cannot be empty");
        }
        Anime anime = animeService.createAnime(request);
        return ResponseEntity.ok(anime);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAnime(@RequestParam("title") String title) {
        try {
            animeService.deleteAnime(title);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting anime: " + e.getMessage());
        }
    }

    @PatchMapping("/patch")
    public ResponseEntity<Anime> patchAnime(@RequestParam(value = "title") String title,
                                            @RequestParam(value = "episodes") int episodes) {
        Anime anime = animeService.patchAnime(title, episodes);
        if (anime != null) {
            return ResponseEntity.ok(anime);
        } else {
            throw new AnimeNotFoundException("Anime not found with title: " + title);
        }
    }

    @PutMapping("/put")
    public ResponseEntity<Anime> putAnime(@RequestParam("title") String title, @RequestBody Anime request) {
        Anime anime = animeService.putAnime(title, request);
        return anime != null ? ResponseEntity.ok(anime) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/genres/{name}")
    public ResponseEntity<List<Anime>> getAnimeByGenre(@PathVariable String name) {
        List<Anime> animeGenreList = animeService.getAnimeByGenre(name);
        if (animeGenreList != null && !animeGenreList.isEmpty()) {
            return ResponseEntity.ok(animeGenreList);
        } else {
            throw new AnimeNotFoundException("not found with genre: " + name);
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Anime>> createBulkAnime(@RequestBody List<Anime> animeList) {
        List<Anime> savedAnimeList = animeService.createBulkAnime(animeList);
        return ResponseEntity.ok(savedAnimeList);
    }

    @GetMapping("/getCount")
    public ResponseEntity<Integer> getCount() {
        return ResponseEntity.ok(animeService.getCount());
    }

}


