import java.util.Random;

/**
 * Created by lux on 18.01.17.
 */
public class Main {
    public static void main(String args[])
    {
        Random random = new Random();
        System.out.println("Spamming...");
        for(int i = 0; i < 100; i++)
        {
            new Thread(new SpammerWorker(random)).start();
        }
        System.out.println("DONE");
    }
}
