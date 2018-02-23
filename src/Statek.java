import java.util.ArrayList;

public class Statek {
    private int size;
    private boolean czy_ustawiony;

    public Statek(int size) {
        this.size = size;
        this.czy_ustawiony = false;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isCzy_ustawiony() {
        return czy_ustawiony;
    }

    public void setCzy_ustawiony(boolean czy_ustawiony) {
        this.czy_ustawiony = czy_ustawiony;
    }
}