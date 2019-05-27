package eu.bebendorf.mctradebot.bot.impl.candycraft;

import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.google.inject.Inject;
import eu.bebendorf.mctradebot.bot.event.PaymentEvent;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import lombok.Getter;

public class PaymentServiceImpl implements PaymentService {
    @Inject
    private MCClient client;

    private double balance = 0;

    public PaymentServiceImpl(){
        System.out.println("Hey!");
    }

    public double getBalance(){
        return balance;
    }

    @Override
    public void pay(String username, double amount) {
        balance -= amount;
        client.getSession().send(new ClientChatPacket("/pay " + username + " " + amount));
        System.out.println("Payout!");
    }

    @Override
    public void checkBalance() {
        client.getSession().send(new ClientChatPacket("/money"));
    }

    @MCEvent
    public void onPayment(MCClient client, ServerChatPacket p) {
        if (p.getType() == MessageType.NOTIFICATION)
            return;
        String[] spl = p.getMessage().getFullText().split(" ");
        if (spl.length != 5)
            return;
        if (!spl[1].equals("hat"))
            return;
        if (!spl[2].equals("dir"))
            return;
        if (!spl[4].equals("gegeben."))
            return;
        if (spl[3].charAt(0) != '€')
            return;
        double amount = Double.parseDouble(spl[3].substring(1).replace(",", ""));
        balance += amount;
        client.getBus().fire(new PaymentEvent(spl[0], amount));
    }

    @MCEvent
    public void onBalance(MCClient client, ServerChatPacket p) {
        if (p.getType() == MessageType.NOTIFICATION)
            return;
        if(p.getMessage().getFullText().contains("Kontostand"))
            System.out.println(p.getMessage().getFullText());
        String text = p.getMessage().getFullText();
        if (!text.startsWith("Kontostand: €"))
            return;
        System.out.println("Balance!");
        try {
            balance = Double.parseDouble(text.substring(13).replace(",", ""));
            System.out.println(balance);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
