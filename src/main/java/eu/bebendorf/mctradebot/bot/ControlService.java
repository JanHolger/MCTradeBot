package eu.bebendorf.mctradebot.bot;

import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import eu.bebendorf.mctradebot.delay.Delay;

import java.util.Arrays;
import java.util.List;

public class ControlService {

    private List<String> admins = Arrays.asList(
            "JanHolger",
            "x7airworker"
    );

    public ControlService(MCClient client){
        client.getBus().register(this);
        client.registerService("control", this);
    }

    @MCEvent
    public void onMSG(MCClient client, MSGEvent e){
        if(!admins.contains(e.getUsername()))
            return;
        PaymentService paymentService = client.getService("payment");
        MSGService msgService = client.getService("msg");
        if(e.getMessage().startsWith("/"))
            client.sendMessage(e.getMessage());
        if(e.getMessage().startsWith("chat ")){
            client.sendMessage(e.getMessage().substring(5));
        }
        if(e.getMessage().startsWith("advertise")){
            if(client.getServer() == Server.CANDYCRAFT){
                client.sendMessage("Ich bin ein GuessTheNumber Bot. Ihr könnt mir eine Zahl zwischen 1 und 100 bezahlen. Wenn euer Betrag die aktuelle Zahl ist gewinnt ihr den Pot. Viel Glück!");
            }
            if(client.getServer() == Server.BAUSUCHT){
                client.sendMessage("Ich bin ein GuessTheNumber Bot. Ihr könnt mir eine Zahl zwischen 1 und 100 bezahlen.");
                client.sendMessage("Wenn euer Betrag die aktuelle Zahl ist gewinnt ihr den Pot. Viel Glück!");
            }
        }
        if(e.getMessage().startsWith("money")){
            paymentService.checkBalance();
            Delay.delay(3000, () -> {
                msgService.send(e.getUsername(), "Kontostand: "+paymentService.getBalance());
            });
        }
    }

}
