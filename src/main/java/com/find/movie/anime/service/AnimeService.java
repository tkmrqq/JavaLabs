package com.find.movie.anime.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.find.movie.anime.config.CacheConfig;
import com.find.movie.anime.model.Anime;
import com.find.movie.anime.model.Genre;
import com.find.movie.anime.model.Titles;
import com.find.movie.anime.repository.AnimeRepository;
import com.find.movie.anime.repository.GenreRepository;
import com.find.movie.anime.repository.TitlesRepository;
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
    private final TitlesRepository titlesRepository;
    private final CacheConfig animeCache;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnimeService.class);

    @Autowired
    public AnimeService(AnimeRepository animeRepository, GenreRepository genreRepository,
                        TitlesRepository titlesRepository,
                        CacheConfig animeCache) {
        this.animeRepository = animeRepository;
        this.genreRepository = genreRepository;
        this.titlesRepository = titlesRepository;
        this.animeCache = animeCache;
    }

    public List<Anime> getAllAnime() {
        LOGGER.info("Getting all anime from the database.");
        List<Anime> animeList = animeRepository.findAll();
        LOGGER.debug("Retrieved {} anime from the database.", animeList.size());
        if(animeList.size() > animeCache.getAllAnime().size()){
            animeList.forEach(anime -> {
                if(!animeCache.contains(anime.getId())){
                    animeCache.put(anime.getId(), anime);
                }
            });
        }
        return animeCache.getAllAnime();
    }

    public Anime getAnimeData(String animeName) {
        if(animeName != null){
            LOGGER.info("Getting anime data for anime with name: {}", animeName);
        }
        RestTemplate restTemplate = new RestTemplate();
        Anime anime = animeRepository.findByNameIgnoreCase(animeName); //ищем в бд

        if (anime != null) {
            LOGGER.info("Anime found in the database.");
            if (!animeCache.getAllAnime().contains(anime)){
                animeCache.put(anime.getId(), anime);
            }
            return anime;
        }
        LOGGER.info("Anime not found in the database, searching external API.");
        String url = "https://api.jikan.moe/v4/anime";
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode data = root.path("data");

            for (JsonNode node : data) {
                String title = node.path("title").asText();
                if (title.equalsIgnoreCase(animeName)) {
                    return processAnimeNode(node, title);
                }
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while fetching anime data.", e);
        }
        return null;
    }

    private Anime processAnimeNode(JsonNode node, String title) {
        String synopsis = node.path("synopsis").asText();
        String source = node.path("source").asText();
        String type = node.path("type").asText();
        int episodes = node.path("episodes").asInt();
        double score = node.path("score").asDouble();
        String duration = node.path("duration").asText();
        JsonNode images = node.path("images");
        JsonNode jpg = images.path("jpg");
        String picUrl = jpg.path("large_image_url").asText();

        List<Genre> genres = new ArrayList<>();
        List<Titles> titlesList = new ArrayList<>();

        ArrayNode genreArray = (ArrayNode) node.path("genres");
        for (JsonNode genreNode : genreArray) {
            String genreName = genreNode.path("name").asText();

            Genre genre = genreRepository.findByName(genreName);

            if (genre == null) {
                genre = new Genre();
                genre.setName(genreName);
                genreRepository.save(genre);
            }
            if (!genres.contains(genre)) {
                genres.add(genre);
            }
        }

        ArrayNode titlesArray = (ArrayNode) node.path("titles");
        for (JsonNode titlesNode : titlesArray) {
            String titleName = titlesNode.path("title").asText();
            String typeName = titlesNode.path("type").asText();

            Titles titles = new Titles(titleName, typeName);
            titlesRepository.save(titles);
            titlesList.add(titles);
        }
        Anime anime = new Anime(title, synopsis, episodes, score);
        anime.setSource(source);
        anime.setType(type);
        anime.setPicUrl(picUrl);
        anime.setDuration(duration);
        anime.setGenres(genres);
        anime.setTitlesList(titlesList);
        return animeRepository.save(anime);
    }

    public Anime createAnime(Anime anime) {
        LOGGER.info("Creating anime object: {}", anime);
        Anime animeTemp = animeRepository.findByNameIgnoreCase(anime.getName());
        if (animeTemp != null) {
            return animeTemp;
        } else {
            for (Genre genre : anime.getGenres()) {
                if (genre.getId() == null) {
                    genreRepository.save(genre);
                }
            }
            for (Titles title : anime.getTitlesList()) {
                if (title.getId() == null) {
                    titlesRepository.save(title);
                }
            }
            return animeRepository.save(anime);
        }
    }


    public void deleteAnime(String title) {
        if(title != null){
            LOGGER.info("Delete anime with name: {}", title);
        }
        Anime animeTemp = animeRepository.findByNameIgnoreCase(title);
        if (animeTemp != null) {
            animeCache.remove(animeTemp.getId());
            animeRepository.delete(animeTemp);
        }
    }

    public Anime patchAnime(String title, int episodes) {
        if(title != null){
            LOGGER.info("Changing episodes in title: {}", title);
        }
        Anime animeTemp = animeRepository.findByNameIgnoreCase(title);
        if (animeTemp != null) {
            animeTemp.setEpisodes(episodes);
            return animeRepository.save(animeTemp);
        }
        return null;
    }

    public Anime putAnime(String title, Anime anime) {
        if(title != null){
            LOGGER.info("Updating anime data for anime with name: {}", title);
        }
        Anime animeTemp = animeRepository.findByNameIgnoreCase(title);
        if (animeTemp != null) {
            anime.setId(animeTemp.getId());
            return animeRepository.save(anime);
        }
        return null;
    }

    public List<Anime> getAnimeByGenre(String genreName) {
        LOGGER.info("Output anime with genre: {}", genreName);
        return animeRepository.findAnimeByGenre(genreName);
    }
}