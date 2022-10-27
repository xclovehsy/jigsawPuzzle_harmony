package com.example.jigsawpuzzle.game;

public class MyImage {
    private int rightY, rightX;
    private int imageId;

    public MyImage() {
    }

    public MyImage(int rightX, int rightY, int imageId) {
        this.rightY = rightY;
        this.rightX = rightX;
        this.imageId = imageId;
    }

    public int getRightY() {
        return rightY;
    }



    public void setRightY(int rightY) {
        this.rightY = rightY;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
