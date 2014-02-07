package com.example.loaderexample;

import com.mobindustry.cursormapper.Column;

public class User {
  private String name;
  private String email;
  @Column("_id")
  private long id;

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "{id=" + id + ", name=" + name + "}";
  }
}