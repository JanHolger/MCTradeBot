package eu.bebendorf.mctradebot.inject;

import com.google.inject.AbstractModule;
import eu.bebendorf.mctradebot.Credentials;
import eu.bebendorf.mctradebot.app.App;
import eu.bebendorf.mctradebot.app.guessthenumber.GuessTheNumber;
import eu.bebendorf.mctradebot.bot.TradeBot;
import eu.bebendorf.mctradebot.client.MCClient;

import java.io.File;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            bind(Credentials.class).toInstance(Credentials.fromFile(new File("credentials.json")));
            bind(App.class).toInstance(new GuessTheNumber());
            bind(MCClient.class).toInstance(new MCClient());
            bind(TradeBot.class).toConstructor(TradeBot.class.getConstructor());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
