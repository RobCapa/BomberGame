package game;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Coordinates implements Runnable {
    private static int bombLevel = 1;
    private List<Flame> flames;
    private boolean isExploded;

    public Bomb(int x, int y) {
        super(x, y);
        new Thread(this).start();
    }

    public static void levelUp() {
        bombLevel++;
    }

    private void createFlame() {
        List<Flame> flames = new ArrayList<>();
        flames.add(new Flame(getX(), getY()));
        for (int i = 1; i <= bombLevel; i++) {
            flames.add(new Flame(getX() + i, getY()));
            flames.add(new Flame(getX() - i, getY()));
            flames.add(new Flame(getX(), getY() + i));
            flames.add(new Flame(getX(), getY() - i));
        }
        validateFlame(flames);
        this.flames = flames;
    }

    private void validateFlame(List<Flame> flames) {
        flames.removeIf(flame -> !Game.getGame().doesNotViolateBoundaries(flame));
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createFlame();
        isExploded = true;
    }

    public List<Flame> getFlame() {
        return flames;
    }

    public boolean isExploded() {
        return isExploded;
    }

    public class Flame extends Coordinates {

        public Flame(int x, int y) {
            super(x, y);
        }
    }
}
