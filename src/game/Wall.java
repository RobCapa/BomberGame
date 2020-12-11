package game;

public class Wall extends Coordinates {
    private boolean isAlive;

    public Wall(int x, int y) {
        super(x, y);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
