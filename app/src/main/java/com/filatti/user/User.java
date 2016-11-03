package com.filatti.user;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class User {
    static User create(FirebaseUser firebaseUser) {
        User user = new User();
        user.name = firebaseUser.getDisplayName();
        user.email = firebaseUser.getEmail();
        user.provider = firebaseUser.getProviderId();
        user.timestamp = System.currentTimeMillis() / 1000;
        return user;
    }

    private String name;
    private String email;
    private String provider;
    private long timestamp;
    private Map<String, Boolean> filters = new HashMap<>();

    User() {
    }

    @Override
    public String toString() {
        return "User: {"
                + "\n\tname: " + name
                + "\n\temail: " + email
                + "\n\tprovider: " + provider
                + "\n\ttimestamp: " + timestamp
                + "\n\tfilters: " + filters
                + "\n}";
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProvider() {
        return provider;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Boolean> getFilters() {
        return filters;
    }
}
