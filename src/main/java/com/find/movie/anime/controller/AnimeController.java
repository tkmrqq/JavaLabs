package com.find.movie.anime.controller;

import com.find.movie.anime.dto.AnimeRequest;
import com.find.movie.anime.model.Anime;
import com.find.movie.anime.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AnimeController {
    private final AnimeService animeService;

    @Autowired
    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/anime")
    public Anime saveDesc(@RequestParam("title") String title) {
        return animeService.getAnimeData(title);
    }

    @PostMapping("/anime")
    public Anime saveAnimeDescription(@RequestBody AnimeRequest request) {
        return animeService.getAnimeData(request.getAnimeName());
    }
}


