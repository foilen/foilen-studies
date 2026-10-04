package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class ChangePasswordForm extends AbstractApiBase {

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirmation;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public ChangePasswordForm setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ChangePasswordForm setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public ChangePasswordForm setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
        return this;
    }

}
