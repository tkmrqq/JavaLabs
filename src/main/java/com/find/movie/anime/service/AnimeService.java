package com.find.movie.anime.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.find.movie.anime.model.Anime;
import com.find.movie.anime.model.Genre;
import com.find.movie.anime.repository.AnimeRepository;
import com.find.movie.anime.repository.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final GenreRepository genreRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeService.class);

    @Autowired
    public AnimeService(AnimeRepository animeRepository, GenreRepository genreRepository) {
        this.animeRepository = animeRepository;
        this.genreRepository = genreRepository;
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
                String title = node.path("title").asText();
                if (title.equalsIgnoreCase(animeName)) {
                    return processAnimeNode(node);
                }
            }
        } catch (Exception e) {
            LOGGER.error("context", e);
        }
        return null;
    }

    private Anime processAnimeNode(JsonNode node) {
        String title = node.path("title").asText();
        String synopsis = node.path("synopsis").asText();
        String source = node.path("source").asText();
        String type = node.path("type").asText();
        int episodes = node.path("episodes").asInt();
        double score = node.path("score").asDouble();
        String duration = node.path("duration").asText();
        JsonNode images = node.path("images");
        JsonNode jpg = images.path("jpg");
        String picUrl = jpg.path("large_image_url").asText();

        List<Genre> genres = new ArrayList<Genre>();

        ArrayNode genreArray = (ArrayNode) node.path("genres");
        for (JsonNode genreNode : genreArray){
            String genreName = genreNode.path("name").asText();

            Genre genre = genreRepository.findByName(genreName);

            if(genre == null) {
                genre = new Genre();
                genre.setName(genreName);
                genreRepository.save(genre);
            }
            if(!genres.contains(genre)) {
                genres.add(genre);
            }
        }

        Anime anime = new Anime(title, synopsis, source, type, episodes, score, picUrl, duration, genres);
        return animeRepository.save(anime);
    }
}