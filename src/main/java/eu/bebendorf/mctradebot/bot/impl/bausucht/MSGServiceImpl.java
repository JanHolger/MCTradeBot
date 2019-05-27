package eu.bebendorf.mctradebot.bot.impl.bausucht;

import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.google.inject.Inject;
import eu.bebendorf.mctradebot.bot.event.MSGEvent;
import eu.bebendorf.mctradebot.bot.service.MSGService;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import eu.bebendorf.mctradebot.delay.PacketQueue;

public class MSGServiceImpl implements MSGService {
    private PacketQueue msgQueue;

    @Inject
    private MCClient client;

    @Inject
    private void init() {
        msgQueue = new PacketQueue(client, 1, 2000);
        new Thread(msgQueue).start();
    }

    @Override
    public void send(String username, String message) {
        msgQueue.offer(new ClientChatPacket("/msg " + username + " " + message));
    }

    @MCEvent
    public void onChat(MCClient client, ServerChatPacket p) {
        if (p.getType() == MessageType.NOTIFICATION)
            return;
        String text = p.getMessage().getFullText();
        if (!text.startsWith("[Nachrichten]  ["))
            return;
        String[] spl = text.split(" ");
        if (!spl[3].equals("->"))
            return;
        if (!spl[4].equals("Dir]"))
            return;
        String username = spl[2].substring(1);
        StringBuilder sb = new StringBuilder();
        for (int i = 5; i < spl.length; i++) {
            sb.append(" " + spl[i]);
        }
        String message = sb.toString().substring(1);
        client.getBus().fire(new MSGEvent(username, message));
    }
}
