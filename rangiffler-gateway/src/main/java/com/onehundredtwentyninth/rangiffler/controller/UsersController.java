package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.model.UserJson;
import com.onehundredtwentyninth.rangiffler.service.UsersClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

  private final UsersClient usersClient;

  @Autowired
  public UsersController(UsersClient usersClient) {
    this.usersClient = usersClient;
  }

  @GetMapping("/allUsers")
  public List<UserJson> getAllUsers() {
    return usersClient.getAllUsers();
  }
}
