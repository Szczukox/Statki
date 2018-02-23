public class Events {

    /** OCZEKIWANIE NA DRUGIEGO GRACZA */
    public static final int CLIENT_LOGIN = 1001;

    /** GRA ROZPOCZĘTA */
    public static final int CLIENT_CONNECTED= 1002;
    /** KLIENT MOZE STRZELAC */
    public static final int CLIENT_CAN_SHOOT= 1003;
    /** KLIENT STRZELIL */
    public static final int CLIENT_SHOT=1004;
    /** ZAKONCZENIE GRY */
    public static final int CLIENT_LOOSE=1005;

    private int game_event;

    private String playerId = "";

    private String message;

    public Events() {

    }

    public Events(int type) {
        setType(type);
    }

    public Events(int type, String message) {
        this(type);
        this.message = message;
    }

    public Events(String receivedMessage) {
        String x = receivedMessage;
        try {
            setType(Integer.parseInt(x));
        } catch (NumberFormatException ex) {
            setType(-1);
        }
    }



    public Integer toSend() {
        int toSend = game_event;
        return toSend;
    }

    public void setType(int type) {
        game_event = type;
    }

    public int getType() {
        return game_event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String id) {
        playerId = id;
    }


}
