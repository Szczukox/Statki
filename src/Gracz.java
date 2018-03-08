public class Gracz {
    private int id_gracza;
    private Plansza plansza;

    public Gracz(int id_gracza) {
        this.id_gracza = id_gracza;
        this.plansza = new Plansza(id_gracza);
    }

    public Plansza getPlansza() {
        return plansza;
    }

}
