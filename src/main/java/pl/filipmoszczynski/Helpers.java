package pl.filipmoszczynski;

import java.util.*;

public abstract class Helpers {
    /*
    * class consisting static helper methods that don't fit anywhere else
    * */

    public static String verifyInput(String input, String allowed) {
        String[] allowedOptions = allowed.split(",");

        String[] matched = (String[]) Arrays.stream(allowedOptions).filter(x -> Objects.equals(x, input)).toArray();
        if (matched.length == 0){
            return "Nie rozpoznano opcji/n dostępne opcje to: " + input;
        }
        return "";
    }

    public static void singlePlayerRound(int max, boolean playGame, Player player){//user guesses
        System.out.println("Witaj w 'Guessing Game'");

        System.out.println("Podaj nick gracza ");

        String nick = new Scanner(System.in).nextLine();

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
    }

    public static void againstMachine(int max, boolean playGame){
        //computer guesses
        Player player = new Player("01001101_01100001_01100011_01101000_01101001_01101110_01100101_00100000_01010011_01110000_01101001_01110010_01101001_01110100");
        Game game  = new Game(player);
        int round = 0;
        int numberToGuess = 0;

        do {

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

            game.roundMachine(round+1, max, numberToGuess);

            boolean goOn = true;
            do{
                System.out.println("Zapisać i zamknąć? [t/n]");
                String answer = new Scanner(System.in).nextLine();
                if (answer.equalsIgnoreCase("t")) {
                    goOn = false;
                    playGame = false;
                    game.save(player, game);
                    System.out.printf("Gra zapisana.%nSumarycznie komputer zdobył %s pkt.", game.getPlayer().getPointsAgainstMachine());
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

    public static void manVsMachine(int max, boolean playGame, Player human) {

        Player machine = new Player("101100");
        Game game  = new Game(FileManager.getPlayer(human.getNick()));
        game = game.read(human.getNick());
        Player man = game.getPlayer();
        int round = 0;
        int numberToGuess = 0;

        System.out.println("W tym trybie na zmianę z komputerem zgadujecie liczbę");

        int min = 0;
        System.out.printf("RUNDA %s  %n", round);
        int attempt = 0;
        boolean guessed = false;
        Player[] players = {machine, man};
        List<Player> playerList = new ArrayList<>(List.of(players));
        Collections.shuffle(playerList);
        players = playerList.toArray(players);
        numberToGuess = (int)(Math.random() * max + 1);
        do{

            System.out.println("hint: " + numberToGuess);
            for (int i = 0; i < players.length; i++) {
                if (guessed) {
                    round++;
                    continue;
                }
                Player currentPlayer = players[i];
                if (currentPlayer.equals(machine)) {
                    guessed = game.roundMachine(round+1, max, numberToGuess);
                }else {
                    System.out.printf("Zgaduje %s%n", players[i].getNick());
                    guessed = game.roundHuman(round+1, numberToGuess);
                }
            }
            round++;
        }
        while(!guessed);


        boolean goOn = true;
        do{
            System.out.println("Zapisać i zamknąć? [t/n]");
            String answer = new Scanner(System.in).nextLine();
            if (answer.equalsIgnoreCase("t")) {
                goOn = false;
                playGame = false;
                game.save(man, game);
                System.out.printf("Gra zapisana.%nSumarycznie %s zdobył %s pkt.%n", game.getPlayer().getNick() ,game.getPlayer().getPointsAgainstMachine());
                System.out.printf("Komputer zdobył sumarycznie %s punktów%n%n", game.getPlayer().getMachinePoints());
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
}
