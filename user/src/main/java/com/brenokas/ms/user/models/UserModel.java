package com.brenokas.ms.user.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_USERS")
public class UserModel implements Serializable {
  private static final long serialVersionUID = 1L; 

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  private String name;
  private String email;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }

  
}
