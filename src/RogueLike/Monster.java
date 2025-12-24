package RogueLike;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Monster extends Entity{
    int id;
    Color[] colors = {Color.GREEN,new Color(175, 75, 25),Color.BLACK,Color.WHITE,Color.RED};
    String[] names = {"Zombie","Bat","Skeleton","Ghost","Demon"};
    int[] maxHPs = {5,7,10,12,15};
    int[] damages = {1,1,2,2,3};
    int xp;
    int[] xps = {15,15,20,20,25};
    int score;
    boolean isCowardly;
    public Monster(int startX, int startY,int floor) {
        this.glyph = 'M';
        this.x_pos = startX;
        this.y_pos = startY;
        this.id = ThreadLocalRandom.current().nextInt(0,5);
        this.color = colors[id];
        this.name = names[id];
        this.maxHP = maxHPs[id]+(floor-1)*(id+1);
        this.damage = damages[id]+(floor-1)*(id+1)/2;
        this.hp = maxHP;
        this.xp = xps[id]+(floor-1)*(id+1)*5/2;
        this.score = (this.id+1)*100;
        if(id == 1 || id == 3) this.isCowardly = true;
    }
}