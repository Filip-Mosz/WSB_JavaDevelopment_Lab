package pl.filipmoszczynski;

import java.util.Scanner;
// github.com/Filip-Mosz/WSB_JavaDevelopment_Lab
public class Main {
    public static void main(String[] args) {

        System.out.println("Witaj w 'Guessing Game'");
        System.out.println("Podaj nick gracza ");
        String nick = new Scanner(System.in).nextLine();
        Player player = new Player(nick);
        boolean playGame = true;
        int max = 100;

        do{
        System.out.printf("%n%nWybierz tryb: %n[1]Ty zgadujesz - komputer podaje liczbę%n[2]Komputer zgaduje - ty podajesz liczbę%n");
        System.out.printf("[3]Rywalizujesz z komputerem%n[4]Gra wieloosobowa%n");
        System.out.println("[5]Zamknij grę");

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
                    Helpers.multiplayer(player);
                    break;
                case 5:
                    playGame = false;
                    System.out.println("Dzięki za grę!");
                default:
            }
        }while(playGame);

    }
}