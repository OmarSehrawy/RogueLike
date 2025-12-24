package RogueLike;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Monster extends Entity{
    int id;
    Color[] colors = {Color.GREEN,Color.BLACK,Color.RED};
    String[] names = {"Zombie","Skeleton","Demon"};
    int[] maxHPs = {5,10,15};
    int[] damages = {1,2,3};
    int xp;
    int[] xps = {15,20,25};
    int score;
    public Monster(int startX, int startY,int floor) {
        this.glyph = 'M';
        this.x_pos = startX;
        this.y_pos = startY;
        this.id = ThreadLocalRandom.current().nextInt(0,3);
        this.color = colors[id];
        this.name = names[id];
        this.maxHP = maxHPs[id]+(floor-1)*(id+1);
        this.damage = damages[id]+(floor-1)*(id+1)/2;
        this.hp = maxHP;
        this.xp = xps[id]+(floor-1)*(id+1)*5/2;
        this.score = (this.id+1)*100;
    }
}