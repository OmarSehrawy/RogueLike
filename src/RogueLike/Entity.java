package RogueLike;

import java.awt.*;

public abstract class Entity {
    int x_pos;
    int y_pos;
    int hp;
    int maxHP;
    int damage;
    String name;
    char glyph;
    Color color;
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
    }
    public boolean isDead() {
        return this.hp <= 0;
    }
}