package pt.isel.poo.covid.model;

public class Trash extends Element {
    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public boolean elementColision(Element e) {
        return false;
    }
}
