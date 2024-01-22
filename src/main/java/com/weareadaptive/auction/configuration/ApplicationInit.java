package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit {
  private final UserState userState;

  public ApplicationInit(final UserState userState) {
    this.userState = userState;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createInitData() {
    System.out.println("Am here");
    var admin = new User(
            userState.nextId(),
            "ADMIN",
            "adminpassword",
            "admin",
            "admin",
            "Adaptive",
            true);
    var user = new User(
            userState.nextId(),
            "user",
            "adminpassword",
            "admin",
            "admin",
            "Adaptive");
    userState.add(admin);

  }

}
