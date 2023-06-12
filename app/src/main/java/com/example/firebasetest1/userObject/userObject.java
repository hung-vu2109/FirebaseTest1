package com.example.firebasetest1.userObject;

public class userObject {
    String fullName;
    int age;
    favouriteActivities favAc;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public favouriteActivities getFavAc() {
        return favAc;
    }

    public void setFavAc(favouriteActivities favAc) {
        this.favAc = favAc;
    }

    public userObject(String fullName, int age, favouriteActivities favAc) {
        this.fullName = fullName;
        this.age = age;
        this.favAc = favAc;
    }
}
