package com.example.jigsawpuzzle.game;

import ohos.agp.components.Image;

public class Blank {
    private Image image;
    private boolean isOccupy;
    private int x;
    private int y;

    public Blank(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.isOccupy = false;
    }

    public Blank() {
    }

    public Image getImage() {
        return image;
    }

    public boolean isOccupy() {
        return isOccupy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setOccupy(boolean occupy) {
        isOccupy = occupy;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
