package pl.filipmoszczynski;

import java.util.*;

public class Game {
    Player[] players;
    int round;
    int roundAgainstMachine;
    int difficulty;
    int maxNumber;
    int roundMultiplayer;

    private int machineMaxGuess;
    private int machineMinGuess;


    public Game (String nick) {
        Game game  = new Game(nick, 1);
        game = game.read(nick, true);
        this.players = game.getPlayers();
        this.round = game.getRound();
        this.roundAgainstMachine = game.getRoundAgainstMachine();
        this.difficulty = game.getDifficulty();
    }

    public Game(Player player) {
        Game game  = new Game(player.getNick(), 1);
        game = game.read(player.getNick(), true);
        this.players = new Player[]{player};
        this.round = game.getRound();
        this.roundAgainstMachine = game.getRoundAgainstMachine();
        this.difficulty = game.getDifficulty();
    }

    public Game(Player[] players) {
        Game game  = new Game(players[0].getNick(), 1);
        game = game.read(players[0].getNick(), true);
        this.players = players;
        this.round = game.getRound();
        this.roundAgainstMachine = game.getRoundAgainstMachine();
        this.difficulty = game.getDifficulty();
    }

    public Game(String nick, int difficulty) {
        this.players = new Player[]{new Player(nick)};
        this.difficulty = difficulty;
    }

    public Game(Player player, int difficulty) {
        this.players = new Player[]{player};
        this.difficulty = difficulty;
    }

    public Game(Player player, int round, int difficulty) {
        this.players = new Player[]{player};
        this.round = round;
        this.difficulty = difficulty;
    }

    public Game(Player player, int round, int roundAgainstMachine, int difficulty) {
        this.players = new Player[]{player};
        this.round = round;
        this.roundAgainstMachine = roundAgainstMachine;
        this.difficulty = difficulty;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

        /**
        *@param difficulty should be equal to difficulty level
        */
    public void setMaxNumber(int difficulty) {
        this.maxNumber = switch (difficulty) {
            case 1 -> 100;
            case 2 -> 1000;
            default -> 10000;
        };
    }

    public int getRound() {
        return this.round;
    }

    public int getRoundAgainstMachine() {
        return this.roundAgainstMachine;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Player getPlayer() {
        return this.players[0];
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int setDifficulty() {//wiem, że setter nie powinien nic zwracać
        System.out.println("Wybierz poziom trudności:");
        System.out.println("[1] łatwy - 0-100");
        System.out.println("[2] średni - 0-1000");
        System.out.println("[3] trudny - 0-10000 ");
        try {
            int option = new Scanner(System.in).nextInt();
            this.difficulty = option;
            switch (option) {
                case 1:
                    System.out.println("No więc łatwy");
                    break;
                case 2:
                    System.out.println("No więc normalny");

                    return  1000;
                case 3:
                    System.out.println("No więc trudny");
                    return 10000;
                default:
                    System.out.println("Skoro nie lubisz prostych instrukcji, to na pewno spodoba Ci się najwyższy poziom trudności :D");
                    return 10000;
            }
        } catch (Exception e) {
            System.out.println("Nie wprowadziłeś liczby całkowitej; no to masz najwyższy poziom trudności");
            this.difficulty = 3;
            return 10000;
        }
        return 100;
    }

    public int getRoundMultiplayer() {
        return roundMultiplayer;
    }

    public void setRoundMultiplayer(int roundMultiplayer) {
        this.roundMultiplayer = roundMultiplayer;
    }

    void setPlayers(Player[] players) {
        this.players = players;
    }

    void setMachineMaxGuess(int max) {
        this.machineMaxGuess = max;
    }

    public int adjustDifficulty(int difficulty) {
        String diffLevel = switch (difficulty) {
            case 1 -> "łatwy";
            case 2 -> "średni";
            case 3 -> "trudny";
            default -> "trudny";
        };
        System.out.printf("Obecny poziom trudności to: %s%nChcesz go zmienić? [t/n]%n", diffLevel);
        boolean answer = Menu.yesNoMenu();
        if (answer) {
            this.setDifficulty();
        }
        return difficulty;
    }


    void round(int roundNum, int max, String playerName) {
        int currNum = (int)(Math.random() * max + 1);

        roundMain(roundNum, currNum, false, playerName);
    }

    boolean roundHuman(int roundNum, int numberToGuess, String playerName) {
        return roundMain(roundNum, numberToGuess, true, playerName);
    }

    boolean roundMain(int roundNum, int currNum, boolean againstMachine, String playerName) {
        int playerIndex = this.findPlayerIndex(playerName);

        System.out.printf("RUNDA %s  %n", roundNum);
        int attempt = 0;
        boolean guessed = false;
        while (attempt < 3 && !guessed) {
            System.out.printf("Podejście %s%n", attempt+1);
            int guess = 0;
            try {
                guess = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                guess = -1;
                System.out.println("Nie wprowadziłeś liczby całkowitej. Tracisz punkt");
            }
            int points = 3 - attempt;
            if (guess == currNum) {
                guessed = true;
                this.machineMaxGuess = this.maxNumber;
                this.machineMinGuess = 0;
                if (againstMachine) {
                    this.players[playerIndex].addPointsAgainstMachine(points*this.difficulty);
                }else{this.players[playerIndex].addPoints(points*this.difficulty);}
            } else {
                String hint = guess > currNum ? "Za wysoko" : "Za nisko";
                System.out.println(hint);
            }
            if (attempt == 2 && !guessed) {
                points = 0;
            }

            if (attempt == 2 || guessed) {
                System.out.printf("Zdobywasz %s pkt.%n%n", points*this.difficulty);
            }
            attempt++;
        }
        this.roundAgainstMachine = roundNum;
        return guessed;
    }

    boolean roundMachine(int roundNum, int max , int numberToGuess) {

        this.setMachineMaxGuess(max);
        System.out.printf("RUNDA %s  %n", roundNum);
        int attempt = 0;
        boolean guessed = false;
        while (attempt < 3 && !guessed) {
            System.out.printf("Podejście %s%n", attempt+1);
            int guess = (int) ((Math.random() * (this.machineMaxGuess - this.machineMinGuess)) + this.machineMinGuess);
            System.out.printf("Komputer zgaduje: %s%n", guess);
            int points = 3 - attempt;
            if (guess == numberToGuess) {
                guessed = true;
                this.machineMaxGuess = max;
                this.machineMinGuess = 0;
                players[0].addMachinePoints(points*this.difficulty);
            } else {
                int botHint = guess > numberToGuess ? -1 : 1;
                if(botHint == -1) {
                    this.machineMaxGuess = guess;
                }else if(botHint == 1) {
                    this.machineMinGuess = guess;
                }
            }
            if (attempt == 2 && !guessed) {
                points = 0;
            }

            if (attempt == 2 || guessed) {
                System.out.printf("Komputer zdobywa %s pkt.%n%n", points*this.difficulty);
            }
            attempt++;
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                System.out.println("coś nie daje mi spać :(");
            }
        }
        this.roundMultiplayer = roundNum++;

        return guessed;
    }

    boolean roundMultiplayer(int roundNum, int max, Player[] players) {
        boolean playGame = true;
        do {
            boolean guessed = false;
            List<Player> playerList = new ArrayList<>(List.of(players));
            Collections.shuffle(playerList);
            players = playerList.toArray(players);
            int numberToGuess = (int) (Math.random() * max + 1);
            do {

                for (Player player : players) {
                    if (guessed) {
                        this.roundMultiplayer = roundNum;
                        continue;
                    }
                    if (player.getNick().equals(Helpers.machineName)) {
                        guessed = this.roundMachine(roundNum, max, numberToGuess);
                    } else {
                        System.out.printf("Zgaduje %s%n", player.getNick());
                        guessed = this.roundHuman(roundNum, numberToGuess, player.getNick());
                    }
                }
                roundNum++;
            }
            while (!guessed);
            //continue
            System.out.println(Helpers.comtinuePrompt);
            boolean answer = Menu.yesNoMenu();
            if (!answer) {
                playGame = false;
            }
            //save
            System.out.println(Helpers.savePrompt);//"zapisać i wyjść do menu?"
            answer = Menu.yesNoMenu();
            if (answer) {
                for (Player player : players) {
                    this.save(player, this);
                }
                for (Player player : players) {
                    String playerName = player.getNick();
                    if (playerName.equals(Helpers.machineName)) {
                        playerName = "Komputer";
                    }
                System.out.printf("Sumarycznie %s zdobył %s pkt.%n", playerName, player.getPointsAgainstMachine());
                }
            }
        } while (playGame);
        return true;
    }

    void save(Player player, Game game) {
        FileManager.saveFile(player, game);
    }

    Game read(String nick, boolean changeDifficulty) {
        String content = FileManager.getPlayerData(nick);

        if (content.isEmpty()) {
            Game newGame = new Game(nick, 0);
            newGame.setDifficulty();
            return newGame;
        }

        String[] fields = content.split(":");
        Player playerOut = null;
        Game savedGame = null;

            try {
                playerOut = new Player(nick, Integer.parseInt(fields[0]), Integer.parseInt(fields[2]), Integer.parseInt(fields[5]));
                savedGame = new Game(playerOut, Integer.parseInt(fields[1]), Integer.parseInt(fields[3]), Integer.parseInt(fields[4]));

                String playerNick = playerOut.getNick();
                if (playerNick.equals(Helpers.machineName)) {playerNick = "Komputer";}
                System.out.printf("Znaleziono zapis gracza %s; wczytać? [t/n]\nOdmowa rozpoczyna nową grę, a zapis zostanie usunięty.%n", playerNick);
                if (!Menu.yesNoMenu()) {
                    Game newGame = new Game(nick,1);
                    newGame.setDifficulty();
                    return newGame;
                }
                System.out.println("Zapis wczytany pomyślnie");
            }
            catch (ArrayIndexOutOfBoundsException e) {
                 Player newPlayer = new Player(nick, 0, 0, 0);
                savedGame = new Game(newPlayer, 0, 0, 1);
                System.out.println("Plik zapisu uszkodzony: Rozpoczęto nową grę.");
            }

        if (changeDifficulty){
            savedGame.adjustDifficulty(savedGame.getDifficulty());
        }
        return savedGame;
    }

    private int findPlayerIndex(String nick){
        for (int i = 0; i < players.length; i++) {
            if (players[i].getNick().equals(nick)) {
                return i;
            }
        }
        return-1;
    }
}
