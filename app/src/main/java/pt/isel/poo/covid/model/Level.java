package pt.isel.poo.covid.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Level {
    int levelNumber;
    boolean playerAlive = true;
    int virusCounter;
    Position playerPosition;
    LinkedList<Element> gravityElements;
    HashMap<String, Element> avaiableElements;
    Element[][] board;
    ModelListener modelListener;

    public Level(int levelNumber, int line, int column) {
        this.levelNumber = levelNumber;
        board = new Element[line][column];
        gravityElements = new LinkedList();
        avaiableElements = new HashMap<>();
        avaiableElements.put("P", new Player());
        avaiableElements.put("T", new Trash());
        avaiableElements.put("V", new Virus());
        avaiableElements.put("F", new Floor());
    }

    public Position getPlayerPosition(){
        return
    }

    public int getNumber() {
        return levelNumber;
    }


    public void reset() {
    }

    public void put(int line, int column, char type) {
        put(line,column,avaiableElements.get(String.valueOf(type)));
    }
    public void put(int line, int column, Element element) {
        board[line][column]= element;
    }
    public void update(int line, int column, Element element){
        modelListener.update(line,column,element);
    }

}
