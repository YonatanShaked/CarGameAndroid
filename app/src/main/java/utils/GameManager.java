package utils;

import java.util.Arrays;
import java.util.Random;

public class GameManager {
    public enum Direction {Left, Right}
    private int lifeCount;
    private boolean[] lives;
    private boolean[] car;
    private int carPosition;
    private boolean[][] barriers;
    private final int barrierRows;
    private final int barrierCols;
    private boolean crash ;
    private boolean gameOver;

    public GameManager(int lifeCount, int barrierRows, int barrierCols) {
        this.lifeCount = lifeCount;
        this.barrierRows = barrierRows;
        this.barrierCols = barrierCols;
    }

    public void moveCar(Direction direction) {
        if (direction == Direction.Left) {
            int pos = carPosition - 1;
            if (pos >= 0) {
                car[carPosition] = false;
                car[pos] = true;
                carPosition = pos;
            }
        }

        if (direction == Direction.Right) {
            int pos = carPosition + 1;
            if (pos < barrierCols) {
                car[carPosition] = false;
                car[pos] = true;
                carPosition = pos;
            }
        }
    }

    public void init() {
        initLives();
        initBarriers();
        initCars();
    }

    private void initLives() {
        lives = new boolean[lifeCount];
        Arrays.fill(lives, true);
    }

    private void initCars() {
        car = new boolean[barrierCols];
        Arrays.fill(car, false);
        carPosition = 1;
        car[1] = true;
    }

    private void initBarriers() {
        barriers = new boolean[barrierRows][barrierCols];
        for (int i = 0; i < barrierRows; i++)
            for (int j = 0; j < barrierCols; j++)
                barriers[i][j] = false;
    }

    public void updateGame() {
        updateObstacles();
        addBarrier();
    }

    private void addBarrier() {
        int rand = new Random().nextInt(barrierCols);
        for (int i = 0; i < barrierCols; i++)
            barriers[0][i] = (rand == i);
    }

    private void updateObstacles() {
        for (int i = barrierRows - 1; i >= 0; i--) {
            for (int j = 0; j < barrierCols; j++) {
                if (i == barrierRows - 1 && barriers[i][j]) {
                    barriers[i][j] = false;
                    if (j == carPosition) { // crash
                        crash = true;
                        lifeCount = lifeCount - 1;
                        lives[lifeCount] = false;
                        if (lifeCount == 0) // game over
                            gameOver = true;
                    }
                } else if (i != barrierRows - 1)
                    barriers[i + 1][j] = barriers[i][j];
            }
        }
    }

    public boolean[] getLives() {
        return lives;
    }

    public void setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
    }

    public boolean getGameOver() { return this.gameOver; }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getCrash() {
        return this.crash;
    }

    public void setCrash(boolean crash) {
        this.crash = crash;
    }

    public boolean[] getCar() {
        return car;
    }

    public boolean[][] getBarriers() {
        return barriers;
    }
}