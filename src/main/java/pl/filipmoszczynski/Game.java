package pl.filipmoszczynski;

import java.util.Scanner;

public class Game {
    Player player;
    int round;


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

    public int getRound() {
        return this.round;
    }

    public Player getPlayer() {
        return this.player;
    }

    void round(int roundNum, int max) {
        int currNum = (int)(Math.random() * max + 1);

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
                player.addPoints(points);
            } else {
                String hint = guess > currNum ? "Za wysoko" : "Za nisko";
                System.out.println(hint);
            }
            if (attempt == 2 && !guessed) {
                points = 0;
                pointsText = "punktów";
            }

            if (attempt == 2 || guessed) {
                System.out.printf("Zdobywasz %s %s.%n", points, pointsText);
            }
            attempt++;
        }
        this.round = roundNum;
    }

    void round(int roundNum, int max , int numberToGuess) {
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
                player.addPoints(points);
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
                System.out.printf("Komputer zdobywa %s %s.%n", points, pointsText);
            }
            attempt++;
        }
        this.round = roundNum;
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
        Player playerOut = new Player(nick,Integer.parseInt(fields[0]));
        return new Game(playerOut, Integer.parseInt(fields[1]));
    }
}
