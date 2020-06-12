package pt.isel.poo.covid.model;

public class Floor extends Element {
    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public boolean elementColision(Element e) {
        return true;
    }
}
