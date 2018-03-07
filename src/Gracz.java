public class Gracz {
    private int id_gracza;
    private Plansza plansza;

    public Gracz(int id_gracza) {
        this.id_gracza = id_gracza;
        this.plansza = new Plansza(id_gracza);
    }

    /*public int getId_gracza() {
        return id_gracza;
    }*/

   /* public void setId_gracza(int id) {
        this.id_gracza = id;
    }*/

    public Plansza getPlansza() {
        return plansza;
    }

   /* public void setPlansza(Plansza plansza) {
        this.plansza = plansza;
    }*/


}
