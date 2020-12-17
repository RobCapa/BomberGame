package game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    private int height, width;
    private Bomber bomber;
    private Bonus bonus;
    private List<UnbreakableWall> unbreakableWalls = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private List<Bomb> bombs = new ArrayList<>();
    private List<Bomb.Flame> flames = new ArrayList<>();
    private static Game game;

    private Game(int height, int width, int countWalls, Bomber bomber) {
        int correctHeight = Math.max(3, height);
        this.height = correctHeight % 2 == 0 ? correctHeight + 1 : correctHeight;

        int correctWidth = Math.max(3, width);
        this.width = correctWidth % 2 == 0 ? correctWidth + 1 : correctWidth;

        this.bomber = bomber;
        game = this;

        createUnbreakableWalls();
        createWalls(countWalls);
        createBonus();
    }

    public static void main(String[] args) {
        Bomber bomber = Bomber.getInstance();
        new Game(10, 10, 20, bomber);
        game.start();
    }

    private void start() {
        KeyboardObserver keyboardObserver = new KeyboardObserver();
        keyboardObserver.start();
        Drawing drawing = new Drawing(this);

        while (bomber.isAlive()) {
            if (checkBombs()) {
                checkFlames();
            }

            if (keyboardObserver.hasKeyEvents()) {
                KeyEvent event = keyboardObserver.getEventFromTop();

                switch (event.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> bomber.move(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> bomber.move(Direction.RIGHT);
                    case KeyEvent.VK_UP -> bomber.move(Direction.UP);
                    case KeyEvent.VK_DOWN -> bomber.move(Direction.DOWN);
                    case KeyEvent.VK_SPACE -> createBomb();
                }
            }

            checkBonus();
            drawing.printGameField();
            flames.clear();

            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("========================================== Game Over ==========================================");
    }

    private boolean checkBombs() {
        if (bombs.isEmpty()) {
            return false;
        }
        Iterator<Bomb> iterator = bombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            if (bomb.isExploded()) {
                flames.addAll(bomb.getFlame());
                iterator.remove();
            }
        }
        return !flames.isEmpty();
    }

    private void checkFlames() {
        flames.forEach(flame -> {
            walls.removeIf(flame::equals);

            if (touchBomber(flame)) {
                bomber.setAlive(false);
            }

            if (touchBonus(flame)) {
                createBonus();
            }
        });
    }

    private void checkBonus() {
        if (touchBonus(bomber)) {
            Bomb.levelUp();
            createBonus();
        }
    }

    private void createUnbreakableWalls() {
        for (int x = 1; x < height; x += 2) {
            for (int y = 1; y < width; y += 2) {
                unbreakableWalls.add(new UnbreakableWall(x, y));
            }
        }
    }

    private void createWalls(int countWalls) {
        while (walls.size() != countWalls) {
            int x = (int) (Math.random() * height);
            int y = (int) (Math.random() * width);
            Wall newWall = new Wall(x, y);

            if (!touchBomberOrAnyWall(newWall)) {
                walls.add(newWall);
            }
        }
    }

    private void createBomb() {
        Bomb newBomb = new Bomb(bomber.getX(), bomber.getY());
        if (!touchBombs(newBomb)) {
            bombs.add(newBomb);
        }
    }

    private void createBonus() {
        while (true) {
            int x = (int) (Math.random() * height);
            int y = (int) (Math.random() * width);
            Bonus newBonus = new Bonus(x, y);

            if (!touchBomberOrAnyWall(newBonus)) {
                bonus = newBonus;
                return;
            }
        }
    }

    private boolean touchBomberOrAnyWall(Coordinates object) {
        return touchBomber(object)
                || touchUnbreakableWalls(object)
                || touchWalls(object);
    }

    public boolean touchWalls(Coordinates object) {
        return walls.contains(object);
    }

    public boolean touchBonus(Coordinates object) {
        return bonus.equals(object);
    }

    public boolean touchUnbreakableWalls(Coordinates object) {
        return unbreakableWalls.contains(object);
    }

    public boolean touchBomber(Coordinates object) {
        return bomber.equals(object);
    }

    public boolean touchBombs(Coordinates object) {
        return bombs.contains(object);
    }

    public boolean violateBoundaries(Coordinates object) {
        return object.getX() < 0 || object.getX() >= height || object.getY() < 0 || object.getY() >= width;
    }

    public static Game getGame() {
        return game;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Bomber getBomber() {
        return bomber;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public List<UnbreakableWall> getUnbreakableWalls() {
        return unbreakableWalls;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public List<Bomb.Flame> getFlames() {
        return flames;
    }
}