package game;

import java.util.Arrays;

public class Drawing {
    private Game game;

    public Drawing(Game game) {
        this.game = game;
    }

    public void printGameField() {
        String[][] galeField = new String[game.getHeight()][game.getWidth()];
        for (String[] strings : galeField) {
            Arrays.fill(strings, " ⬜ ");
        }

        for (UnbreakableWall unbreakableWall : game.getUnbreakableWalls()) {
            galeField[unbreakableWall.getX()][unbreakableWall.getY()] = " \uD83C\uDFFF ";
        }

        for (Wall wall : game.getWalls()) {
            galeField[wall.getX()][wall.getY()] = " \uD83C\uDFFB ";
        }

        for (Bomb bomb : game.getBombs()) {
            galeField[bomb.getX()][bomb.getY()] = " \uD83D\uDCA3️ ";
        }

        for (Bomb.Flame flame : game.getFlames()) {
            galeField[flame.getX()][flame.getY()] = " \uD83D\uDD25 ";
        }

        galeField[game.getBonus().getX()][game.getBonus().getY()] = " \uD83C\uDF87 ";

        if (game.getBomber().isAlive()) {
            galeField[game.getBomber().getX()][game.getBomber().getY()] = " \uD83D\uDC64 ";
        } else {
            galeField[game.getBomber().getX()][game.getBomber().getY()] = " \uD83D\uDC80 ";
        }

        for (String[] line : galeField) {
            for (String obj : line) {
                System.out.print(obj);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
