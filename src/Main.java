import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Main extends JPanel {

    private BufferedImage img;
    boolean clientStarted = false;
    Client client;
    private boolean token = false;
    private static Main instance = null;
    Gracz gracz = new Gracz(0);
    private int strzał_x;
    private int strzał_y;
    int statki_moje=5;
    int statki_przeciwnika=5;
    int propozycja = 0;

    private JFrame frame;

    JLabel twojRuch;
    JLabel ruchPrzeciwnika;

    public int getStrzał_x() {
        return strzał_x;
    }

    public void setStrzał_x(int strzał_x) {
        this.strzał_x = strzał_x;
    }

    public int getStrzał_y() {
        return strzał_y;
    }

    public void setStrzał_y(int strzał_y) {
        this.strzał_y = strzał_y;
    }

    public static Main getInstance() {
        return instance;
    }




    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                    Main thisClass = new Main();
                    thisClass.setVisible(true);
            }
        });
    }

    public Main(){
        super();
        instance = this;
        JFrame frame = new JFrame("Statki");
        this.frame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
   //     Gracz gracz1 = new Gracz(1);
        JButton bt = new JButton("Graj");
        JButton bt2 = new JButton("Wyjdź");
        JLabel label = new JLabel();

        JLabel twojRuch = new JLabel("Twój ruch!");
        JLabel ruchPrzeciwnika = new JLabel("Ruch przeciwnika!");

        this.twojRuch = twojRuch;
        this.ruchPrzeciwnika = ruchPrzeciwnika;

        twojRuch.setVisible(false);
        ruchPrzeciwnika.setVisible(false);



        label.setText("Wybierz ustawienie statków");

        label.setHorizontalAlignment(SwingConstants.RIGHT);

        JTextField textField = new JTextField();

        textField.setVisible(true);
        textField.setLocation(new Point(287,350));
        textField.setSize(new Dimension(132, 20));
        textField.setText("127.0.0.1");
        textField.setVisible(false);

        JTextField textField1 = new JTextField();
        textField1.setVisible(true);
        textField1.setLocation(new Point(287,400));
        textField1.setSize(new Dimension(132, 20));
        textField1.setText("3490");
        textField1.setVisible(false);

        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {

            }
        });
        textField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {

            }
        });

        label.setBounds(new Rectangle(300, 12, 67, 16));
        gracz.getPlansza().setBounds(new Rectangle(0, 0, 702, 302));
   //     gracz1.getPlansza().setBounds(new Rectangle(400, 0, 702, 702));
        bt.setBounds(new Rectangle(287, 450, 132, 50));
        bt2.setBounds(new Rectangle(287, 525, 132, 50));

        twojRuch.setBounds(new Rectangle(100, 350, 200, 50));
        ruchPrzeciwnika.setBounds(new Rectangle(503, 350, 200, 50));


        bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(gracz.getPlansza().dlugosc==5) {
                    gracz.getPlansza().setGotowy(true);
                    //gracz1.getPlansza().setGotowy(true);
                    String id = textField.getText();
                    client = new Client();
                    client.setHost(textField.getText());
                    client.setPort(Integer.parseInt(textField1.getText()));
                    if (client.start()) {
                        Events ge = new Events(Events.CLIENT_LOGIN);
                        sendMessage(ge);
                    }
                    System.out.println("Serwer pomyślnie uruchomiony!\nOczekiwanie na drugiego gracza...\n");
                    JOptionPane.showMessageDialog(frame, "Oczekiwanie na drugiego gracza");
         //           client.getConnection().sendId(id);
               //     client.getConnection().sendMessage(33);
                    bt.setEnabled(false);
                    textField.setEnabled(false);
                    textField1.setEnabled(false);
                    clientStarted=true;
                } else {
                    JOptionPane.showMessageDialog(frame, "Nie ustawiono wszystkich statków");
                }
            }
        });

        bt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });



        frame.getContentPane().add(bt);
        frame.getContentPane().add(bt2);
        frame.getContentPane().add(gracz.getPlansza());
       // frame.getContentPane().add(gracz1.getPlansza());
        frame.getContentPane().add(label);
        frame.getContentPane().add(textField);
        frame.getContentPane().add(textField1);

        frame.getContentPane().add(twojRuch);
        frame.getContentPane().add(ruchPrzeciwnika);


        frame.setSize(707, 707);
        frame.setResizable(false);
        frame.setLayout(null);

        frame.setVisible(true);

        new Thread(() -> {
            while (true) {
                if (client != null && client.isAlive()) {
         //           client.getConnection().run();
                    processMessages();
                    if(statki_przeciwnika==-1){
                        twojRuch.setVisible(false);
                        ruchPrzeciwnika.setVisible(false);
                        JOptionPane.showMessageDialog(frame, "Koniec gry - ZWYCIĘSTWO");
                        statki_moje=5;
                        statki_przeciwnika=5;
                        frame.dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                Main thisClass = new Main();
                                thisClass.setVisible(true);
                            }
                        });
                    }
                    if(statki_moje==0){
                    clientStarted=false;
                    client.getConnection().sendMessage(Events.CLIENT_LOOSE);
                    twojRuch.setVisible(false);
                    ruchPrzeciwnika.setVisible(false);
                    JOptionPane.showMessageDialog(frame, "Koniec gry - PORAŻKA");
                    statki_moje=5;
                    statki_przeciwnika=5;
                    frame.dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                Main thisClass = new Main();
                                thisClass.setVisible(true);
                            }
                        });
                    }
                } else if (clientStarted && client != null) {
                    client.stop();
                    client = null;
                    System.out.println("Koniec gry");
                    JOptionPane.showMessageDialog(frame, "Nastąpiło niespodziewane rozłączenie z serwerem");
                    frame.dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            Main thisClass = new Main();
                            thisClass.setVisible(true);
                        }
                    });
                    //    zerwanePolaczenie();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                }
            }
        }).start();

    }


    private void processMessages() {
        Events events;
        while (client != null && client.isAlive()
                && (events = client.receiveMessage()) != null) {
            switch (events.getType()) {
                case Events.CLIENT_CONNECTED:
                    System.out.println("NO i sie polaczyli");
                    break;
                case Events.CLIENT_CAN_SHOOT:
                    Main.getInstance().token=true;
                    twojRuch.setVisible(true);
                    ruchPrzeciwnika.setVisible(false);
                    break;
                case Events.CLIENT_LOOSE:
                    System.out.println("To juz jest koniec");
                    client.stop();
                    client=null;
                    break;


                }
                if(events.getType()<=100){
                int number1;
                int number2;
                number1=events.getType()%10;
                number2=events.getType()/10;
                strzał_x=number1;
                strzał_y=number2;
                    System.out.println("jestem");
                if(gracz.getPlansza().plansza[number1][number2]==Pole.POLE_PUSTE){
                 //   System.out.println("101");
                    client.getConnection().sendMessage(101);
                    gracz.getPlansza().plansza[number1][number2]=Pole.PUDLO;

                }
                    else if(gracz.getPlansza().plansza[number1][number2]==Pole.PUDLO){
                //        System.out.println("102");
                        client.getConnection().sendMessage(102);

                    }
                    else if(gracz.getPlansza().plansza[number1][number2]==Pole.STATEK){
               //         System.out.println("103");
                        gracz.getPlansza().plansza[number1][number2]= Pole.STATEK_TRAFIONY;
                    if(gracz.getPlansza().czyZatopiony(number1,number2)){
                        gracz.getPlansza().zaznaczZatopiony(number1,number2,gracz.getPlansza().plansza);
                        statki_moje--;
                        client.getConnection().sendMessage(105);
                    }else
                        client.getConnection().sendMessage(103);





                }
                    else if(gracz.getPlansza().plansza[number1][number2]==Pole.STATEK_TRAFIONY){
               //         System.out.println("104");
                        client.getConnection().sendMessage(104);


                    }
                    else if(gracz.getPlansza().plansza[number1][number2]==Pole.STATEK_ZATOPIONY){
            //           System.out.println("105");
                        client.getConnection().sendMessage(105);


                    }
                    setToken(false);
                    gracz.getPlansza().repaint();

                }
                if(events.getType()>100 && events.getType()<1000){
                    System.out.println(strzał_x);
                    System.out.println(strzał_y);
                if(events.getType()==101) {
                    gracz.getPlansza().plansza_przeciwnika[strzał_x][strzał_y] = Pole.PUDLO;
                    twojRuch.setVisible(false);
                    ruchPrzeciwnika.setVisible(true);
             //       client.getConnection().sendMessage(111);


                }
                    else if(events.getType()==102) {
                        gracz.getPlansza().plansza_przeciwnika[strzał_x][strzał_y] = Pole.PUDLO;
                        twojRuch.setVisible(false);
                        ruchPrzeciwnika.setVisible(true);
               //         client.getConnection().sendMessage(112);

                    }
                    else if(events.getType()==103) {
                        gracz.getPlansza().plansza_przeciwnika[strzał_x][strzał_y] = Pole.STATEK_TRAFIONY;
                        twojRuch.setVisible(false);
                        ruchPrzeciwnika.setVisible(true);
           //             client.getConnection().sendMessage(113);

                    }
                    else if(events.getType()==104) {
                        gracz.getPlansza().plansza_przeciwnika[strzał_x][strzał_y] = Pole.STATEK_TRAFIONY;
                        twojRuch.setVisible(false);
                        ruchPrzeciwnika.setVisible(true);
           //             client.getConnection().sendMessage(114);

                    }
                    else if(events.getType()==105) {

                        gracz.getPlansza().plansza_przeciwnika[strzał_x][strzał_y] = Pole.STATEK_TRAFIONY;
                        twojRuch.setVisible(false);
                        ruchPrzeciwnika.setVisible(true);
                    gracz.getPlansza().zaznaczZatopiony(strzał_x,strzał_y,gracz.getPlansza().plansza_przeciwnika);
                    statki_przeciwnika--;
                    if(statki_przeciwnika==0){
                        statki_przeciwnika=-1;
                    }

                    //               client.getConnection().sendMessage(115);

                    }

                    gracz.getPlansza().repaint();
                    System.out.println(gracz.getPlansza().plansza_przeciwnika[strzał_x][strzał_y]);
                }
                if(events.getType()>=10000) {
                    int number1;
                    int number2;
                    number1 = propozycja % 10;
                    number2 = propozycja / 10;
                    if (gracz.getPlansza().plansza_przeciwnika[number1][number2] == Pole.STRZAL_PROPOZYCJA){
                        gracz.getPlansza().plansza_przeciwnika[number1][number2] = Pole.POLE_PUSTE;
                    }

                    propozycja = events.getType() - 10000;
                    number1 = propozycja % 10;
                    number2 = propozycja / 10;
                    gracz.getPlansza().plansza_przeciwnika[number1][number2] = Pole.STRZAL_PROPOZYCJA;
                    System.out.println(number1 + "totototoototototot" + number2);
                    System.out.println("gfdgdfgfdgdfgdffdgdfgfdgfd" + gracz.getPlansza().plansza_przeciwnika[number1][number2]);
                    gracz.getPlansza().repaint();
                }
            }
        }

    public boolean sendMessage(Events events) {
        if (client != null && client.isAlive()) {
            client.sendMessage(events);
            return true;
        } else {
            return false;
        }
    }

    public boolean isToken() {
        return token;
    }

    public void setToken(boolean token) {
        this.token = token;
    }


/*
    public Main() {
        super();
        initialize();
    }
    */
/*

    private void initialize() {
        this.setSize(694, 547);
        this.setContentPane(getJContentPane());
        this.setResizable(false);
        this.setTitle("Statki");
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            }
        });
    }*/
    }
