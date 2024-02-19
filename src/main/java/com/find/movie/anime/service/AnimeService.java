package com.find.movie.anime.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.find.movie.anime.model.Anime;
import com.find.movie.anime.repository.AnimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnimeService {

    private final AnimeRepository animeRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeService.class);

    @Autowired
    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public Anime getAnimeData(String animeName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.jikan.moe/v4/anime";
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode data = root.path("data");

            for (JsonNode node : data) {
                String title = node.path("title").asText().toLowerCase();
                String synopsis = node.path("synopsis").asText();

                if (title.equals(animeName.toLowerCase())) {
                    Anime anime = new Anime(title, synopsis);
                    return animeRepository.save(anime);
                }
            }
        } catch (Exception e) {
            LOGGER.error("context", e);
        }
        return null;
    }
}