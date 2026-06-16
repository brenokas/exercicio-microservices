package com.brenokas.ms.user.services;

import com.brenokas.ms.user.producers.UserProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brenokas.ms.user.models.UserModel;
import com.brenokas.ms.user.repositories.UserRepository;

@Service
public class UserService {
  final UserRepository userRepository;
  final UserProducer userProducer;

  public UserService(UserRepository userRepository, UserProducer userProducer) {
    this.userRepository = userRepository;
    this.userProducer = userProducer;
  }
  
  @Transactional
  public UserModel save(UserModel user) {
    UserModel userModel = userRepository.save(user);
    userProducer.publishMessageEmail(userModel);

    return userModel;
  }

}
