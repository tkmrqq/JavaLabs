package com.find.movie.anime.controller;

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

    @PostMapping("/post")
    public Anime createAnime(@RequestBody Anime request) {
        return animeService.createAnime(request);
    }

    @DeleteMapping("/delete")
    public void deleteAnime(@RequestParam("title") String title) {
        animeService.deleteAnime(title);
    }

    @PatchMapping("/patch")
    public Anime patchAnime(@RequestParam(value = "title") String title,
                           @RequestParam(value = "episodes") int episodes){
        return animeService.patchAnime(title, episodes);
    }

    @PutMapping("/put")
    public Anime putAnime(@RequestParam("title") String title, @RequestBody Anime request){
        return animeService.putAnime(title, request);
    }

}


