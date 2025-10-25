package pl.filipmoszczynski;

import java.io.Console;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//todo: punkt 3. sprzetestować tryb 1
//todo 2: punkt 4. zapisywanie poziomu trudności i punktacji na każdym poziomie trudności; poziom trudności jako argument metod i dodatkowe parametry dla klasy game(poziom trudności) i player(punkty wg poziomu trudności)
        System.out.println("Witaj w 'Guessing Game'");
        System.out.println("Podaj nick gracza ");
        String nick = new Scanner(System.in).nextLine();
        Player player = new Player(nick);
        boolean playGame = true;
        int max = 100;

        do{
        System.out.printf("%n%nWybierz tryb: %n[1]Ty zgadujesz - komputer podaje liczbę%n[2]Komputer zgaduje - ty podajesz liczbę%n");
        System.out.printf("[3]Rywalizujesz z komputerem%n");
        System.out.println("[4]Zamknij grę");

            int choice = new Scanner(System.in).nextInt();
            switch (choice) {
                case 1:
                    Helpers.singlePlayerRound(max, player);
                    break;
                case 2:
                    Helpers.againstMachine(max, player);
                    break;
                case 3:
                    Helpers.manVsMachine(max, player);
                    break;
                case 4:
                    playGame = false;
                    System.out.println("Dzięki za grę!");
                default:
            }
        }while(playGame);

    }
}