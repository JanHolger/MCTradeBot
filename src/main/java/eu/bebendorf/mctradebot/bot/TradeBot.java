package eu.bebendorf.mctradebot.bot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import eu.bebendorf.mctradebot.BotConfig;
import eu.bebendorf.mctradebot.BotCredentials;
import eu.bebendorf.mctradebot.app.App;
import eu.bebendorf.mctradebot.app.guessthenumber.GuessTheNumber;
import eu.bebendorf.mctradebot.bot.service.ControlService;
import eu.bebendorf.mctradebot.bot.service.MSGService;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.bot.service.PrepareService;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.inject.MainModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeBot {

    Injector injector;
    Server server;

    private static final Class[] SERVICES = {
            PaymentService.class,
            PrepareService.class,
            MSGService.class,
            ControlService.class
    };

    public TradeBot(BotConfig config, BotCredentials credentials){
        this.server = config.getServer();
        injector = Guice.createInjector(new MainModule(config, credentials, new GuessTheNumber()), server.getModule());
    }

    public void connect() {
        MCClient client = injector.getInstance(MCClient.class);
        App app = injector.getInstance(App.class);
        client.prepare(server.getIp(), server.getPort());
        for(Class c : SERVICES){
            client.getBus().register(injector.getInstance(c));
        }
        app.init(client);
        new Thread(client::connect).start();
    }

    public void disconnect() {
        MCClient client = injector.getInstance(MCClient.class);
        client.disconnect();
    }

}
