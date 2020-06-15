package pt.isel.poo.covid.model;

import java.io.PrintWriter;

public class Virus extends Element {

    static final  String character = "*";

    @Override
    public boolean hasGravity() {
        return true;
    }

    @Override
    public void save(PrintWriter to) {
        to.print(character);
    }

    @Override
    public boolean elementColision(Element e) {
        return false;
    }
}
