package com.find.movie.anime.service;

import com.find.movie.anime.AnimeService;
import com.find.movie.anime.config.CacheConfig;
import com.find.movie.anime.config.RequestCount;
import com.find.movie.anime.model.Anime;
import com.find.movie.anime.repository.AnimeRepository;
import com.find.movie.anime.repository.GenreRepository;
import com.find.movie.anime.repository.TitlesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private TitlesRepository titlesRepository;
    @Mock
    private RequestCount requestCount;
    @Mock
    private CacheConfig animeCache;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private AnimeService animeService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetAllAnime() {
        // Arrange
        List<Anime> animeList = new ArrayList<>();
        when(animeRepository.findAll()).thenReturn(animeList);

        // Act
        List<Anime> result = animeService.getAllAnime();

        // Assert
        assertEquals(animeList, result);
        verify(animeRepository, times(1)).findAll();
        verify(requestCount, times(1)).increment();
    }


    // Write similar tests for other methods in AnimeService...
}


