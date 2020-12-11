package game;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Coordinates implements Runnable {
    private static int bombLevel = 1;
    private List<Flame> flame;
    private boolean isExploded;

    public Bomb (int x, int y){
        super(x, y);
        new Thread(this).start();
    }

    public static void levelUp() {
        bombLevel++;
    }

    private void createFlame() {
        List<Flame> flame = new ArrayList<>();
        flame.add(new Flame(getX(), getY()));
        for(int i = 1; i <= bombLevel; i++) {
            flame.add(new Flame(getX() + i, getY()));
            flame.add(new Flame(getX() - i, getY()));
            flame.add(new Flame(getX(), getY() + i));
            flame.add(new Flame(getX(), getY() - i));
        }
        validateFlame(flame);
        this.flame = flame;
    }

    private void validateFlame(List<Flame> flame) {
        flame.removeIf(currentFlame -> currentFlame.getX() < 0 || currentFlame.getX() >= Game.getGame().getHeight() ||
                currentFlame.getY() < 0 || currentFlame.getY() >= Game.getGame().getWidth());
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
        return flame;
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
