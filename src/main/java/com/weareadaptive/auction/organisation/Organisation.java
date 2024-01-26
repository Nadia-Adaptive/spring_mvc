package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.model.Entity;
import com.weareadaptive.auction.user.User;

import java.util.List;

public record Organisation(int id, String organisationName, List<User> users) implements Entity {

    @Override
    public int getId() {
        return id;
    }
}
