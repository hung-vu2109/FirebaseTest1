package com.example.firebasetest1.userObject;

public class favouriteActivities {
    boolean likeSinging;
    boolean likeReading;

    public favouriteActivities(boolean likeSinging, boolean likeReading) {
        this.likeSinging = likeSinging;
        this.likeReading = likeReading;
    }

    public boolean isLikeSinging() {
        return likeSinging;
    }

    public void setLikeSinging(boolean likeSinging) {
        this.likeSinging = likeSinging;
    }

    public boolean isLikeReading() {
        return likeReading;
    }

    public void setLikeReading(boolean likeReading) {
        this.likeReading = likeReading;
    }
}
