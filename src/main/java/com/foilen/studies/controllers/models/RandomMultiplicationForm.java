package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class RandomMultiplicationForm extends AbstractApiBase {

    private short leftMax;
    private short rightMax;
    private short amount;

    private boolean leftAlwaysSmaller;

    public short getLeftMax() {
        return leftMax;
    }

    public RandomMultiplicationForm setLeftMax(short leftMax) {
        this.leftMax = leftMax;
        return this;
    }

    public short getRightMax() {
        return rightMax;
    }

    public RandomMultiplicationForm setRightMax(short rightMax) {
        this.rightMax = rightMax;
        return this;
    }

    public short getAmount() {
        return amount;
    }

    public RandomMultiplicationForm setAmount(short amount) {
        this.amount = amount;
        return this;
    }

    public boolean isLeftAlwaysSmaller() {
        return leftAlwaysSmaller;
    }

    public RandomMultiplicationForm setLeftAlwaysSmaller(boolean leftAlwaysSmaller) {
        this.leftAlwaysSmaller = leftAlwaysSmaller;
        return this;
    }

}
