package eu.bebendorf.mctradebot.inject;

import com.google.inject.AbstractModule;
import eu.bebendorf.mctradebot.BotConfig;
import eu.bebendorf.mctradebot.BotCredentials;
import eu.bebendorf.mctradebot.app.App;
import eu.bebendorf.mctradebot.client.MCClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainModule extends AbstractModule {

    BotConfig config;
    BotCredentials credentials;
    App app;

    @Override
    protected void configure() {
        bind(BotCredentials.class).toInstance(credentials);
        bind(App.class).toInstance(app);
        bind(MCClient.class).toInstance(new MCClient());
        bind(BotConfig.class).toInstance(config);
    }
}
