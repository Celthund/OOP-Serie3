package pt.isel.poo.covid.model;

import java.io.PrintWriter;

/**
 * Base class for every model of elements in-game.
 */
public abstract class Element {

    /**
     * Checks whether if the element has gravity.
     * @return boolean true if the element can fall down otherwise false.
     */
    public abstract boolean hasGravity();

    /**
     * Saves the element to a file using PrintWriter.
     * @param to The PrinterWriter to use
     */
    public abstract void save(PrintWriter to);

    /**
     * Checks whether if the object can collide with other elements.
     * @return boolean true if the element can collide with other elements.
     */
    public abstract boolean elementColision();
}
