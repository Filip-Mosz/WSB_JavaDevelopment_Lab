package pl.filipmoszczynski;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
//todo: punkt 3. 3 poziomy trudności, potem zapisywanie
//todo 2: punkt 4. zapisywanie poziomu trudności i punktacji na każdym poziomie trudności; poziom trudności jako argument metod i dodatkowe parametry dla klasy game(poziom trudności) i player(punkty wg poziomu trudności)
        System.out.println("Witaj w 'Guessing Game'");
        System.out.println("Podaj nick gracza ");
        String nick = new Scanner(System.in).nextLine();
        Player player = new Player(nick);
        boolean playGame = true;

        do{
        System.out.printf("Wybierz tryb: %n[1]Ty zgadujesz - komputer podaje liczbę%n[2]Komputer zgaduje - ty podajesz liczbę%n");
        System.out.printf("[3]Rywalizujesz z komputerem%n");
        System.out.println("[4]Zamknij grę");
        int max = 100;

            int choice = new Scanner(System.in).nextInt();
            switch (choice) {
                case 1:
                    Helpers.singlePlayerRound(max, playGame, player);
                    break;
                case 2:
                    Helpers.againstMachine(max, playGame);
                    break;
                case 3:
                    Helpers.manVsMachine(max, playGame, player);
                    break;
                case 4:
                    playGame = false;
                default:
            }
        }while(playGame);

    }
}