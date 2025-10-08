package pl.filipmoszczynski;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Wybierz tryb: %n[1]Ty zgadujesz - komputer podaje liczbę%n[2]Komputer zgaduje - ty podajesz liczbę%n");
        int max = 100;
        boolean playGame = true;

        int choice = new Scanner(System.in).nextInt();
        if (choice == 1) {//user guesses
            System.out.println("Witaj w 'Guessing Game'");

            System.out.println("Podaj nick gracza ");

            String nick = new Scanner(System.in).nextLine();

            Player player = new Player(nick);
            Game game  = new Game(player);
            int round = 0;

            if (!FileManager.getPlayer(nick).isEmpty()){
                boolean goOn = true;
                game = new Game(nick);
                do{
                    System.out.println("Znaleziono zapis gracza. Wczytać? [t/n]");
                    System.out.println("Rozpoczęcie nowej gry skasuje postęp!");
                    String answer = new Scanner(System.in).nextLine();
                    if (answer.equalsIgnoreCase("t")) {
                        goOn = false;
                        game = game.read(nick);
                        round = game.getRound();
                        player = game.getPlayer();
                        FileManager.deleteFile(player);
                    }
                    if (answer.equalsIgnoreCase("n")) {
                        goOn = false;
                    }
                    if (!answer.equalsIgnoreCase("n") && !answer.equalsIgnoreCase("t")) {
                        System.out.println("Nie rozumiem, użyj 't' lub 'n'");
                    }
                }
                while (goOn);
            }



            System.out.println("Masz 3 próby na odgadniecie liczby");
            System.out.println("W każdej rundzie do zdobycia są 3 punkty, każda błędna odpowiedź to jeden punkt mniej");


            do {
                game.round(round+1, max);

                boolean goOn = true;
                do{
                    System.out.println("Zapisać i zamknąć? [t/n]");
                    String answer = new Scanner(System.in).nextLine();
                    if (answer.equalsIgnoreCase("t")) {
                        goOn = false;
                        playGame = false;
                        game.save(player, game);
                        System.out.printf("Gra zapisana.%nSumarycznie masz %s pkt.", game.getPlayer().getPoints());
                    }
                    if (answer.equalsIgnoreCase("n")) {
                        goOn = false;
                    }
                    if (!answer.equalsIgnoreCase("n") && !answer.equalsIgnoreCase("t")) {
                        System.out.println("Nie rozumiem, użyj 't' lub 'n'");
                    }
                }
                while (goOn);
                round++;

            }
            while (playGame);
        } else if (choice == 2){//computer guesses
            Player player = new Player("komputer");
            Game game  = new Game(player);
            int round = 0;
            int numberToGuess = 0;


            do { //todo: po wprowadzeniu niewłaściwej liczby kontynuuje

                    System.out.printf("Podaj liczbę z zakresu 0-%s, którą ma odgadnąć komputer: %nOszukiwanie będzie karane%n", max);
                    try {
                        numberToGuess = new Scanner(System.in).nextInt();
                    } catch (Exception e) {
                        System.out.println("Nie wprowadziłeś liczby całkowitej. Komputer dostaje punkt");
                        player.addPoints(1);
                        round++;
                        continue;
                    }


                if (numberToGuess < 0 || numberToGuess > max) {
                    System.out.println("Podałeś liczbę spoza zakresu");
                    System.out.println("komputer dostaje punkt");
                    player.addPoints(1);
                    round++;
                    continue;
                }

                game.round(round+1, max, numberToGuess);

                boolean goOn = true;
                do{
                    System.out.println("Zapisać i zamknąć? [t/n]");
                    String answer = new Scanner(System.in).nextLine();
                    if (answer.equalsIgnoreCase("t")) {
                        goOn = false;
                        playGame = false;
                        game.save(player, game);
                        System.out.printf("Gra zapisana.%nSumarycznie komputer zdobył %s pkt.", game.getPlayer().getPoints());
                    }
                    if (answer.equalsIgnoreCase("n")) {
                        goOn = false;
                    }
                    if (!answer.equalsIgnoreCase("n") && !answer.equalsIgnoreCase("t")) {
                        System.out.println("Nie rozumiem, użyj 't' lub 'n'");
                    }
                }
                while (goOn);
                round++;

            }
            while (playGame);
        }

    }
}