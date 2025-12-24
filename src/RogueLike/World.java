package RogueLike;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private int floor = 0;
    private int width;
    private int height;
    private Tile[][] map;
    private List<Tile[][]> levels = new ArrayList<>();
    public List<Monster> monsters = new ArrayList<>();
    public List<List<Monster>> levelMonsters = new ArrayList<>();
    public MessageLog log = new MessageLog();
    boolean hasEnteredJungle = false;
    boolean hasEnteredDungeon = false;
    public void display(Player player) {
        String reset = "\u001B[0m";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (player.x_pos == x && player.y_pos == y) {
                    printEntity(player.color,player.glyph);
                } else if (getMonsterAt(x,y) != null) {
                    Monster m = getMonsterAt(x,y);
                    printEntity(m.color,m.glyph);
                } else {
                    Tile tile = map[x][y];
                    Color displayColor = getDisplayColor(tile);
                    String colorCode = convertColor(displayColor);
                    System.out.printf("%s%c%s ", colorCode, tile.glyph, reset);
                }
            }
            System.out.println();
        }
    }
    private Color getDisplayColor(Tile tile) {
        Color displayColor = tile.color;
        if (this.floor > 5 && this.floor <= 10) {
            if (tile == Tile.WALL) displayColor = new Color(0, 150, 0);
            if (tile == Tile.FLOOR) displayColor = new Color(150, 75, 25);
        } else if (this.floor > 10) {
            if (tile == Tile.WALL) displayColor = new Color(100,0, 150);
            if (tile == Tile.FLOOR) displayColor = new Color(0,0,100);
        }
        return displayColor;
    }
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Tile[width][height];
    }
    public void setMap(Tile[][] map) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    map[x][y] = Tile.WALL;
                } else {
                    map[x][y] = Tile.FLOOR;
                }
            }
        }
    }
    public String convertColor(Color color) {
        if (color == null) return "\u001B[0m";
        return String.format("\u001B[38;2;%d;%d;%dm",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }
    public Tile getTile(int x, int y) {
        return map[x][y];
    }
    public void spawnMonsters(List<Monster> monsters,int count,Player player) {
        for (int i = 0; i < count; i++) {
            int x = ThreadLocalRandom.current().nextInt(1,width-1);
            int y = ThreadLocalRandom.current().nextInt(1,height-1);
            if (!isOccupied(player,x,y)) {
                monsters.add(new Monster(x,y,this.floor));
            } else {
                i--;
            }
        }
    }
    public Monster getMonsterAt(int x, int y) {
        for (Monster m : monsters) {
            if (m.x_pos == x && m.y_pos == y) return m;
        }
        return null;
    }
    private void printEntity(Color c,char g) {
        System.out.printf("%s%c\u001B[0m ", convertColor(c), g);
    }
    public boolean isOccupied(Player player,int x,int y) {
        if(map[x][y] == Tile.WALL) return true;
        if(player.x_pos == x && player.y_pos == y) return true;
        for (Monster m : monsters) {
            if (m.x_pos == x && m.y_pos == y) return true;
        }
        return false;
    }
    public void moveMonsters(Player player) {
        int visionRange = 5;
        for (Monster m : monsters) {
            int dist = Math.max(Math.abs(player.x_pos - m.x_pos), Math.abs(player.y_pos - m.y_pos));
            if (dist <= visionRange) {
                moveTowardPlayer(player,m);
            } else {
                moveRandomly(m);
            }
        }
    }
    private void moveTowardPlayer(Player player,Monster m) {
        int dx = Integer.compare(player.x_pos,m.x_pos);
        int dy = Integer.compare(player.y_pos,m.y_pos);
        if(!isOccupied(player,m.x_pos+dx,m.y_pos+dy)) {
            m.x_pos += dx;
            m.y_pos += dy;
        } else if (player.x_pos == m.x_pos+dx && player.y_pos == m.y_pos+dy) {
            player.takeDamage(m.damage);
            log.add(String.format("%s%s hit you for %d damage%s", convertColor(Color.RED), m.name, m.damage, "\u001B[0m"));
        }
    }
    private void moveRandomly(Monster m) {
        if(Math.random() < 0.25) {
            int dir = (int)(Math.random() * 4);
            int dx = 0; int dy = 0;
            switch (dir) {
                case 0:
                    dy = -1;
                    break;
                case 1:
                    dy = 1;
                    break;
                case 2:
                    dx = -1;
                    break;
                case 3:
                    dx = 1;
                    break;
                default:
                    break;
            }
            if(map[m.x_pos+dx][m.y_pos+dy] == Tile.FLOOR) {
                m.x_pos += dx;
                m.y_pos += dy;
            }
        }
    }
    public void playerAtk(Player player,int x,int y) {
        Monster target = getMonsterAt(x,y);
        if (target != null) {
            int damage = player.damage;
            target.takeDamage(damage);
            log.add(String.format("%sYou have hit %s for %d damage%s",convertColor(Color.GREEN),target.name,damage,"\u001B[0m"));
            if(target.isDead()) {
                log.add(String.format("%s%s died%s",convertColor(Color.GREEN),target.name,"\u001B[0m"));
                player.gainXP(target.xp,log);
                player.gainScore(target.score);
                monsters.remove(target);
            }
        }
    }
    public boolean isPlayerExit(Player player) {
        return map[player.x_pos][player.y_pos] == Tile.STAIRSDOWN;
    }
    public boolean isPlayerReturn(Player player) {
        return map[player.x_pos][player.y_pos] == Tile.STAIRSUP;
    }
    public void generateFloor(Player player) {
        this.floor++;
        Tile[][] newMap = new Tile[width][height];
        if(Math.random() < 0.2) {
            setMap(newMap);
        } else {
            int mapID = ThreadLocalRandom.current().nextInt(MapPool.floorMaps.size());
            String[] mapLayout = MapPool.floorMaps.get(mapID);
            loadPreset(mapLayout, newMap);
        }
        levels.add(newMap);
        this.map = newMap;
        List<Monster> newMonsters = new ArrayList<>();
        int monsterRandom = ThreadLocalRandom.current().nextInt(2,5);
        spawnMonsters(newMonsters,monsterRandom,player);
        levelMonsters.add(newMonsters);
        this.monsters = newMonsters;
        placeExit();
        if(this.floor > 1) {
            log.add(String.format("%sYou descend deeper%s", "\u001B[38;2;255;255;0m", "\u001B[0m"));
            placeReturn();
            Point start = findTile(Tile.STAIRSUP);
            player.x_pos = start.x_pos;
            player.y_pos = start.y_pos;
        }
        if(this.floor == 6) {
            if(!hasEnteredJungle) {
                log.add(String.format("%sYou enter the jungle%s", "\u001B[38;2;255;255;0m", "\u001B[0m"));
                hasEnteredJungle = true;
            }
        }
        if(this.floor == 11) {
            if(!hasEnteredDungeon) {
                log.add(String.format("%sYou enter the dungeon%s", "\u001B[38;2;255;255;0m", "\u001B[0m"));
                hasEnteredDungeon = true;
            }
        }
    }
    public void placeExit() {
        int x,y;
        do {
            x = ThreadLocalRandom.current().nextInt(1,width-1);
            y = ThreadLocalRandom.current().nextInt(1,height-1);
        } while (map[x][y] != Tile.FLOOR);
        map[x][y] = Tile.STAIRSDOWN;
    }
    public void placeReturn() {
        int x,y;
        do {
            x = ThreadLocalRandom.current().nextInt(1,width-1);
            y = ThreadLocalRandom.current().nextInt(1,height-1);
        } while (map[x][y] != Tile.FLOOR);
        map[x][y] = Tile.STAIRSUP;
    }
    public void changeFloor(Player player, int dir) {
        int targetIndex = this.floor - 1 + dir;
        if (targetIndex >= 0 && targetIndex < levels.size()) {
            this.floor += dir;
            this.map = levels.get(targetIndex);
            this.monsters = levelMonsters.get(targetIndex);
            Tile landingTile = (dir > 0)? Tile.STAIRSUP : Tile.STAIRSDOWN;
            Point landingPos = findTile(landingTile);
            if(landingPos != null) {
                player.x_pos = landingPos.x_pos;
                player.y_pos = landingPos.y_pos;
            }
        } else if (dir > 0) {
            generateFloor(player);
            player.gainScore(1000);
        }
    }
    private Point findTile(Tile tile) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(map[x][y] == tile) return new Point(x,y);
            }
        }
        return null;
    }
    public void loadPreset(String[] preset,Tile[][] newMap) {
        for (int y = 0; y < height; y++) {
            String line = preset[y];
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                if (c=='#') {
                    newMap[x][y] = Tile.WALL;
                } else {
                    newMap[x][y] = Tile.FLOOR;
                }
            }
        }
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public int getFloor() {
        return this.floor;
    }
}