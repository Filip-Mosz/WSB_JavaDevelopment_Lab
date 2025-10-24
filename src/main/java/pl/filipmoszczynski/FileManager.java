package pl.filipmoszczynski;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class FileManager {
        //savefile construction
        //pointsSinglePlayer : roundSinglePlayer
    private final static String saveDirectory = "./src/main/resources/saves/";

    static void saveFile(Player player, Game game) {
        Path pathname = Paths.get(saveDirectory + player.getNick() +".txt");
        String fileContent = player.getPoints() + ":"
                + game.getRound() + ":"
                + player.getPointsAgainstMachine() + ":"
                + player.getMachinePoints() + ":"
                + game.getRoundMachine() + ":"
                + game.getDifficulty();

        File file = new File(pathname.toString());

        if (!file.exists())
        {
            try
            {
                FileWriter myWriter = new FileWriter(pathname.toString());
                myWriter.write(fileContent);
                myWriter.close();  // must close manually
            }
            catch (FileAlreadyExistsException x)
            {
                //System.err.format("file named %s already exists%n", pathname);
            }
            catch (IOException x)
            {
                // Some other sort of failure, such as permissions.
                System.err.format("createFile error: %s%n", x);
            }
        }
        else{
            try {
                Files.write(pathname, Arrays.asList(fileContent));
            } catch (IOException x) {
                System.err.format("createFile error: %s%n", x);
            }
        }
    }

    static void deleteFile(Player player) {
        Path pathname = Paths.get(saveDirectory + player.getNick() +".txt");
        File file = new File(pathname.toString());
        if(file.delete()){
            System.out.println("Zapis usunięty.");
        } else {
            System.out.println("Problem z usunieciem zapisu.");
        }
    }

    static String getPlayer(String player) {
        File myObj = new File(saveDirectory+ player +".txt");
        StringBuilder content = new StringBuilder();
        // try-with-resources: Scanner will be closed automatically
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content.append(data);
            }
        } catch (FileNotFoundException e) {
            return content.toString();
        }
        return content.toString();
    }

}


