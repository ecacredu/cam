package com.acecosmos.camle.models;

/**
 * Created by sd on 02-05-2017.
 */

public class Product {

  String type, user, userName, userCity, brand, modelNumber, rent1Day, rent3Day, rent5Day;

  public Product() {
  }

  public Product(String type, String user, String userName, String userCity, String brand, String modelNumber, String rent1Day, String rent3Day, String rent5Day) {
    this.type = type;
    this.user = user;
    this.userName = userName;
    this.userCity = userCity;
    this.brand = brand;
    this.modelNumber = modelNumber;
    this.rent1Day = rent1Day;
    this.rent3Day = rent3Day;
    this.rent5Day = rent5Day;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserCity() {
    return userCity;
  }

  public void setUserCity(String userCity) {
    this.userCity = userCity;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public String getRent1Day() {
    return rent1Day;
  }

  public void setRent1Day(String rent1Day) {
    this.rent1Day = rent1Day;
  }

  public String getRent3Day() {
    return rent3Day;
  }

  public void setRent3Day(String rent3Day) {
    this.rent3Day = rent3Day;
  }

  public String getRent5Day() {
    return rent5Day;
  }

  public void setRent5Day(String rent5Day) {
    this.rent5Day = rent5Day;
  }
}
