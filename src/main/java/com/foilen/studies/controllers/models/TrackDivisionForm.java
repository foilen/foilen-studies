package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class TrackDivisionForm extends AbstractApiBase {

    private short divisor;
    private short quotient;
    private boolean success;

    public short getDivisor() {
        return divisor;
    }

    public void setDivisor(short divisor) {
        this.divisor = divisor;
    }

    public short getQuotient() {
        return quotient;
    }

    public void setQuotient(short quotient) {
        this.quotient = quotient;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
