package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

import java.util.Date;

public class UserInfo extends AbstractApiBase {

    private String id;
    private String email;
    private boolean passwordSet;
    private Date creationDate;
    private Date lastLoginDate;

    public String getId() {
        return id;
    }

    public UserInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isPasswordSet() {
        return passwordSet;
    }

    public UserInfo setPasswordSet(boolean passwordSet) {
        this.passwordSet = passwordSet;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public UserInfo setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public UserInfo setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
        return this;
    }

}
