package com.example.jigsawpuzzle.game;

import ohos.media.image.PixelMap;

public class MyImage {
    private int rightY, rightX;
    private PixelMap pixelMap;

    public MyImage() {
    }

    public MyImage(int rightX, int rightY, PixelMap pixelMap) {
        this.rightY = rightY;
        this.rightX = rightX;
        this.pixelMap = pixelMap;
    }

    public int getRightY() {
        return rightY;
    }

    public PixelMap getPixelMap() {
        return pixelMap;
    }

    public void setPixelMap(PixelMap pixelMap) {
        this.pixelMap = pixelMap;
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

}
