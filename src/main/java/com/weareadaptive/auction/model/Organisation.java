package com.weareadaptive.auction.model;

import com.weareadaptive.auction.user.User;

import java.util.List;

public record Organisation(String organisationName, List<User> users) {

}
