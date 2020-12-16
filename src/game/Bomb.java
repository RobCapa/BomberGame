package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bomb extends Coordinates implements Runnable {
    private static int bombLevel = 3;
    private List<Flame> flames;
    private boolean isExploded;

    public Bomb(int x, int y) {
        super(x, y);
        new Thread(this).start();
    }

    public static void levelUp() {
        bombLevel++;
    }

    /**
     * Добавляет/наращивает взрыв на месте бомбы и в 4 направлениях от нее
     */
    private void createFlame() {
        List<Flame> flames = new ArrayList<>();
        flames.add(new Flame(getX(), getY()));
        for (int i = 1; i <= bombLevel; i++) {
            flames.add(new Flame(getX() + i, getY()));
        }
        for (int i = 1; i <= bombLevel; i++) {
            flames.add(new Flame(getX() - i, getY()));
        }
        for (int i = 1; i <= bombLevel; i++) {
            flames.add(new Flame(getX(), getY() + i));
        }
        for (int i = 1; i <= bombLevel; i++) {
            flames.add(new Flame(getX(), getY() - i));
        }
        validateFlame(flames);
        this.flames = flames;
    }

    /**
     * В flames хранится четкая последовательность пламени от бомбы. Первое пламя - на месте самой бомбы, его проверять не нужно, поэтому сразу пропускаем (iterator.next()).
     * Далее remainingLengthFlame отвечает за оставшуюся длину пламени, которую нужно будет обрезать, если будет встречено препятствие.
     * В цикле получаем пламя, смотрим, соприкоснулось ли оно с чем-нибудь. removeFirstFlame хранит true или false в зависимости от того, является ли встреченное
     * препятствие (концом карты или неразрушимой стенной) или (разрушимой стенной). Если 1 случай, то это пламя нужно удалить, если 2, то оно должно уничтожить стену,
     * значит оставляем.
     * Затем в любом случае убирается все последующее пламя после него, так как дальше оно идти не должно.
     * Ну и если remainingLengthFlame == 0, то это значит, что в одном из 4 направлений мы дошли до конца, значит нужно снова вернуть количество последующего пламени
     * до уровня взрыва бомбы. Так повторять, пока не будет отброшено лишнее пламя по всем 4 направлениям
     */
    private void validateFlame(List<Flame> flames) {
        Iterator<Flame> iterator = flames.iterator();
        iterator.next();
        Game game = Game.getGame();

        int remainingLengthFlame = bombLevel;
        while (iterator.hasNext()) {
            Flame flame = iterator.next();

            boolean removeFirstFlame;
            if ((removeFirstFlame = (game.touchUnbreakableWalls(flame) || game.violateBoundaries(flame))) || game.touchWalls(flame)) {
                if (removeFirstFlame) {
                    iterator.remove();
                }
                for (int i = 0; i < remainingLengthFlame - 1; i++) {
                    iterator.next();
                    iterator.remove();
                }
                remainingLengthFlame = 1;
            }

            remainingLengthFlame--;
            if (remainingLengthFlame == 0) {
                remainingLengthFlame = bombLevel;
            }
        }
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

        @Override
        public String toString() {
            return "Flame{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
