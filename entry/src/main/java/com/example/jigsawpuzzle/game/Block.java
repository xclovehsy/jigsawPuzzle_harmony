package com.example.jigsawpuzzle.game;

import ohos.agp.components.Image;
import ohos.agp.render.render3d.ViewHolder;

public class Block {
    private int x, y;
    private MyImage image;
    private Image view;
    private boolean isBlank;

    public Block(int x, int y, MyImage image, Image view, boolean isBlank) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.view = view;
        this.isBlank = isBlank;
    }

    public boolean isRightPosition(){
        if(image.getRightX() == x && image.getRightY() == y){
            return true;
        }else{
            return false;
        }
    }

    public Block() {
    }

    public boolean isBlank() {
        return isBlank;
    }


    public void RenewImage(){
        this.view.setPixelMap(this.image.getPixelMap());
    }

    public void setBlank(boolean blank) {
        isBlank = blank;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public MyImage getImage() {
        return image;
    }

    public void setImage(MyImage image) {
        this.image = image;
    }

    public Image getView() {
        return view;
    }

    public void setView(Image view) {
        this.view = view;
    }
}
