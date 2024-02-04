package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class TrackMultiplicationForm extends AbstractApiBase {

    private short left;
    private short right;
    private boolean success;

    public short getLeft() {
        return left;
    }

    public void setLeft(short left) {
        this.left = left;
    }

    public short getRight() {
        return right;
    }

    public void setRight(short right) {
        this.right = right;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
