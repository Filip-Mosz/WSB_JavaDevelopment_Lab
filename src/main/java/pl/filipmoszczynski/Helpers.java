package pl.filipmoszczynski;

import java.util.*;

/**
 * class consisting static helper methods that don't fit anywhere else
 *
 */
public abstract class Helpers {
    private final static String saveAndQuit = "Zapisać i wrócić do menu głównego? [t/n]";

    public static String verifyInput(String input, String allowed) {//YAGNI!
        String[] allowedOptions = allowed.split(",");

        String[] matched = (String[]) Arrays.stream(allowedOptions).filter(x -> Objects.equals(x, input)).toArray();
        if (matched.length == 0) {
            return "Nie rozpoznano opcji/n dostępne opcje to: " + input;
        }
        return "";
    }

    /**
     * @param max    represents top number to be guessed
     * @param player Player-class object representing user
     */
    public static void singlePlayerRound(int max, Player player) {//user guesses
        boolean playGame = true;
        Game game = new Game(player);
        player = game.getPlayer();
        int round = game.getRoundMachine();

        System.out.println("Masz 3 próby na odgadniecie liczby");
        System.out.printf("W każdej rundzie do zdobycia są %s pkt, każda błędna odpowiedź to %s pkt mniej", 3 * game.getDifficulty(), game.getDifficulty());

        do {
            game.round(round + 1, max);

            System.out.println(saveAndQuit);
            boolean answer = Menu.yesNoMenu();
            if (answer) {
                playGame = false;
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
        //Player player = new Player("01001101_01100001_01100011_01101000_01101001_01101110_01100101_00100000_01010011_01110000_01101001_01110010_01101001_01110100");

        Game game = new Game(human);
        Player player = game.getPlayer();
        int round = game.getRoundMachine();
        int numberToGuess = 0;
        if (game.getMaxNumber() != max) {
            game.setMaxNumber(game.getMaxNumber());
        }

        do {

            System.out.printf("Podaj liczbę z zakresu 0-%s, którą ma odgadnąć komputer: %nOszukiwanie będzie karane%n", max);
            try {
                numberToGuess = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.printf("Nie wprowadziłeś liczby całkowitej. Komputer dostaje %s pkt", game.getDifficulty());
                player.addMachinePoints(game.getDifficulty());
                round++;
                continue;
            }

            if (numberToGuess < 0 || numberToGuess > max) {
                System.out.println("Podałeś liczbę spoza zakresu");
                System.out.printf("komputer dostaje %s pkt", game.getDifficulty());
                player.addMachinePoints(game.getDifficulty());
                round++;
                continue;
            }

            game.roundMachine(round + 1, max, numberToGuess);

            System.out.println(saveAndQuit);//"zapisać i wyjść do menu?"
            boolean answer = Menu.yesNoMenu();
            if (answer) {
                playGame = false;
                game.save(player, game);
                System.out.printf("Komputer zdobył sumarycznie %s pkt%n%n", game.getPlayer().getMachinePoints());
            }
            round++;

        }
        while (playGame);
    }

    public static void manVsMachine(int max, Player human) {
        boolean playGame = true;
        Player machine = new Player("101100");
        Game game = new Game(FileManager.getPlayer(human.getNick()), 1);
        game = game.read(human.getNick());
        Player man = game.getPlayer();
        int round = 0;
        int numberToGuess = 0;

        System.out.println("W tym trybie na zmianę z komputerem zgadujecie liczbę");
        do {
            int min = 0;
            System.out.printf("RUNDA %s  %n", round);
            int attempt = 0;
            boolean guessed = false;
            Player[] players = {machine, man};
            List<Player> playerList = new ArrayList<>(List.of(players));
            Collections.shuffle(playerList);
            players = playerList.toArray(players);
            numberToGuess = (int) (Math.random() * max + 1);
            do {

                System.out.println("hint: " + numberToGuess);
                for (Player player : players) {
                    if (guessed) {
                        round++;
                        continue;
                    }
                    if (player.equals(machine)) {
                        guessed = game.roundMachine(round + 1, max, numberToGuess);
                    } else {
                        System.out.printf("Zgaduje %s%n", player.getNick());
                        guessed = game.roundHuman(round + 1, numberToGuess);
                    }
                }
                round++;
            }
            while (!guessed);

            System.out.println(saveAndQuit);//"zapisać i wyjść do menu?"
            boolean answer = Menu.yesNoMenu();
            if (answer) {
                playGame = false;
                game.save(man, game);
                System.out.printf("Gra zapisana.%nSumarycznie %s zdobył %s pkt.%n", game.getPlayer().getNick(), game.getPlayer().getPointsAgainstMachine());
                System.out.printf("Komputer zdobył sumarycznie %s pkt%n%n", game.getPlayer().getMachinePoints());
            }
        } while (playGame);

    }
}
