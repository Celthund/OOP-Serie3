package pt.isel.poo.covid.model;

import java.io.PrintWriter;

public abstract class Element {
    public abstract boolean hasGravity();
    public abstract void save(PrintWriter to);
    public abstract boolean elementColision(Element e);
}
