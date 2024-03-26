package com.find.movie.anime.config;

import org.springframework.stereotype.Component;

@Component
public class RequestCount {
    private static RequestCount instance;
    private int count;

    private RequestCount() {
        this.count = 0;
    }

    public synchronized static RequestCount getInstance(){
        if (instance == null) {
            instance = new RequestCount();
        }
        return instance;
    }

    public synchronized void increment(){
        count++;
    }

    public synchronized int getCount(){
        return count;
    }

}
