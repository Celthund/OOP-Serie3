package pt.isel.poo.covid.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that keeps the information of a game level.
 */
public class Level {
    int levelNumber;
    boolean playerAlive = true;
    int virusCounter = 0;
    Position playerPosition;
    ArrayList<Position> gravityElements;
    HashMap<String, Element> availableElements;
    Element[][] board;

    public Level(int levelNumber, int line, int column) {
        this.levelNumber = levelNumber;
        board = new Element[line][column];
        gravityElements = new ArrayList<>();
        availableElements = new HashMap<>();
        availableElements.put(Player.character, new Player());
        availableElements.put(Trash.character, new Trash());
        availableElements.put(Virus.character, new Virus());
        availableElements.put(Floor.character, new Floor());
    }

    /**
     * Return number of existing columns.
     * @return int number of columns.
     */
    public int getColumn(){
        return board[0].length;
    }

    /**
     * Return number of existing lines.
     * @return int number of lines.
     */
    public int getLine(){
        return board.length;
    }

    /**
     * Return level number.
     * @return int level number.
     */
    public int getNumber() {
        return levelNumber;
    }

    /**
     * Resets level to blank to be initialized again.
     */
    public void reset() {
        for (int i = 0; i < getLine(); i++){
            for (int j = 0; j < getColumn(); j++){
                board[i][j] = null;
            }
        }
        gravityElements.clear();
    }

    /**
     * Return element given a line and column.
     * @param line line to retrieve element from.
     * @param column column to retrieve element from.
     * @return Element found in the line and column (can be null).
     */
    public Element get(int line, int column){
        return board[line][column];
    }


    /**
     * Return element given a line and column.
     * @param position position to retrieve the element from.
     * @return Element found in the position (can be null).
     */
    public Element get(Position position){
        return board[position.line][position.column];
    }

    /**
     * Checks if the line and column given is inbounds.
     * @param line line to check if its inbounds.
     * @param column column to check if its inbounds.
     * @return True if its inbounds false otherwise.
     */
    public boolean isPositionInbounds(int line, int column){
        return (line >= 0 && line < getLine() && column >= 0 && column < getColumn());
    }

    /**
     * Checks if the position given is inbounds.
     * @param position position to check if its inbounds
     * @return Element
     */
    public boolean isPositionInbounds(Position position){
        if (position == null)
            return false;
        return isPositionInbounds(position.line ,position.column);
    }


    /**
     * Create a Element in a given line and column depending on a char.
     * @param line line to be set the element.
     * @param column column to be set the element.
     * @param type type of the element to be created.
     */
    public void put(int line, int column, char type) {
        if (isPositionInbounds(line, column)){
            Element element = availableElements.get(String.valueOf(type));
            if (element != null){
                if (element.getClass() == Player.class){
                    playerPosition = new Position(line, column);
                    gravityElements.add(playerPosition);
                } else{
                    if (element.getClass() == Virus.class)
                        virusCounter += 1;
                    if (element.hasGravity())
                        gravityElements.add(new Position(line, column));
                }
            }
            board[line][column] = element;
        }
    }


    /**
     * Decrease the current virus counter.
     */
    public void decreaseVirus(){
        --virusCounter;
    }

    /**
     * Remove element in a given position
     * @param position position to remove the element.
     */
    public void removeElement(Position position){
        board[position.line][position.column] = null;
    }

    /**
     * Update Elements with gravity if they changed position.
     * This method is needed for always keeping the same object until its removed.
     * @param oldPosition position to check.
     * @param newPosition new position to update the element with.
     */
    private void updatePositionInGravityElements(Position oldPosition, Position newPosition){
        for (Position pos: gravityElements) {
            if (pos.column == oldPosition.column && pos.line == oldPosition.line){
                pos.column = newPosition.column;
                pos.line = newPosition.line;
            }
        }
    }

    /**
     * Move a element from a given old position to a new one.
     * @param oldPosition position that will be moved from.
     * @param newPosition position that will be move to.
     */
    public void moveElement(Position oldPosition, Position newPosition){
        if (isPositionInbounds(oldPosition) && isPositionInbounds(newPosition)){
            Element element = board[oldPosition.line][oldPosition.column];
            board[oldPosition.line][oldPosition.column] = null;
            board[newPosition.line][newPosition.column] = element;
            if (element.getClass() == Player.class){
                playerPosition.line = newPosition.line;
                playerPosition.column = newPosition.column;
            }
            if (element.hasGravity()){
                updatePositionInGravityElements(oldPosition, newPosition);
            }
        }
    }
}
