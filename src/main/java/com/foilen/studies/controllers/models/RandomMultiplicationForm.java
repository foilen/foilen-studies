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

    public void setLeftMax(short leftMax) {
        this.leftMax = leftMax;
    }

    public short getRightMax() {
        return rightMax;
    }

    public void setRightMax(short rightMax) {
        this.rightMax = rightMax;
    }

    public short getAmount() {
        return amount;
    }

    public void setAmount(short amount) {
        this.amount = amount;
    }

    public boolean isLeftAlwaysSmaller() {
        return leftAlwaysSmaller;
    }

    public void setLeftAlwaysSmaller(boolean leftAlwaysSmaller) {
        this.leftAlwaysSmaller = leftAlwaysSmaller;
    }

}
