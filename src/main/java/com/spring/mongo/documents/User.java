package com.spring.mongo.documents;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.spring.mongo.db.utils.Constants;

@Document(collection = Constants.MONGO_DB_USER_COLLECTION)
public class User {

  @Id
  private String id;

  @Indexed
  private String ic;

  @Field("name")
  private String name;

  @Field("age")
  private int age;

  @Field("created_date")
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Date createdDate;

  @Field("address")
  private Address address;

  @Field("gender")
  private Gender gender;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIc() {
    return ic;
  }

  public void setIc(String ic) {
    this.ic = ic;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}