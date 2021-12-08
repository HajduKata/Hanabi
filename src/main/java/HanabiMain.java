/*
Hanabi program
Készítette: Hajdu Katalin
Elkészült 2020. május 15.

Tesztüzemmód futtatásához a kikommentezett részt kell használni,
és a korábban használt programrészletet kikommentezni.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import controller.PlayHanabi;
import controller.PlayTest;

public class HanabiMain {
    private static boolean isTestGame;

    public static void main(String[] args) {
        readProperties();

        // User plays
        if (!isTestGame) {
            boolean exit = false;
            while (!exit) {
                PlayHanabi playHanabi = new PlayHanabi();
                playHanabi.play();
                exit = playHanabi.scoring();
            }
        } // AI plays
        else {
            runInTestMode();
        }
    }

    public static void readProperties() {
        try (InputStream input = HanabiMain.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties properties = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            // Load a properties file from class path, inside static method
            properties.load(input);

            // Get the property value and set it in isTestGame
            isTestGame = properties.getProperty("testmode").equals("true");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void runInTestMode() {
        int numberOfTests = 5;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH-mm-ss");
        String fileName = "Test_" + numberOfTests + "_" + dtf.format(LocalDateTime.now()) + ".csv";
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
                playTest.play();
                playTest.scoring();
                printWriter.println(playTest.getResult());
            }
        }
        printWriter.close();
    }
}
