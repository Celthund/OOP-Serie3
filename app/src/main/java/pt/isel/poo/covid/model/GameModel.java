package pt.isel.poo.covid.model;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Class that handles the game logic.
 */
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
    }

    /**
     * Load a level from asset levels.txt
     * @param levelNumber level number to be loaded.
     */
    public void loadLevelFromAssets(int levelNumber){
        try {
            InputStream inFile = assets.open("levels.txt");
            Scanner in = new Scanner(inFile);
            loadLevel(in, levelNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save to a file the current game state.
     * @param to file to be written to.
     */
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

    /**
     * Load a level from a file the first position must be the level number.
     * @param from file to be read from.
     */
    public void loadLevel(Scanner from){
        int levelNumber = from.nextInt();
        try {
            level = new Loader(from).load(levelNumber);
        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a specific level number from a file.
     * @param from file to be read from.
     * @param number level number to be read.
     */
    private void loadLevel(Scanner from, int number){
        Loader loader = new Loader(from);
        try {
            level = loader.load(number);
        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * Move the player by one to a given direction.
     * @param direction direction to move the player.
     */
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
                    else if (!element.elementColision()
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
                    else if (!element.elementColision()
                            && level.isPositionInbounds(right2) && level.get(right2) == null){
                        level.moveElement(right1, right2);
                        level.moveElement(playerPosition, right1);
                    }
                }
                break;
        }
        update();
    }

    /**
     * Return the current level number.
     * @return int current level number.
     */
    public int getNumberLevel(){
        return level.levelNumber;
    }

    /**
     * Return the current level virus counter.
     * @return int current level virus counter.
     */
    public int getVirusCounter(){
        return level.virusCounter;
    }

    /**
     * Return whether the current level player is alive.
     * @return boolean true if player is alive false otherwise.
     */
    public boolean isPlayerAlive(){
        return level.playerAlive;
    }

    /**
     * Removes the element in the current position if below it is a trash can.
     * In case the element is of the type player sets the player alive status to false.
     * @param position position to be checked.
     */
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


    /**
     * Updates isGameOver propriety or loads a new level if the conditions are met.
     */
    public void updateIfLevelIsFinished(){
        if (!level.playerAlive){
            isGameOver = true;
        }
        if (level.virusCounter == 0){
            if (level.levelNumber >= TOTAL_LEVELS)
                isGameOver = true;
            else {
                loadLevelFromAssets(level.levelNumber + 1);
            }
        }
        checkGravity();
    }

    /**
     * Moves (if possible) elements with gravity down one position.
     */
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

    /**
     * Update the current model listener.
     */
    public void update() {
        modelListener.update(level);
    }

}
