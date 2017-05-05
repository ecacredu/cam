package com.acecosmos.camle.models;

/**
 * Created by sd on 02-05-2017.
 */

public class Pref {

  String title, summary;

  public Pref() {
  }

  public Pref(String title, String summary) {
    this.title = title;
    this.summary = summary;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }
}
