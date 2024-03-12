package com.find.movie.anime.controller;

import com.find.movie.anime.model.Anime;
import com.find.movie.anime.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Anime> saveDesc(@RequestParam("title") String title) {
        Anime anime = animeService.getAnimeData(title);
        if(anime != null){
            return ResponseEntity.ok(anime);
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/post")
    public ResponseEntity<Anime> createAnime(@RequestBody Anime request) {
        Anime anime = animeService.createAnime(request);
        if(anime != null){
            return ResponseEntity.ok(anime);
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAnime(@RequestParam("title") String title) {
        animeService.deleteAnime(title);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/patch")
    public ResponseEntity<Anime> patchAnime(@RequestParam(value = "title") String title,
                           @RequestParam(value = "episodes") int episodes){
        Anime anime = animeService.patchAnime(title, episodes);
        if(anime != null){
            return ResponseEntity.ok(anime);
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/put")
    public ResponseEntity<Anime> putAnime(@RequestParam("title") String title, @RequestBody Anime request){
        Anime anime = animeService.putAnime(title, request);
        return anime != null ? ResponseEntity.ok(anime) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}


