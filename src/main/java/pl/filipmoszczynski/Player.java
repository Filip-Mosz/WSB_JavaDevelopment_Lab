package pl.filipmoszczynski;

public class Player {
    private final String nick;
    private int points;
    private int pointsAgainstMachine;
    private int pointsMultiplayer;

    public Player(String nick) {
        this.nick = nick;
    }
    public Player(String nick, int points) {
        this.nick = nick;
        this.points = points;
    }

    public Player(String nick, int points, int pointsAgainstMachine, int pointsMultiplayer) {
        this.nick = nick;
        this.points = points;
        this.pointsAgainstMachine = pointsAgainstMachine;
        this.pointsMultiplayer = pointsMultiplayer;
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

    public static Player read(String nick) {
        String content = FileManager.getPlayerData(nick);

        if (content.isEmpty()) {
            return new Player(nick);
        }

        String[] fields = content.split(":");
        Player playerOut = null;

        try {
            playerOut = new Player(nick, Integer.parseInt(fields[0]), Integer.parseInt(fields[2]), Integer.parseInt(fields[5]));
            String playerNick = playerOut.getNick();
            if (playerNick.equals(Helpers.machineName)) {playerNick = "Komputer";}
            System.out.printf("Znaleziono zapis gracza %s; wczytać? [t/n]\nOdmowa rozpoczyna nową grę, a zapis zostanie usunięty.\n", playerNick);
            if (!Menu.yesNoMenu()) {
                return new Player(nick);
            }
            System.out.println("Zapis wczytany pomyślnie");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            playerOut = new Player(nick, 0, 0, 0);
            System.out.println("Plik zapisu uszkodzony: Rozpoczęto nową grę.");
        }

        return playerOut;
    }

}
