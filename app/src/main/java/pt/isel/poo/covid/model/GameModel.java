package pt.isel.poo.covid.model;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ListIterator;
import java.util.Scanner;

public class GameModel {
    private ModelListener modelListener;
    public boolean isGameOver = false;
    public enum Direction {LEFT, RIGHT}
    private final int TOTAL_LEVELS = 2;
    private Level level;
    private AssetManager assets;

    public GameModel(ModelListener modelListener, AssetManager assets){
        this.modelListener = modelListener;
        this.assets = assets;
        loadFromAssets(1);
    }

    public void loadFromAssets(int levelNumber){
        try {
            InputStream inFile = assets.open("levels.txt");
            Scanner in = new Scanner(inFile);
            level = loadLevel(in,levelNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(PrintWriter to){
        to.print(level.levelNumber + "\n\n");
        to.printf("#%s %s x %s\n", level.levelNumber, level.getLine(), level.getColumn());
        for(int i = 0; i < level.getColumn(); i++){
            for(int j = 0; j < level.getLine(); j++){
                Element element = level.get(i,j);
                if (element == null)
                    to.print(".");
                else
                    element.save(to);
            }
            to.print("\n");
        }
    }

    public void load(Scanner from){
        int levelNumber = from.nextInt();
        try {
            level = new Loader(from).load(levelNumber);
        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
        }
    }

    private Level loadLevel(Scanner in, int number){
        Loader loader = new Loader(in);
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

    public boolean isPlayerAlive(){
        return level.playerAlive;
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

    public void onBeat(){
        if (!level.playerAlive){
            isGameOver = true;
        }
        if (level.virusCounter == 0){
            if (level.levelNumber >= TOTAL_LEVELS)
                isGameOver = true;
            else {
                loadFromAssets(level.levelNumber + 1);
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
