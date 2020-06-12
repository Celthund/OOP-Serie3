package pt.isel.poo.covid.model;

import java.util.ListIterator;
import java.util.Scanner;

public class GameModel {
    private ModelListener modelListener;
    public boolean isGameOver = false;
    public enum Direction {LEFT, RIGHT}
    private final int TOTAL_LEVELS = 2;
    private Level level;
    private Loader loader;

    public GameModel(ModelListener modelListener, Scanner in){
        this.modelListener = modelListener;
        loader = new Loader(in);
        level = loadLevel(1);

    }

    private Level loadLevel(int number){
        Level level = null;
        try {
            level = loader.load(number);
        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
        }
        return level;
    }

    public void movePlayer(Direction direction){
        Position playerPosition = level.playerPosition;
        Element element;
        switch (direction){
            case LEFT:
                Position left1 = new Position(playerPosition.line, playerPosition.column - 1);
                Position left2 = new Position(playerPosition.line, playerPosition.column - 2);
                if (level.isPositionInbounds(left1)){
                    element = level.get(left1);
                    if (element == null){
                        level.moveElement(playerPosition, left1);
                    }
                    else if (!element.elementColision(level.get(playerPosition))
                            && level.isPositionInbounds(left2) && level.get(left2) == null){
                        level.moveElement(left1, left2);
                        level.moveElement(playerPosition, left1);
                    }
                }
                break;
            case RIGHT:
                Position right1 = new Position(playerPosition.line, playerPosition.column + 1);
                Position right2 = new Position(playerPosition.line, playerPosition.column + 2);
                if (level.isPositionInbounds(right1)){
                    element = level.get(right1);
                    if (element == null){
                        level.moveElement(playerPosition, right1);
                    }
                    else if (!element.elementColision(level.get(playerPosition))
                            && level.isPositionInbounds(right2) && level.get(right2) == null){
                        level.moveElement(right1, right2);
                        level.moveElement(playerPosition, right1);
                    }
                }
                break;
        }
        update();
    }

    public int getNumberLevel(){
        return level.levelNumber;
    }

    public int getVirusCounter(){
        return level.virusCounter;
    }

    private void checkForTrash(Position position){
        Position down = new Position(position.line + 1, position.column);
        if (level.isPositionInbounds(down) && level.get(down) != null){
            if (level.get(down).getClass() == Trash.class && level.get(position) != null){
                if (level.get(position).getClass() == Player.class){
                    level.playerAlive = false;
                }
                else if (level.get(position).getClass() == Virus.class){
                    level.removeElement(position);
                    level.decreaseVirus();
                    update();
                }
            }
        }
    }

    public boolean isLevelFinished(){
        return level.virusCounter == 0;
    }

    public void onBeat(){
        if (!level.playerAlive){
            isGameOver = true;
        }
        if (level.virusCounter == 0){
            if (level.getNumber() >= TOTAL_LEVELS)
                isGameOver = true;
            else {
                level = loadLevel(level.levelNumber + 1);
            }
        }
        checkGravity();
    }

    public void checkGravity(){
        ListIterator<Position> iterator = level.gravityElements.listIterator();
        while (iterator.hasNext()){
            Position curr = iterator.next();
            Position down = new Position(curr.line + 1, curr.column);
            if (level.isPositionInbounds(down)){
                checkForTrash(curr);
                if (level.get(curr) == null) {
                    iterator.remove();
                }
                else if (level.get(down) == null) {
                    level.moveElement(curr, down);
                    curr.line = down.line;
                    curr.column = down.column;
                }
            }
        }
        update();
    }

    public void update() {
        modelListener.update(level);
    }

}
