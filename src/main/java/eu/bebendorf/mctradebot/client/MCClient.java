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
import eu.bebendorf.mctradebot.bot.Server;
import eu.bebendorf.mctradebot.delay.PacketQueue;
import lombok.Getter;

import java.net.Proxy;
import java.util.*;

public class MCClient {

    private Client client = null;
    private MCEventBus bus = null;
    private String username;
    private String password;
    private Map<String, Object> services = new HashMap<>();
    private PacketQueue messageQueue;
    @Getter
    private Server server = Server.CANDYCRAFT;

    public MCClient(String username){
        this(username, null);
    }

    public MCClient(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void prepare(String host, int port){
        if(client != null){
            disconnect();
        }
        MinecraftProtocol protocol;
        if (password != null) {
            try {
                protocol = new MinecraftProtocol(username, password);
            } catch (RequestException e) {
                e.printStackTrace();
                return;
            }
        } else {
            protocol = new MinecraftProtocol(username);
        }
        client = new Client(host, port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
        client.getSession().addListener(new SessionAdapter() {
            public void disconnected(DisconnectedEvent event) {
                clearAfterDisconnect();
                System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
                if(event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
            }
        });
        if(server == Server.BAUSUCHT){
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
            clearAfterDisconnect();
        }
    }

    private void clearAfterDisconnect(){
        client = null;
        bus.close();
        bus = null;
        services.clear();
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

    public void registerService(String name, Object service){
        services.put(name, service);
    }

    public <T> T getService(String name){
        return (T) services.get(name);
    }

}
