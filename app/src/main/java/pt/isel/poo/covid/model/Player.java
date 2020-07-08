package pt.isel.poo.covid.model;

import java.io.PrintWriter;

public class Player extends Element{

    static final String character = "@";

    /**
     * Checks whether if the element has gravity.
     * @return boolean true if the element can fall down otherwise false.
     */
    @Override
    public boolean hasGravity() {
        return true;
    }

    /**
     * Saves the element to a file using PrintWriter.
     * @param to The PrinterWriter to use
     */
    @Override
    public void save(PrintWriter to) {
        to.print(character);
    }

    /**
     * Checks whether if the object can collide with other elements.
     * @return boolean true if the element can collide with other elements.
     */
    @Override
    public boolean elementColision() {
        return false;
    }
}
