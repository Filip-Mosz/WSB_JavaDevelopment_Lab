package pl.filipmoszczynski;

public class Player {
    private final String nick;
    private int points;
    private int pointsAgainstMachine;
    private int machinePoints;

    public Player(String nick) {
        this.nick = nick;
    }
    public Player(String nick, int points) {
        this.nick = nick;
        this.points = points;
    }

    public Player(String nick, int points, int pointsAgainstMachine, int machinePoints) {
        this.nick = nick;
        this.points = points;
        this.pointsAgainstMachine = pointsAgainstMachine;
        this.machinePoints = machinePoints;
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
    public int getMachinePoints() {
        return machinePoints;
    }

    public void addPoints(int points) {
        this.points += points;
    }
    public void addPointsAgainstMachine(int points) {
        this.pointsAgainstMachine += points;
    }
    public void addMachinePoints(int points) {
        this.machinePoints += points;
    }

}
