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

        /*
        * Тут костыль с bomberCheck. Я создаю стену с предполагаемыми новыми Х и Y, чтобы проверить
        * будет ли bomberCheck касаться какой-нибудь стены. Иначе нужно вручную прописывать цикл,
        * в котором будет проход по всем существующим UnbreakableWalls и Walls и сравнение с их полями Х и Y
        */
        Game game = Game.getGame();
        Wall bomberCheck = new Wall(offsetX, offsetY);
        if (offsetX >= 0 && offsetX < game.getHeight() && offsetY >= 0 && offsetY < game.getWidth() &&
            !game.getUnbreakableWalls().contains(bomberCheck) && !game.getWalls().contains(bomberCheck)) {
            x = offsetX;
            y = offsetY;
        }
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
