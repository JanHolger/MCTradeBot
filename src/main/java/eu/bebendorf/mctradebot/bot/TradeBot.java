package eu.bebendorf.mctradebot.bot;

import eu.bebendorf.mctradebot.app.App;
import eu.bebendorf.mctradebot.client.MCClient;

public class TradeBot {

    private MCClient client;
    private App app;

    public TradeBot(String username, String password, App app){
        client = new MCClient(username, password);
        this.app = app;
    }

    public void connect(String host, int port){
        client.prepare(host, port);
        PaymentService paymentService = new PaymentService(client);
        PrepareService prepareService = new PrepareService(client);
        MSGService msgService = new MSGService(client);
        ControlService controlService = new ControlService(client);
        app.init(client);
        client.connect();
    }

    public void disconnect(){
        client.disconnect();
    }

}
