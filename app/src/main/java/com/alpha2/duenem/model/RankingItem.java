package com.alpha2.duenem.model;

import java.io.Serializable;

public class RankingItem implements Serializable {

    private User user;
    private int points;

    public RankingItem() {
    }

    public RankingItem(User user, int points) {
        this.user = user;
        this.points = points;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
