import controller.PlayHanabi;

public class HanabiMain {

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            PlayHanabi playHanabi = new PlayHanabi();
            exit = playHanabi.play();
        }

    }
}
