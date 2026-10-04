package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class LoginForm extends AbstractApiBase {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public LoginForm setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginForm setPassword(String password) {
        this.password = password;
        return this;
    }

}
