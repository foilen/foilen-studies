package com.foilen.studies.data.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class UserDetails {

    @Id
    private String id;

    private String email;
    private boolean disabled;

    private String passwordHash;
    private Date passwordLastChange;

    private String loginCode;
    private Date loginCodeExpiration;
    private Date loginCodeLastGenerated;

    private Date creationDate;
    private Date lastLoginDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getPasswordLastChange() {
        return passwordLastChange;
    }

    public void setPasswordLastChange(Date passwordLastChange) {
        this.passwordLastChange = passwordLastChange;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public Date getLoginCodeExpiration() {
        return loginCodeExpiration;
    }

    public void setLoginCodeExpiration(Date loginCodeExpiration) {
        this.loginCodeExpiration = loginCodeExpiration;
    }

    public Date getLoginCodeLastGenerated() {
        return loginCodeLastGenerated;
    }

    public void setLoginCodeLastGenerated(Date loginCodeLastGenerated) {
        this.loginCodeLastGenerated = loginCodeLastGenerated;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

}
