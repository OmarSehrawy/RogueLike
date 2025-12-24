package RogueLike;

import java.awt.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void handleInput(World world, Player player) {
        System.out.println("Move with numpad in 8 directions");
        if(!scanner.hasNextInt()) {
            scanner.nextLine();
            return;
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        int nextX = player.x_pos;
        int nextY = player.y_pos;
        if(input == 8) nextY--;
        if(input == 2) nextY++;
        if(input == 6) nextX++;
        if(input == 4) nextX--;
        if(input == 7) { nextY--; nextX--; }
        if(input == 9) { nextY--; nextX++; }
        if(input == 1) { nextY++; nextX--; }
        if(input == 3) { nextY++; nextX++; }
        boolean turnFinished = false;
        boolean playerMoved = false;
        if(world.getTile(nextX,nextY) != Tile.WALL) {
            if(world.getMonsterAt(nextX,nextY) != null) {
                world.playerAtk(player,nextX,nextY);
            } else {
                player.x_pos = nextX;
                player.y_pos = nextY;
                playerMoved = true;
            }
            turnFinished = true;
        }
        if(turnFinished) world.moveMonsters(player);
        if(playerMoved) {
            if(world.isPlayerExit(player)) world.changeFloor(player,1);
            else if(world.isPlayerReturn(player)) world.changeFloor(player,-1);
        }
    }
    public static String convertColor(Color color) {
        if (color == null) return "\u001B[0m";
        return String.format("\u001B[38;2;%d;%d;%dm",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }
    public static void displayStats(Player player,int floor) {
        String green = convertColor(Color.GREEN);
        String red = convertColor(Color.RED);
        String cyan = convertColor(Color.CYAN);
        String yellow = convertColor(Color.yellow);
        String reset = "\u001B[0m";
        String hpColor = (player.hp > player.maxHP/2)? green : red;
        System.out.printf("Floor: %s%d%s | Score: %s%d%s%n",yellow,floor,reset,yellow,player.score,reset);
        System.out.printf("HP: %s%d/%d%s | DMG: %s%d%s%n",hpColor,player.hp,player.maxHP,reset,red,player.damage,reset);
        System.out.printf("XP: %s%d%s | Level: %s%d%s%n",cyan,player.xp,reset,cyan,player.level,reset);
    }
    public static void gameLoop(Player player, World world) {
        clearScreen();
        world.display(player);
        displayStats(player, world.getFloor());
        world.log.display();
        handleInput(world, player);
    }
    public static void placePlayer(Player player,World world) {
        int x,y;
        do {
            x = ThreadLocalRandom.current().nextInt(1, world.getWidth() - 1);
            y = ThreadLocalRandom.current().nextInt(1, world.getHeight() - 1);
        } while (world.getTile(x,y) != Tile.FLOOR);
        player.x_pos = x;
        player.y_pos = y;
    }
    public static void main(String[] args) {
        boolean playing = true;
        World world = new World(15, 11);
        Player player = new Player();
        world.generateFloor(player);
        placePlayer(player,world);
        while (playing) {
            gameLoop(player,world);
            if(player.isDead()) {
                System.out.printf("%sYou died%s%n",convertColor(Color.RED),"\u001B[0m");
                System.out.println("Play again y/n?");
                String choice = scanner.nextLine().toLowerCase();
                if(!choice.equals("y")) {
                    playing = false;
                } else {
                    world = new World(15,11);
                    player = new Player();
                    world.generateFloor(player);
                    placePlayer(player,world);
                }
            }
        }
    }
}