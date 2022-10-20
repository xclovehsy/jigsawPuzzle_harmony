package com.example.jigsawpuzzle.game;

import ohos.agp.components.Image;

public class Chip {
    private int rightX;
    private int rightY;
    private int curX;
    private int curY;
    private Image image;
    private int id;

    public Chip(int rightX, int rightY, Image image, int id) {
        this.rightX = rightX;
        this.rightY = rightY;
        this.image = image;
        this.id = id;
        this.curX = -1;
        this.curY = -1;
    }

    public boolean isRightPosition(){
        return curX == rightX && curY == rightY;
    }

    public boolean isInSpace(){
        return !(curX == -1 && curY == -1);
    }

    public void outOfSpace(){
        curY = -1;
        curX = -1;
    }

    public Chip() {
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getRightY() {
        return rightY;
    }

    public void setRightY(int rightY) {
        this.rightY = rightY;
    }

    public int getCurX() {
        return curX;
    }

    public void setCurX(int curX) {
        this.curX = curX;
    }

    public int getCurY() {
        return curY;
    }

    public void setCurY(int curY) {
        this.curY = curY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}