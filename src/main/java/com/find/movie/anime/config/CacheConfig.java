package com.find.movie.anime.config;

import com.find.movie.anime.model.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CacheConfig {
    private final ConcurrentMap<Long, Anime> cache = new ConcurrentHashMap<>();

    public void put(Long id, Anime anime) {
        cache.put(id, anime);
    }

    public Anime get(Long id) {
        return cache.get(id);
    }

    public boolean contains(Long id) {
        return cache.containsKey(id);
    }

    public void remove(Long id) {
        cache.remove(id);
    }

    public void clear() {
        cache.clear();
    }

    public List<Anime> getAllAnime() {
        return new ArrayList<>(cache.values());
    }

    public void putAllAnime(List<Anime> animeList) {
        animeList.forEach(character -> cache.put(character.getId(), character));
    }
}