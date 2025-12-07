package pl.filipmoszczynski;

import java.util.*;

/**
 * class consisting static helper methods that don't fit anywhere else *
 */
public abstract class Helpers {
    final static String savePrompt = "Zapisać? [t/n]";
    final static String comtinuePrompt = "Wrócić do menu głównego? [t/n]";
    final static String machineName = "01001101_01100001_01100011_01101000_01101001_01101110_01100101_00100000_01010011_01110000_01101001_01110010_01101001_01110100";

    /**
     * @param player Player-class object representing user
     */
    public static void singlePlayerRound(int max, Player player) {//user guesses
        boolean playGame = true;
        Game game = new Game(player);
        player = game.getPlayer();
        int round = game.getRoundAgainstMachine();
        if (game.getMaxNumber() != max) {
            max = game.getMaxNumber();
        }

        System.out.println("Masz 3 próby na odgadniecie liczby");
        System.out.printf("W każdej rundzie do zdobycia są %s pkt, każda błędna odpowiedź to %s pkt mniej%n", 3 * game.getReward(), game.getReward());

        do {
            game.round(round + 1, max, player.getNick());

            //continue
            System.out.println(comtinuePrompt);
            boolean answer = Menu.yesNoMenu();
            if (answer) {
                playGame = false;
            }
            //save
            System.out.println(savePrompt);
            answer = Menu.yesNoMenu();
            if (answer) {
                game.save(player, game);
                System.out.printf("Gra zapisana.%nSumarycznie masz %s pkt.", game.getPlayer().getPoints());
            }
            round++;
        }
        while (playGame);
    }

    public static void againstMachine(int max, Player human) {
        //computer guesses
        boolean playGame = true;

        Game game = new Game(machineName);
        Player machine = new Player(machineName);
        int round = game.getRoundAgainstMachine();
        int numberToGuess = 0;
        if (game.getMaxNumber() != max) {
            max = game.getMaxNumber();
        }

        do {

            System.out.printf("%nPodaj liczbę z zakresu 0-%s, którą ma odgadnąć komputer: %nOszukiwanie będzie karane%n", max);
            try {
                numberToGuess = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.printf("Nie wprowadziłeś liczby całkowitej. Komputer dostaje %s pkt", game.getDifficulty());
                machine.addPoints(game.getDifficulty());
                round++;
                continue;
            }

            if (numberToGuess < 0 || numberToGuess > max) {
                System.out.println("Podałeś liczbę spoza zakresu");
                System.out.printf("komputer dostaje %s pkt", game.getDifficulty());
                machine.addPoints(game.getDifficulty());
                round++;
                continue;
            }

            game.roundMachine(round + 1, max, numberToGuess);

            //continue
            System.out.println(comtinuePrompt);
            boolean answer = Menu.yesNoMenu();
            if (answer) {
                playGame = false;
            }
            //save
            System.out.println(savePrompt);
            answer = Menu.yesNoMenu();
            if (answer) {
                game.save(machine, game);
                System.out.printf("Komputer zdobył sumarycznie %s pkt%n%n", game.getPlayer().getPoints());
            }
            round++;

        }
        while (playGame);
    }

    public static void manVsMachine(int max, Player human) {
        boolean playGame = true;
        Player machine = new Player(machineName);
        Game game = new Game(FileManager.getPlayerData(human.getNick()), 1);
        game = game.read(human.getNick(), true);
        Player man = game.getPlayer();
        int round = game.getRoundAgainstMachine();
        if (game.getMaxNumber() != max) {
            max = game.getMaxNumber();
        }

        System.out.printf("W tym trybie na zmianę z komputerem zgadujecie liczbę z przedziału 0-%s%n", max);
            Player[] players = {machine, man};
            game.roundMultiplayer(round, max, players);
    }

    public static void multiplayer(Player mainPlayer) {
        //ask for number of players
        boolean inputIncorrect = false;
        int playersCount = 1;
        int alreadyAssignedPlayers = 1;
        String prompt = "Podaj liczbę graczy";
        do{
            try {
                System.out.println(prompt);
                playersCount = new Scanner(System.in).nextInt();
                if (playersCount < 1) {
                    throw new IllegalArgumentException(prompt);
                } else{
                    inputIncorrect = false;
                }
            } catch (Exception e) {
                prompt = "Podaj liczbę całkowitą, większą niż 1, która reprezentuje ilość uczestników rozgrywki";
                inputIncorrect = true;
            }
        }while(inputIncorrect);

        Player[] players = new Player[playersCount];
        Game game = new Game(mainPlayer);
        players[0] = game.getPlayer();
        //ask if include computer
        System.out.println("Dołączyć komputer do rozgrywki? [t/n]");
        boolean answer = Menu.yesNoMenu();
        if (answer) {
            players[1] = Player.read(machineName);
            alreadyAssignedPlayers++;
        }

        for (int i = alreadyAssignedPlayers; i < playersCount; i++) {
            System.out.println("Podaj nick gracza nr " + i);
            String input = new  Scanner(System.in).nextLine();
            players[i] = game.read(input, false).getPlayer();
        }

        System.out.println("\nW tym trybie wszyscy gracze zgadują tą samą liczbę\n Pierwszy, który zgadnie dostaje punkty, a kolejność zgadywania jest losowana.\n");

        game.setPlayers(players);
        game.setMaxNumber(game.getDifficulty());
        game.setMachineMaxGuess(game.getMaxNumber());
        game.roundMultiplayer(game.getRoundMultiplayer(), game.getMaxNumber(), players);

    }

}
