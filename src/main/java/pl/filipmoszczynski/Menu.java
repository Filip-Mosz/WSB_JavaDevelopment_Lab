package pl.filipmoszczynski;

import java.util.Scanner;

public class Menu {
    /*
    * Class containing static methods providing functionalities for menus*/

    public static boolean yesNoMenu(){
        do{
            String answer = new Scanner(System.in).nextLine();
            if (answer.trim().equalsIgnoreCase("t")) {
                return true;
            }
            if (answer.trim().equalsIgnoreCase("n")) {
                return false;
            }
            if (!answer.trim().equalsIgnoreCase("n") && !answer.trim().equalsIgnoreCase("t")) {
                System.out.println("Nie rozumiem, użyj 't' lub 'n'");
            }
        }
        while (true);
    }

}
