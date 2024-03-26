package com.find.movie.anime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.find.movie.anime.config.CacheConfig;
import com.find.movie.anime.config.RequestCount;
import com.find.movie.anime.exception.AnimeNotFoundException;
import com.find.movie.anime.model.Anime;
import com.find.movie.anime.model.Genre;
import com.find.movie.anime.model.Titles;
import com.find.movie.anime.repository.AnimeRepository;
import com.find.movie.anime.repository.GenreRepository;
import com.find.movie.anime.repository.TitlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final GenreRepository genreRepository;
    private final TitlesRepository titlesRepository;
    private final CacheConfig animeCache;
    private final RequestCount requestCount;

    @Autowired
    public AnimeService(AnimeRepository animeRepository, GenreRepository genreRepository,
                        TitlesRepository titlesRepository,
                        CacheConfig animeCache,
                        RequestCount requestCount) {
        this.animeRepository = animeRepository;
        this.genreRepository = genreRepository;
        this.titlesRepository = titlesRepository;
        this.animeCache = animeCache;
        this.requestCount = requestCount;
    }

    public List<Anime> getAllAnime() {
        requestCount.increment();
        List<Anime> animeList = animeRepository.findAll();
        if (animeList.size() > animeCache.getAllAnime().size()) {
            animeList.forEach(anime -> {
                if (!animeCache.contains(anime.getId())) {
                    animeCache.put(anime.getId(), anime);
                }
            });
        }
        return animeCache.getAllAnime();
    }

    public Anime getAnimeData(String animeName) {
        requestCount.increment();
        RestTemplate restTemplate = new RestTemplate();
        Anime anime = animeRepository.findByNameIgnoreCase(animeName); //ищем в бд

        if (anime != null) {
            if (!animeCache.getAllAnime().contains(anime)) {
                animeCache.put(anime.getId(), anime);
            }
            return anime;
        }
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
            throw new RuntimeException("Failed to fetch anime data from external API");
        }
        throw new AnimeNotFoundException("Anime with name" + animeName + "not found");
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
        Anime animeTemp = animeRepository.findByNameIgnoreCase(title);
        if (animeTemp != null) {
            animeCache.remove(animeTemp.getId());
            animeRepository.delete(animeTemp);
        }
    }

    public Anime patchAnime(String title, int episodes) {
        Anime animeTemp = animeRepository.findByNameIgnoreCase(title);
        if (animeTemp != null) {
            animeTemp.setEpisodes(episodes);
            return animeRepository.save(animeTemp);
        }
        return null;
    }

    public Anime putAnime(String title, Anime anime) {
        Anime animeTemp = animeRepository.findByNameIgnoreCase(title);
        if (animeTemp != null) {
            titlesRepository.saveAll(anime.getTitlesList());
            genreRepository.saveAll(anime.getGenres());
            anime.setId(animeTemp.getId());
            return animeRepository.save(anime);
        }
        return null;
    }


    public List<Anime> getAnimeByGenre(String genreName) {
        return animeRepository.findAnimeByGenre(genreName);
    }

    public List<Anime> createBulkAnime(List<Anime> animeList) {
        return animeList.stream()
                .filter(anime -> animeRepository.findByNameIgnoreCase(anime.getName()) == null)
                .peek(anime -> {
                    anime.getGenres().forEach(genre -> {
                        if (genre.getId() == null) {
                            genreRepository.save(genre);
                        }
                    });
                    anime.getTitlesList().forEach(title -> {
                        if (title.getId() == null) {
                            titlesRepository.save(title);
                        }
                    });
                })
                .map(animeRepository::save)
                .collect(Collectors.toList());
    }

    public int getCount(){
        return requestCount.getCount();
    }

}