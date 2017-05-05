package com.acecosmos.camle.models;

import java.util.HashMap;

/**
 * Created by sd on 02-05-2017.
 */

public class User {

  HashMap<String,Object> profile;

  public User() {
  }

  public User(HashMap<String, Object> profile) {
    this.profile = profile;
  }

  public HashMap<String, Object> getProfile() {
    return profile;
  }

  public void setProfile(HashMap<String, Object> profile) {
    this.profile = profile;
  }
}
