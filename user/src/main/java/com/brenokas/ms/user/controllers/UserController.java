package com.brenokas.ms.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.brenokas.ms.user.models.UserModel;
import com.brenokas.ms.user.models.dtos.UserRequestDTO;
import com.brenokas.ms.user.services.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/users")
public class UserController {

  final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRequestDTO request) {
      var userModel = new UserModel();
      BeanUtils.copyProperties(request, userModel);
      
      return ResponseEntity.status(201).body(userService.save(userModel));
  }
  
}
