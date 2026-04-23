package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class RandomDivisionForm extends AbstractApiBase {

    private short divisorMax;
    private short quotientMax;
    private short amount;

    private boolean divisorAlwaysSmaller;

    public short getDivisorMax() {
        return divisorMax;
    }

    public RandomDivisionForm setDivisorMax(short divisorMax) {
        this.divisorMax = divisorMax;
        return this;
    }

    public short getQuotientMax() {
        return quotientMax;
    }

    public RandomDivisionForm setQuotientMax(short quotientMax) {
        this.quotientMax = quotientMax;
        return this;
    }

    public short getAmount() {
        return amount;
    }

    public RandomDivisionForm setAmount(short amount) {
        this.amount = amount;
        return this;
    }

    public boolean isDivisorAlwaysSmaller() {
        return divisorAlwaysSmaller;
    }

    public RandomDivisionForm setDivisorAlwaysSmaller(boolean divisorAlwaysSmaller) {
        this.divisorAlwaysSmaller = divisorAlwaysSmaller;
        return this;
    }

}
