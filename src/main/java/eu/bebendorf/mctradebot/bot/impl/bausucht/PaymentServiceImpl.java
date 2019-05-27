package eu.bebendorf.mctradebot.bot.impl.bausucht;

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

    @Getter
    private double balance;

    @Override
    public void pay(String username, double amount) {
        balance -= amount;
        client.getSession().send(new ClientChatPacket("/pay " + username + " " + ((int) amount)));
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
        if (spl.length != 10)
            return;
        if (!spl[0].equals("[Bank]"))
            return;
        if (!spl[1].equals("Du"))
            return;
        if (!spl[2].equals("hast"))
            return;
        if (!spl[4].equals("Münzen"))
            return;
        if (!spl[5].equals("von"))
            return;
        if (!spl[7].equals("|"))
            return;
        if (!spl[9].equals("erhalten."))
            return;
        double amount = Double.parseDouble(spl[3].replace(",", ""));
        balance += amount;
        client.getBus().fire(new PaymentEvent(spl[8], amount));
    }

    @MCEvent
    public void onBalance(MCClient client, ServerChatPacket p) {
        if (p.getType() == MessageType.NOTIFICATION)
            return;
        String text = p.getMessage().getFullText();
        String[] spl = text.split(" ");
        if (spl.length != 5)
            return;
        if (!spl[0].equals("[Bank]"))
            return;
        if (!spl[1].equals("Du"))
            return;
        if (!spl[2].equals("hast"))
            return;
        if (!spl[4].equals("Münzen."))
            return;
        try {
            balance = Double.parseDouble(spl[3]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
