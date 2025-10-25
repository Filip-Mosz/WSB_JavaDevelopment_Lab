package pl.filipmoszczynski;

import java.util.Scanner;

public class Game {
    Player player;
    int round;
    int roundAgainstMachine;
    int difficulty;
    int maxNumber;

    private int machineMaxGuess;
    private int machineMinGuess;

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

    public Game (String nick) {
        Game game  = new Game(nick, 1);
        game = game.read(nick);
        //game.setDifficulty();
        this.player = game.getPlayer();
        this.round = game.getRound();
        this.roundAgainstMachine = game.getRoundAgainstMachine();
        this.difficulty = game.getDifficulty();
    }

    public Game(Player player) {
        Game game  = new Game(player.getNick(), 1);
        game = game.read(player.getNick());
        //game.setDifficulty();
        this.player = player;
        this.round = game.getRound();
        this.roundAgainstMachine = game.getRoundAgainstMachine();
        this.difficulty = game.getDifficulty();
    }

    public Game(String nick, int difficulty) {
        this.player = new Player(nick);
        this.difficulty = difficulty;
    }

    public Game(Player player, int difficulty) {
        this.player = player;
        this.difficulty = difficulty;
    }

    public Game(Player player, int round, int difficulty) {
        this.player = player;
        this.round = round;
        this.difficulty = difficulty;
    }

    public Game(Player player, int round, int roundAgainstMachine, int difficulty) {
        this.player = player;
        this.round = round;
        this.roundAgainstMachine = roundAgainstMachine;
        this.difficulty = difficulty;
    }

    public int getRound() {
        return this.round;
    }

    public int getRoundAgainstMachine() {
        return this.roundAgainstMachine;
    }

    public Player getPlayer() {
        return this.player;
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
            return 10000;
        }
        this.difficulty = 1;
        return 100;
    }

    public int adjustDifficulty(int difficulty) {
        String diffLevel = switch (difficulty) {
            case 1 -> "łatwy";
            case 2 -> "średni";
            case 3 -> "trudny";
            default -> "trudny";
        };
        System.out.printf("Obecny poziom trudności to: %s%nChcesz go zmienić> [t/n]", diffLevel);
        boolean answer = Menu.yesNoMenu();
        if (answer) {
            this.setDifficulty();
        }
        return difficulty;
    }

    void round(int roundNum, int max) {
        int currNum = (int)(Math.random() * max + 1);

        roundMain(roundNum, currNum, false);
    }

    boolean roundHuman(int roundNum, int numberToGuess) {
        return roundMain(roundNum, numberToGuess, true);
    }

    boolean roundMain(int roundNum, int currNum, boolean againstMachine) {
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
                if (againstMachine) {
                    player.addPointsAgainstMachine(points*this.difficulty);
                }else{player.addPoints(points*this.difficulty);}
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
        int min = 0;

        System.out.printf("RUNDA %s  %n", roundNum);
        int attempt = 0;
        boolean guessed = false;
        while (attempt < 3 && !guessed) {
            System.out.printf("Podejście %s%n", attempt+1);
            int guess = (int) ((Math.random() * (max - min)) + min);
            System.out.printf("Komputer zgaduje: %s%n", guess);
            int points = 3 - attempt;
            String pointsText = points == 1 ? "punkt" :"punkty";
            if (guess == numberToGuess) {
                guessed = true;
                player.addMachinePoints(points*this.difficulty);
            } else {
                int hint = guess > numberToGuess ? -1 : 1;
                if(hint == -1) {
                    this.machineMaxGuess = guess;
                }else if(hint == 1) {
                    this.machineMinGuess = guess;
                }
            }
            if (attempt == 2 && !guessed) {
                points = 0;
                pointsText = "punktów";
            }

            if (attempt == 2 || guessed) {
                System.out.printf("Komputer zdobywa %s %s.%n%n", points*this.difficulty, pointsText);
            }
            attempt++;
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                System.out.println("coś nie daje mi spać :(");
            }
        }
        this.round = roundNum;
        return guessed;
    }

    void save(Player player, Game game) {
        FileManager.saveFile(player, game);
    }

    Game read(String nick) {
        String content = FileManager.getPlayer(nick);

        if (content.isEmpty()) {
            Game newGame = new Game(nick, 0);
            newGame.setDifficulty();
            return newGame;
        }

        String[] fields = content.split(":");
        Player playerOut = null;
        Game savedGame = null;

            try {
                playerOut = new Player(nick, Integer.parseInt(fields[0]), Integer.parseInt(fields[2]));
                savedGame = new Game(playerOut, Integer.parseInt(fields[1]), Integer.parseInt(fields[3]), Integer.parseInt(fields[4]));
                System.out.println("Znaleziono zapis gracza; wczytać? [t/n]\nOdmowa rozpoczyna nową grę, a zapis zostanie usunięty.");
                if (!Menu.yesNoMenu()) {
                    Game newGame = new Game(nick,1);
                    newGame.setDifficulty();
                    return newGame;
                }
                System.out.println("Zapis wczytany pomyślnie");
            }
            catch (ArrayIndexOutOfBoundsException e) {
                 Player newPlayer = new Player(nick, 0, 0);
                savedGame = new Game(newPlayer, 0, 0, 1);
                System.out.println("Plik zapisu uszkodzony: Rozpoczęto nową grę.");
            }

        savedGame.adjustDifficulty(savedGame.getDifficulty());
        return savedGame;
    }
}
