package pl.filipmoszczynski;

public class Player {
    private final String nick;
    private int points;

    public Player(String nick) {
        this.nick = nick;
    }
    public Player(String nick, int points) {
        this.nick = nick;
        this.points = points;
    }

    public String getNick() {
        return nick;
    }
    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
