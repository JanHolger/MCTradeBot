package eu.bebendorf.mctradebot.bot.impl.candycraft;

import com.google.inject.Inject;
import eu.bebendorf.mctradebot.bot.event.MSGEvent;
import eu.bebendorf.mctradebot.bot.service.ControlService;
import eu.bebendorf.mctradebot.bot.service.MSGService;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import eu.bebendorf.mctradebot.delay.Delay;

public class ControlServiceImpl implements ControlService {
    @Inject
    private PaymentService paymentService;

    @Inject
    private MSGService msgService;

    @MCEvent
    public void onMSG(MCClient client, MSGEvent e) {
        if (!getAdmins().contains(e.getUsername()))
            return;

        if (e.getMessage().startsWith("/"))
            client.sendMessage(e.getMessage());
        if (e.getMessage().startsWith("chat ")) {
            client.sendMessage(e.getMessage().substring(5));
        }
        if (e.getMessage().startsWith("advertise")) {
            client.sendMessage("Ich bin ein GuessTheNumber Bot. Ihr könnt mir eine Zahl zwischen 1 und 100 bezahlen. Wenn euer Betrag die aktuelle Zahl ist gewinnt ihr den Pot. Viel Glück!");
        }
        if (e.getMessage().startsWith("money")) {
            paymentService.checkBalance();
            Delay.delay(3000, () -> {
                msgService.send(e.getUsername(), "Kontostand: " + paymentService.getBalance());
            });
        }
    }
}
