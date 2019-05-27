package eu.bebendorf.mctradebot.client;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import com.google.inject.Inject;
import eu.bebendorf.mctradebot.BotConfig;
import eu.bebendorf.mctradebot.BotCredentials;
import eu.bebendorf.mctradebot.MCTradeBot;
import eu.bebendorf.mctradebot.bot.Server;
import eu.bebendorf.mctradebot.delay.PacketQueue;

import java.net.Proxy;

public class MCClient {

    private Client client = null;
    private MCEventBus bus = null;
    private PacketQueue messageQueue;

    @Inject
    private BotCredentials credentials;
    @Inject
    private BotConfig config;

    public void prepare(String host, int port){
        if(client != null){
            disconnect();
        }
        MinecraftProtocol protocol;
        if (credentials.getPassword() != null) {
            try {
                protocol = new MinecraftProtocol(credentials.getUsername(), credentials.getPassword());
            } catch (RequestException e) {
                e.printStackTrace();
                return;
            }
        } else {
            protocol = new MinecraftProtocol(credentials.getUsername());
        }
        client = new Client(host, port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
        client.getSession().addListener(new SessionAdapter() {
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
                if(event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
            }
        });
        if(config.getServer() == Server.BAUSUCHT){
            messageQueue = new PacketQueue(this, 1, 3100);
        }else{
            messageQueue = new PacketQueue(this, 1, 1100);
        }
        new Thread(messageQueue).start();
        bus = new MCEventBus(this);
    }

    public void connect(){
        connect(null, 0);
    }

    public void connect(String host, int port){
        if(client == null && host != null)
            prepare(host, port);
        client.getSession().connect();
    }

    public void disconnect(){
        disconnect("Finished!");
    }

    public void disconnect(String reason){
        if(client != null){
            client.getSession().disconnect(reason);
        }
    }

    public void sendMessage(String message){
        System.out.println("M: "+message);
        messageQueue.offer(new ClientChatPacket(message));
    }

    public Session getSession(){
        return client.getSession();
    }

    public MCEventBus getBus(){
        return bus;
    }

}
