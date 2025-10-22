package pl.filipmoszczynski;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.Scanner;

public class Game {
    Player player;
    int round;
    int roundMachine;


    public Game(String nick) {
        this.player = new Player(nick);
    }

    public Game(Player player) {
        this.player = player;
    }

    public Game(Player player, int round) {
        this.player = player;
        this.round = round;
    }

    public Game(Player player, int round, int roundMachine) {
        this.player = player;
        this.round = round;
        this.roundMachine = roundMachine;
    }

    public int getRound() {
        return this.round;
    }
    public int getRoundMachine() {
        return this.roundMachine;
    }

    public Player getPlayer() {
        return this.player;
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
            String pointsText = points == 1 ? "punkt" :"punkty";
            if (guess == currNum) {
                guessed = true;
                if (againstMachine) {
                    player.addPointsAgainstMachine(points);
                }else{player.addPoints(points);}
            } else {
                String hint = guess > currNum ? "Za wysoko" : "Za nisko";
                System.out.println(hint);
            }
            if (attempt == 2 && !guessed) {
                points = 0;
                pointsText = "punktów";
            }

            if (attempt == 2 || guessed) {
                System.out.printf("Zdobywasz %s %s.%n%n", points, pointsText);
            }
            attempt++;
        }
        this.roundMachine = roundNum;
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
                player.addMachinePoints(points);
            } else {
                int hint = guess > numberToGuess ? -1 : 1;
                if(hint == -1) {
                    max = guess;
                }else if(hint == 1) {
                    min = guess;
                }
            }
            if (attempt == 2 && !guessed) {
                points = 0;
                pointsText = "punktów";
            }

            if (attempt == 2 || guessed) {
                System.out.printf("Komputer zdobywa %s %s.%n%n", points, pointsText);
            }
            attempt++;
            try {
                Thread.sleep(1500);
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
            return new Game(player);
        }

        String[] fields = content.split(":");
        Player playerOut = null;
        int machineRounds = 0;
        try {
            playerOut = new Player(nick, Integer.parseInt(fields[0]), Integer.parseInt(fields[2]), Integer.parseInt(fields[3]));
        }
        catch (ArrayIndexOutOfBoundsException e) {
            switch (fields.length) {
                case 0:
                    playerOut = new Player(nick);
                    break;
                case 1:
                    playerOut = new Player(nick, Integer.parseInt(fields[0]), 0, 0);
                    break;
                case 2:
                    playerOut = new Player(nick, Integer.parseInt(fields[0]), 0, 0);
                case 3:
                    playerOut = new Player(nick, Integer.parseInt(fields[0]), Integer.parseInt(fields[2]), 0);
                    break;
                case 5:
                    machineRounds = Integer.parseInt(fields[4]);
                default:
                    System.out.println("nie wczytane osiagniecia zostały wyzerowane");
            }
            System.out.println("Plik zapisu uszkodzony:");
            switch (fields.length) {
                case 0:
                    System.out.println("Plik zapisu uszkodzony:");
                case 1:
                    System.out.println("nie wczytano numeru rundy trybu pojedynczego");
                case 2:
                    System.out.println("nie wczytano danych trybu przeciwko komputerowi: nie wczytano osiagnięć gracza");
                case 3:
                    System.out.println("nie wczytano danych trybu przeciwko komputerowi: nie wczytano osiągnięć komputera");
                case 4:
                    System.out.println("nie wczytano danych trybu przeciwko komputerowi: nie wczytano ilości rund z komputerem");
                default:
                    System.out.println("nie wczytane osiagniecia zostały wyzerowane");
            }
        }

        return new Game(playerOut, Integer.parseInt(fields[1]), machineRounds);
    }
}
