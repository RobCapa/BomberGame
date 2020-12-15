package game;

import java.util.function.Predicate;

public class Bomber extends Coordinates {
    private boolean isAlive;
    private static Bomber bomber;

    private Bomber() {
        super(0, 0);
        isAlive = true;
    }

    public void move(Direction direction) {
        int offsetX = x + direction.getX();
        int offsetY = y + direction.getY();

        Coordinates bomberCheck = new Coordinates(offsetX, offsetY) {};
        if (Game.getGame().doesNotViolateBoundaries(bomberCheck) && nothingTouches(bomberCheck)) {
            x = offsetX;
            y = offsetY;
        }
    }

    private boolean nothingTouches(Coordinates bomberCheck) {
        Game game = Game.getGame();
        return !game.getUnbreakableWalls().contains(bomberCheck)
                && !game.getWalls().contains(bomberCheck)
                && !game.getBombs().contains(bomberCheck);
    }

    public static Bomber getInstance() {
        if (bomber == null) {
            bomber = new Bomber();
        }
        return bomber;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
