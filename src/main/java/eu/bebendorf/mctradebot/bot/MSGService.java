package eu.bebendorf.mctradebot.bot;

import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import eu.bebendorf.mctradebot.delay.PacketQueue;

public class MSGService {

    private MCClient client;
    private PacketQueue msgQueue;

    public MSGService(MCClient client){
        this.client = client;
        client.getBus().register(this);
        client.registerService("msg", this);
        if(client.getServer() == Server.BAUSUCHT){
            msgQueue = new PacketQueue(client, 1, 2000);
        }else{
            msgQueue = new PacketQueue(client, 1, 1100);
        }
        new Thread(msgQueue).start();
    }

    public void send(String username, String message){
        msgQueue.offer(new ClientChatPacket("/msg "+username+ " " + message));
    }

    @MCEvent
    public void onChat(MCClient client, ServerChatPacket p){
        if(p.getType() == MessageType.NOTIFICATION)
            return;
        String text = p.getMessage().getFullText();
        if(client.getServer() == Server.CANDYCRAFT){
            if(!text.startsWith("["))
                return;
            String[] spl = text.split(" ");
            if(!spl[1].equals("->"))
                return;
            if(!spl[2].equals("Du]"))
                return;
            String username = spl[0].substring(1);
            StringBuilder sb = new StringBuilder();
            for(int i = 3; i < spl.length; i++){
                sb.append(" " + spl[i]);
            }
            String message = sb.toString().substring(1);
            client.getBus().fire(new MSGEvent(username, message));
        }
        if(client.getServer() == Server.BAUSUCHT){
            if(!text.startsWith("[Nachrichten]  ["))
                return;
            String[] spl = text.split(" ");
            if(!spl[3].equals("->"))
                return;
            if(!spl[4].equals("Dir]"))
                return;
            String username = spl[2].substring(1);
            StringBuilder sb = new StringBuilder();
            for(int i = 5; i < spl.length; i++){
                sb.append(" " + spl[i]);
            }
            String message = sb.toString().substring(1);
            client.getBus().fire(new MSGEvent(username, message));
        }
    }

}
