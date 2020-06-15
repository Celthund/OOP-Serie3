package pt.isel.poo.covid.model;

import java.io.PrintWriter;

public class Floor extends Element {

    static final  String character = "X";

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public void save(PrintWriter to) {
        to.print(character);
    }

    @Override
    public boolean elementColision(Element e) {
        return true;
    }
}
