package pt.isel.poo.covid.model;

import java.util.ArrayList;
import java.util.HashMap;

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
        availableElements.put("@", new Player());
        availableElements.put("V", new Trash());
        availableElements.put("*", new Virus());
        availableElements.put("X", new Floor());
    }

    public int getColumn(){
        return board[0].length;
    }

    public int getLine(){
        return board.length;
    }

    public int getNumber() {
        return levelNumber;
    }


    public void reset() {
        for (int i = 0; i < getLine(); i++){
            for (int j = 0; j < getColumn(); j++){
                board[i][j] = null;
            }
        }
        gravityElements.clear();
    }

    public Element get(int line, int column){
        return board[line][column];
    }

    public Element get(Position position){
        return board[position.line][position.column];
    }

    public boolean isPositionInbounds(int line, int column){
        return (line >= 0 && line < getLine() && column >= 0 && column < getColumn());
    }

    public boolean isPositionInbounds(Position position){
        if (position == null)
            return false;
        return isPositionInbounds(position.line ,position.column);
    }

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

    public void decreaseVirus(){
        --virusCounter;
    }

    public void removeElement(Position position){
        board[position.line][position.column] = null;
    }

    private void updatePositionInGravityElements(Position oldPosition, Position newPosition){
        for (Position pos: gravityElements) {
            if (pos.column == oldPosition.column && pos.line == oldPosition.line){
                pos.column = newPosition.column;
                pos.line = newPosition.line;
            }
        }
    }

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
