package com.spring.mongo.documents;

import org.springframework.data.mongodb.core.mapping.Field;

public class Address {

  @Field("city")
  private String city;

  @Field("state")
  private String state;

  @Field("country")
  private String country;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

}
