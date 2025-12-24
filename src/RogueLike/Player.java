package RogueLike;

import java.awt.*;

public class Player extends Entity{
    int xp;
    int level;
    int score;
    public Player() {
        this.glyph = '@';
        this.color = Color.GREEN;
        this.name = "RogueLike.Player";
        this.maxHP = 30;
        this.hp = maxHP;
        this.damage = 77;
        this.xp = 0;
        this.level = 1;
        this.score = 0;
    }
    public void gainXP(int xp,MessageLog log) {
        this.xp += xp;
        if(this.xp >= 50 + (this.level - 1) * 50) {
            this.xp -= 50 + (this.level - 1) * 50;
            this.level++;
            this.maxHP += 25;
            this.hp = maxHP;
            this.damage += 3;
            log.add(String.format("%sYou leveled up%s","\u001B[38;2;0;255;255m","\u001B[0m"));
        }
    }
    public void gainScore(int amount) {
        this.score += amount;
    }
}