package pt.isel.poo.covid.model;

public class Virus extends Element {
    @Override
    public boolean hasGravity() {
        return true;
    }

    @Override
    public boolean elementColision(Element e) {
        return false;
    }
}
