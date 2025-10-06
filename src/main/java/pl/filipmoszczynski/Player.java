package pl.filipmoszczynski;

public class Gamer {
    final String nick;
    int points;

    public Gamer(String nick) {
        this.nick = nick;
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
