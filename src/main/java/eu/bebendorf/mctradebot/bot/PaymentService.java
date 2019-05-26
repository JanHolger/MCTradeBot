package eu.bebendorf.mctradebot.bot;

import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import lombok.Getter;

public class PaymentService {

    private MCClient client;
    @Getter
    private double balance = 0;

    public PaymentService(MCClient client){
        this.client = client;
        client.getBus().register(this);
        client.registerService("payment", this);
    }

    public void pay(String username, double amount){
        balance -= amount;
        if(client.getServer() == Server.BAUSUCHT){
            client.getSession().send(new ClientChatPacket("/pay "+username+" "+((int)amount)));
        }else{
            client.getSession().send(new ClientChatPacket("/pay "+username+" "+amount));
        }
        System.out.println("Payout!");
    }

    public void checkBalance(){
        client.getSession().send(new ClientChatPacket("/money"));
    }

    @MCEvent
    public void onPayment(MCClient client, ServerChatPacket p){
        if(p.getType() == MessageType.NOTIFICATION)
            return;
        String[] spl = p.getMessage().getFullText().split(" ");
        if(client.getServer() == Server.CANDYCRAFT){
            if(spl.length != 5)
                return;
            if(!spl[1].equals("hat"))
                return;
            if(!spl[2].equals("dir"))
                return;
            if(!spl[4].equals("gegeben."))
                return;
            if(spl[3].charAt(0) != '€')
                return;
            double amount = Double.parseDouble(spl[3].substring(1));
            balance += amount;
            client.getBus().fire(new PaymentEvent(spl[0],amount));
        }
        if(client.getServer() == Server.BAUSUCHT){
            if(spl.length != 10)
                return;
            if(!spl[0].equals("[Bank]"))
                return;
            if(!spl[1].equals("Du"))
                return;
            if(!spl[2].equals("hast"))
                return;
            if(!spl[4].equals("Münzen"))
                return;
            if(!spl[5].equals("von"))
                return;
            if(!spl[7].equals("|"))
                return;
            if(!spl[9].equals("erhalten."))
                return;
            double amount = Double.parseDouble(spl[3]);
            balance += amount;
            client.getBus().fire(new PaymentEvent(spl[8],amount));
        }
    }

    @MCEvent
    public void onBalance(MCClient client, ServerChatPacket p){
        if(p.getType() == MessageType.NOTIFICATION)
            return;
        String text = p.getMessage().getFullText();
        if(client.getServer() == Server.CANDYCRAFT){
            if(!text.startsWith("Kontostand: €"))
                return;
            try {
                balance = Double.parseDouble(text.substring(13).replace(",",""));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        if(client.getServer() == Server.BAUSUCHT){
            String[] spl = text.split(" ");
            if(spl.length != 5)
                return;
            if(!spl[0].equals("[Bank]"))
                return;
            if(!spl[1].equals("Du"))
                return;
            if(!spl[2].equals("hast"))
                return;
            if(!spl[4].equals("Münzen."))
                return;
            try {
                balance = Double.parseDouble(spl[3]);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
