package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class LoginWithCodeForm extends AbstractApiBase {

    private String email;
    private String code;

    public String getEmail() {
        return email;
    }

    public LoginWithCodeForm setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCode() {
        return code;
    }

    public LoginWithCodeForm setCode(String code) {
        this.code = code;
        return this;
    }

}
