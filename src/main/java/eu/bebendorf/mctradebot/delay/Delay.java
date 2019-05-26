package eu.bebendorf.mctradebot.delay;

public class Delay {

    public static void delay(long time, Runnable action){
        new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            action.run();
        }).start();
    }

}
