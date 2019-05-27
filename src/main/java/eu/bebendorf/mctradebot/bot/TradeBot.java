package eu.bebendorf.mctradebot.bot;

import com.google.inject.Inject;
import eu.bebendorf.mctradebot.app.App;
import eu.bebendorf.mctradebot.bot.service.ControlService;
import eu.bebendorf.mctradebot.bot.service.MSGService;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.bot.service.PrepareService;
import eu.bebendorf.mctradebot.client.MCClient;

public class TradeBot {

    @Inject
    private MCClient client;
    @Inject
    private App app;

    @Inject
    private PaymentService paymentService;

    @Inject
    private PrepareService prepareService;

    @Inject
    private ControlService controlService;

    @Inject
    private MSGService msgService;

    public void connect(String host, int port) {
        client.prepare(host, port);
        client.getBus().register(paymentService);
        client.getBus().register(prepareService);
        client.getBus().register(msgService);
        client.getBus().register(controlService);
        app.init(client);
        client.connect();
    }

    public void disconnect() {
        client.disconnect();
    }

}
