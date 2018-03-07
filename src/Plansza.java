import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Plansza extends JPanel {
    public Pole[][] plansza = new Pole[10][10];
    public Pole[][] plansza_przeciwnika = new Pole[10][10];
    public int dlugosc = 0;
    public boolean ustawione;
    private int id_gracza;
    private boolean gotowy;
    public int x;
    public int y;

    BufferedImage pusteImage;
    BufferedImage pudloImage;
    BufferedImage statekImage;
    BufferedImage trafionyImage;
    BufferedImage zatopionyImage;
    BufferedImage propozycjaImage;
    BufferedImage backgroundImage;

    public int getId_gracza() {
        return id_gracza;
    }

   /* public void setId_gracza(int id_gracza) {
        this.id_gracza = id_gracza;
    }*/

    /*public boolean isGotowy() {
        return gotowy;
    }*/

    public void setGotowy(boolean gotowy) {
        this.gotowy = gotowy;
    }

    public Plansza(int id_gracza) {
        init();
        this.id_gracza=id_gracza;
        this.gotowy=false;
        this.ustawione=false;

        setLayout(null);

        try {
            URL imageUrl = Plansza.class.getResource("puste.png");
            pusteImage = ImageIO.read(imageUrl);
            imageUrl = Plansza.class.getResource("pudlo.png");
            pudloImage = ImageIO.read(imageUrl);
            imageUrl = Plansza.class.getResource("statek.png");
            statekImage = ImageIO.read(imageUrl);
            imageUrl = Plansza.class.getResource("trafiony.png");
            trafionyImage = ImageIO.read(imageUrl);
            imageUrl = Plansza.class.getResource("zatopiony.png");
            zatopionyImage = ImageIO.read(imageUrl);
            imageUrl = Plansza.class.getResource("propozycja.png");
            propozycjaImage = ImageIO.read(imageUrl);
            imageUrl = Plansza.class.getResource("background.png");
            backgroundImage = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Statek statek1 = new Statek(1);
        Statek statek2 = new Statek(2);
        Statek statek3 = new Statek(3);
        Statek statek4 = new Statek(4);
        Statek statek5 = new Statek(5);
        ArrayList<Statek> statki = new ArrayList<Statek>();

        statki.add(statek1);
        statki.add(statek2);
        statki.add(statek3);
        statki.add(statek4);
        statki.add(statek5);*/


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();

                if (p.x <= 500 && p.y <= 500  && !gotowy) {
                    x = p.x / 50;
                    y = p.y / 50;
                    ustaw_statki(x,y,e);
                }
                if(p.x >=600 && p.x<=1100 && p.y<=500 && gotowy && Main.getInstance().isToken()==true){
                    x=(p.x-600)/50;
                    y=p.y/50;
                    Events event = new Events(Events.CLIENT_SHOT);
                    event.setMessage(x + "" + y);
                    Main.getInstance().client.setMessage(Integer.parseInt(y+""+x));
                    Main.getInstance().setToken(false);
                    Main.getInstance().setStrzał_x(x);
                    Main.getInstance().setStrzał_y(y);
                }
            }
        });

    }





    public void ustaw_statki(int x, int y, MouseEvent e){
        if (!ustawione) {
            if (getId_gracza() == 0) {
                boolean czy_wolne = true;

                if (dlugosc < 5) {
                    dlugosc++;
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        for (int i = 0; i <= dlugosc; i++) {
                            if ((x + 1) >= 10 || (y + i) >= 10)
                                continue;
                            if (plansza[x + 1][y + i] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                                break;
                            }
                        }
                        for (int i = 0; i <= dlugosc; i++) {
                            if ((x) < 0 || (y + i) >= 10)
                                continue;
                            if (plansza[x][y + i] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                                break;
                            }
                        }
                        for (int i = 0; i <= dlugosc; i++) {
                            if ((x - 1) < 0 || (y + i) >= 10)
                                continue;
                            if (plansza[x - 1][y + i] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                                break;
                            }
                        }
                        if (y - 1 >= 10 || y - 1 < 0) {
                        } else if (plansza[x][y - 1] != Pole.POLE_PUSTE) {
                            czy_wolne = false;
                        }
                        if (y + dlugosc >= 10) {
                        } else {
                            if (plansza[x][y + dlugosc] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                            }
                        }
                        if ((x - 1 < 0) || (y - 1 < 0)) {
                        } else {
                            if (plansza[x - 1][y - 1] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                            }
                        }
                        if ((x + 1) >= 10 || (y - 1) < 0) {
                        } else {
                            if (plansza[x + 1][y - 1] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                            }
                        }

                        if (y + dlugosc - 1 >= 10) {
                            czy_wolne = false;
                        }

                        if (plansza[x][y] == Pole.POLE_PUSTE && czy_wolne) {
                            for (int i = 0; i < dlugosc; i++) {
                                plansza[x][y + i] = Pole.STATEK;
                            }
                        }
                        if (czy_wolne == false) {
                            czy_wolne = true;
                            dlugosc--;
                        }
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        for (int i = 0; i <= dlugosc; i++) {
                            if ((x + i) >= 10 || (y + 1) >= 10)
                                continue;
                            if (plansza[x + i][y + 1] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                                break;
                            }
                        }
                        for (int i = 0; i <= dlugosc; i++) {
                            if ((x + i) >= 10 || (y) < 0)
                                continue;
                            if (plansza[x + i][y] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                                break;
                            }
                        }
                        for (int i = 0; i <= dlugosc; i++) {
                            if ((x + i) >= 10 || (y - 1) < 0)
                                continue;
                            if (plansza[x + i][y - 1] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                                break;
                            }
                        }
                        if ((x - 1) >= 10 || x - 1 < 0) {
                        } else {
                            if (plansza[x - 1][y] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                            }
                        }
                        if ((x + dlugosc) >= 10) {
                        } else {
                            if (plansza[x + dlugosc][y] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                            }
                        }
                        if ((x - 1) < 0 || (y + 1) >= 10) {
                        } else {
                            if (plansza[x - 1][y + 1] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                            }
                        }
                        if ((x - 1) < 0 || (y - 1) < 0) {
                        } else {
                            if (plansza[x - 1][y - 1] != Pole.POLE_PUSTE) {
                                czy_wolne = false;
                            }
                        }

                        if (x + dlugosc - 1 >= 10) {
                            czy_wolne = false;
                        }

                        if (plansza[x][y] == Pole.POLE_PUSTE && czy_wolne) {
                            for (int i = 0; i < dlugosc; i++) {
                                plansza[x + i][y] = Pole.STATEK;
                            }
                        }
                        if (czy_wolne == false) {
                            //czy_wolne = true;
                            dlugosc--;
                        }
                    }

                } else if (dlugosc == 5) {
                    ustawione = true;
                }
            }
            repaint();

        }
    }

    public void init() {
        for (int row = 0; row < plansza.length; row++) {
            for (int col = 0; col < plansza.length; col++) {
                plansza[row][col] = Pole.POLE_PUSTE;
            }
        }

        for (int row = 0; row < plansza_przeciwnika.length; row++) {
            for (int col = 0; col < plansza_przeciwnika.length; col++) {
                plansza_przeciwnika[row][col] = Pole.POLE_PUSTE;
            }
        }

    }

    public boolean czyZatopiony(int x, int y) {
        int t = x;
        while (--t >= 0
                && (plansza[t][y] == Pole.STATEK || plansza[t][y] == Pole.STATEK_TRAFIONY))
            if (plansza[t][y] == Pole.STATEK)
                return false;

        t = x;
        while (++t < 10
                && (plansza[t][y] == Pole.STATEK || plansza[t][y] == Pole.STATEK_TRAFIONY))
            if (plansza[t][y] == Pole.STATEK)
                return false;

        t = y;
        while (--t >= 0
                && (plansza[x][t] == Pole.STATEK || plansza[x][t] == Pole.STATEK_TRAFIONY))
            if (plansza[x][t] == Pole.STATEK)
                return false;

        t = y;
        while (++t < 10
                && (plansza[x][t] == Pole.STATEK || plansza[x][t] == Pole.STATEK_TRAFIONY))
            if (plansza[x][t] == Pole.STATEK)
                return false;

        return true;
    }


    public void zaznaczZatopiony(int x, int y,Pole[][] plansza) {
        int x1 = x;
        int x2 = x;
        int y1 = y;
        int y2 = y;

        while (x1 > 0 && plansza[x1][y] == Pole.STATEK_TRAFIONY) {
            x1--;
        }

        while (x2 < 9 && (plansza[x2][y] == Pole.STATEK_TRAFIONY)) {
            x2++;
        }

        while (y1 > 0 && plansza[x][y1] == Pole.STATEK_TRAFIONY) {
            y1--;
        }

        while (y2 < 9 && (plansza[x][y2] == Pole.STATEK_TRAFIONY)) {
            y2++;
        }

        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                if (plansza[i][j] == Pole.STATEK_TRAFIONY)
                    plansza[i][j] = Pole.STATEK_ZATOPIONY;
    }


    @Override
    public void paint(Graphics g) {
        Image img = createImage(getSize().width, getSize().height);

        Graphics2D g2 = (Graphics2D) img.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(0, 6, 6);
        AffineTransformOp op = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        g2.drawImage(op.filter(backgroundImage, null), 0, 0, this);

        g2.setColor(Color.black);
        g2.fillRect(0, 0, 502, 502);
        for (int i = 0; i < plansza.length; i++) {
            for (int j = 0; j < plansza[i].length; j++) {
                if (plansza[i][j] == Pole.POLE_PUSTE) {
                    g2.drawImage(op.filter(pusteImage, null), 2 + i * 50, 2 + j * 50, this);
                }
                if (plansza[i][j] == Pole.PUDLO) {
                    g2.drawImage(op.filter(pudloImage, null), 2 + i * 50, 2 + j * 50, this);
                }
                if (plansza[i][j] == Pole.STATEK) {
                    g2.drawImage(op.filter(statekImage, null), 2 + i * 50, 2 + j * 50, this);
                }
                if (plansza[i][j] == Pole.STATEK_TRAFIONY) {
                    g2.drawImage(op.filter(trafionyImage, null), 2 + i * 50, 2 + j * 50, this);
                }
                if (plansza[i][j] == Pole.STATEK_ZATOPIONY) {
                    g2.drawImage(op.filter(zatopionyImage, null), 2 + i * 50, 2 + j * 50, this);
                }
            }
        }

        g2.setColor(Color.black);
        g2.fillRect(600, 0, 1102, 502);
        for (int i = 0; i < plansza_przeciwnika.length; i++) {
            for (int j = 0; j < plansza_przeciwnika[i].length; j++) {
                if (plansza_przeciwnika[i][j] == Pole.POLE_PUSTE) {
                    g2.drawImage(op.filter(pusteImage, null), 602 + i * 50, 2 + j * 50, this);
                }
                if (plansza_przeciwnika[i][j] == Pole.PUDLO) {
                    g2.drawImage(op.filter(pudloImage, null), 602 + i * 50, 2 + j * 50, this);
                }
                if (plansza_przeciwnika[i][j] == Pole.STATEK) {
                    g2.drawImage(op.filter(statekImage, null), 602 + i * 50, 2 + j * 50, this);
                }
                if (plansza_przeciwnika[i][j] == Pole.STATEK_TRAFIONY) {
                    g2.drawImage(op.filter(trafionyImage, null), 602 + i * 50, 2 + j * 50, this);
                }
                if (plansza_przeciwnika[i][j] == Pole.STATEK_ZATOPIONY) {
                    g2.drawImage(op.filter(zatopionyImage, null), 602 + i * 50, 2 + j * 50, this);
                }
                if (plansza_przeciwnika[i][j] == Pole.STRZAL_PROPOZYCJA) {
                    g2.drawImage(op.filter(propozycjaImage, null), 602 + i * 50, 2 + j * 50, this);
                }
            }
        }
        g.drawImage(img, 0, 0, this);
        paintComponents(g);
    }


}
