package eu.bebendorf.mctradebot.delay;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import eu.bebendorf.mctradebot.client.MCClient;

public class MovementAnimation implements Runnable {

    private MCClient client;
    private double[] startPosition;
    private double[] endPosition;

    private static final int steps = 30;
    private static final long interval = 50;

    public MovementAnimation(MCClient client, double[] startPosition, double[] endPosition){
        this.client = client;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public void run(){
        double xStep = (endPosition[0] - startPosition[0]) / (double) steps;
        double yStep = (endPosition[1] - startPosition[1]) / (double) steps;
        double zStep = (endPosition[2] - startPosition[2]) / (double) steps;
        for(int step = 1; step < steps; step++){
            client.getSession().send(new ClientPlayerPositionPacket(true, startPosition[0] + (xStep * step), startPosition[1] + (yStep * step), startPosition[2] + (zStep * step)));
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        client.getSession().send(new ClientPlayerPositionPacket(true, endPosition[0], endPosition[1], endPosition[2]));
    }

}
