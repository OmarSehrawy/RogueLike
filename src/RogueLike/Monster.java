package RogueLike;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Monster extends Entity{
    Color[] colors = {Color.RED,Color.GREEN,Color.BLACK};
    String[] names = {"Demon","Zombie","Skeleton"};
    int[] maxHPs = {15,5,10};
    int[] damages = {4,2,3};
    int xp;
    int[] xps = {25,15,20};
    public Monster(int startX, int startY) {
        this.glyph = 'M';
        this.x_pos = startX;
        this.y_pos = startY;
        int id = ThreadLocalRandom.current().nextInt(0,3);
        this.color = colors[id];
        this.name = names[id];
        this.maxHP = maxHPs[id];
        this.damage = damages[id];
        this.hp = maxHP;
        this.xp = xps[id];
    }
}