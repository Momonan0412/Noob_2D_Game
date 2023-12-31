package entity;

import main.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.getGPTile;

public class Player extends Entity {
    UtilityTool uTool;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        this.screenX = (gp.screenWidth/2) - (getGPTile()/2);
        this.screenY = (gp.screenHeight/2) - (getGPTile()/2);
        solidArea = new Rectangle();
        solidArea.x = 13; /** x*2 + width == 48 **/
        solidArea.y = 30; /** y + height == 48 **/
        super.solidAreaDefaultX = solidArea.x;
        super.solidAreaDefaultY = solidArea.y;
        solidArea.width = 22; /** 28 + 10*2 = 48 **/
        solidArea.height = 18;
        setDefaultValues();
        getPlayerImage();
    }

    private static int frameSize(int size) {
        return size;
    }
    public void setDefaultValues(){
        super.worldX = getGPTile()*30; // postition
        super.worldY = getGPTile()*38; // postition
        super.speed = 2;
        super.upSize = 8; /** # Frames **/
        super.downSize = 8; /** # Frames **/
        super.leftSize = 8; /** # Frames **/
        super.rightSize = 8; /** # Frames **/
        super.idleSize = 3;
        super.direction = "idle"; /** Initial position shown or static **/
        uTool = new UtilityTool();
        up = new BufferedImage[upSize];
        down = new BufferedImage[downSize];
        left = new BufferedImage[leftSize];
        right = new BufferedImage[rightSize];
        idle = new BufferedImage[idleSize];
    }
    public void getPlayerImage(){
            for(int i = 0; i < upSize; i++){
                super.up[i] = uTool.setup("Unfinished 2D Character/Back", i, true);
            }
            for(int i = 0; i < downSize; i++){
                super.down[i] = uTool.setup("Unfinished 2D Character/Front", i, true);
            }
            for(int i = 0; i < leftSize; i++){
                super.left[i] = uTool.setup("Unfinished 2D Character/Left", i, true);
            }
            for(int i = 0; i < rightSize; i++){
                super.right[i] = uTool.setup("Unfinished 2D Character/Right", i, true);
            }
            for(int i = 0; i < idleSize; i++){
                super.idle[i] = uTool.setup("Unfinished 2D Character/Idle", i, true);
            }
    }
    public void update(){

        if(keyH.upPressed){
            super.direction = "up";
        } else if (keyH.downPressed) {
            super.direction = "down";
        } else if (keyH.leftPressed) {
            super.direction = "left";
        } else if (keyH.rightPressed) {
            super.direction = "right";
        }
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            /**
             * CHECK TILE COLLISION
             * **/
            collisionOn = false;
            /**
             * CHECK OBJECT COLLISION
             * **/
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            gp.cChecker.checkTile(this);
            if(!collisionOn){
                switch (direction) {
                    case "up" -> super.worldY -= speed;
                    case "down" -> super.worldY += speed;
                    case "left" -> super.worldX -= speed;
                    case "right" -> super.worldX += speed;
                }
            }
            super.transition++;
            if(super.transition > 6){
                switch (super.direction) {
                    case "up" -> super.spriteCounter = (super.spriteCounter + 1) % super.upSize;
                    case "down" -> super.spriteCounter = (super.spriteCounter + 1) % super.downSize;
                    case "left" -> super.spriteCounter = (super.spriteCounter + 1) % super.leftSize;
                    case "right" -> super.spriteCounter = (super.spriteCounter + 1) % super.rightSize;
                    case "idle" -> {
                        Random idle = new Random();
                        super.spriteCounter = idle.nextInt(100);
                        if (super.spriteCounter > 1) {
                            super.spriteCounter = 2;
                        }
                    }
                }
                super.transition = 0;
            }
        }
    }
    public void pickUpObject(int index) {
        if (index != 999 && gp.objectDrawerThread.getObject(index) != null) {
        }
    }
    public void draw(Graphics2D g2) {

            BufferedImage[] image = null;
            switch (super.direction) {
                case "up":
                    image = super.up;
                    break;
                case "down":
                    image = super.down;
                    break;
                case "left":
                    image = super.left;
                    break;
                case "right":
                    image = super.right;
                    break;
                case "idle":
                    image = super.idle;
                    break;
            }
            if (image != null && spriteCounter < image.length) {
                g2.drawImage(image[spriteCounter], screenX, screenY, null);
                g2.setColor(Color.RED);
                g2.drawRect(screenX, screenY, getGPTile(), getGPTile()); /** Collision Check! Debug! **/
                g2.setColor(Color.BLUE);
                g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }
    }
}
