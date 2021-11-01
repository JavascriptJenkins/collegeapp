package com.isaac.collegeapp.h2model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name="token")
@JsonIgnoreProperties
public class TokenVO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    Long id;

    @JsonProperty
    String token;
    @JsonProperty
    String usermetadata; // for storing browser info etc (and hashed version of email)
    @JsonProperty
    int tokenused;
    @JsonProperty
    java.time.LocalTime updatedtimestamp;
    @JsonProperty
    java.time.LocalTime createtimestamp;
    @JsonProperty
    @Transient
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsermetadata() {
        return usermetadata;
    }

    public void setUsermetadata(String usermetadata) {
        this.usermetadata = usermetadata;
    }

    public int getTokenused() {
        return tokenused;
    }

    public void setTokenused(int tokenused) {
        this.tokenused = tokenused;
    }

    public LocalTime getUpdatedtimestamp() {
        return updatedtimestamp;
    }

    public void setUpdatedtimestamp(LocalTime updatedtimestamp) {
        this.updatedtimestamp = updatedtimestamp;
    }

    public LocalTime getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(LocalTime createtimestamp) {
        this.createtimestamp = createtimestamp;
    }



}