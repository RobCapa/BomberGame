package game;

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
        if (!Game.getGame().violateBoundaries(bomberCheck) && !touchBombsOrAnyWall(bomberCheck)) {
            x = offsetX;
            y = offsetY;
        }
    }

    private boolean touchBombsOrAnyWall(Coordinates bomberCheck) {
        Game game = Game.getGame();
        return game.touchUnbreakableWalls(bomberCheck)
                || game.touchWalls(bomberCheck)
                || game.touchBombs(bomberCheck);
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
