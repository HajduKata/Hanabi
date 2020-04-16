import controller.PlayTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HanabiMain {

    public static void main(String[] args) {
        boolean exit = false;
        /*
        while (!exit) {
            PlayHanabi playHanabi = new PlayHanabi();
            exit = playHanabi.play();
        }
        */

        // Test Games
        int numberOfTests = 10;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH-mm-ss");
        String fileName = "Test_" + dtf.format(LocalDateTime.now()) + ".csv";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert fileWriter != null;
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (int numberOfPlayers = 2; numberOfPlayers <= 5; numberOfPlayers++) {
            for (int i = 0; i < numberOfTests; i++) {
                PlayTest playTest = new PlayTest(numberOfPlayers);
                printWriter.println(playTest.play());
            }
        }
        printWriter.close();
    }
}
