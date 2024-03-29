package com.find.movie.anime.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Anime {    //anime object to work with JPA and etc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String source;
    private String type;
    private int episodes;
    private double score;
    private String duration;
    private String picUrl;

    @ManyToMany
    @JoinTable(name = "anime_genres", joinColumns = @JoinColumn(name = "anime_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @OneToMany
    private List<Titles> titlesList;

    public Anime() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Titles> getTitlesList() {
        return titlesList;
    }

    public void setTitlesList(List<Titles> titlesList) {
        this.titlesList = titlesList;
    }

    public Anime(String name, String description, int episodes, double score) {
        this.name = name;
        this.description = description;
        this.episodes = episodes;
        this.score = score;
    }
}
