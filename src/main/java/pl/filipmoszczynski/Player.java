package pl.filipmoszczynski;

public class Player {
    private final String nick;
    private int points;
    private int pointsAgainstMachine;

    public Player(String nick) {
        this.nick = nick;
    }
    public Player(String nick, int points) {
        this.nick = nick;
        this.points = points;
    }

    public Player(String nick, int points, int pointsAgainstMachine) {
        this.nick = nick;
        this.points = points;
        this.pointsAgainstMachine = pointsAgainstMachine;
    }

    public String getNick() {
        return nick;
    }
    public int getPoints() {
        return points;
    }
    public int getPointsAgainstMachine() {
        return pointsAgainstMachine;
    }

    public void addPoints(int points) {
        this.points += points;
    }
    public void addPointsAgainstMachine(int points) {
        this.pointsAgainstMachine += points;
    }
    public void addMachinePoints(int points) {
    }

}
