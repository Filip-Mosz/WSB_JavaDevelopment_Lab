package pl.filipmoszczynski;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Wybierz tryb: %n[1]Ty zgadujesz - komputer podaje liczbę%n[2]Komputer zgaduje - ty podajesz liczbę%n");
        System.out.printf("[3]Rywalizujesz z komputerem%n");
        int max = 100;
        boolean playGame = true;

        int choice = new Scanner(System.in).nextInt();
        switch (choice) {
            case 1:
                Helpers.singlePlayerMenu(max, playGame);
                break;
            case 2:
                Helpers.againstMachine(max, playGame);
                break;

            default:
        }

    }
}