package eu.bebendorf.mctradebot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import eu.bebendorf.mctradebot.app.guessthenumber.GuessTheNumber;
import eu.bebendorf.mctradebot.bot.Server;
import eu.bebendorf.mctradebot.bot.TradeBot;
import eu.bebendorf.mctradebot.bot.impl.bausucht.BausuchtModule;
import eu.bebendorf.mctradebot.bot.impl.candycraft.CandyCraftModule;
import eu.bebendorf.mctradebot.inject.MainModule;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static eu.bebendorf.mctradebot.bot.Server.CANDYCRAFT;

public class MCTradeBot {
    @Getter
    private static Server server = CANDYCRAFT;

    public static void main(String [] args){
        List<Module> modules = new ArrayList<Module>() {{
            add(new MainModule());
        }};

        switch (server) {
            case CANDYCRAFT:
                modules.add(new CandyCraftModule());
                break;
            case BAUSUCHT:
                modules.add(new BausuchtModule());
                break;
        }

        Injector injector = Guice.createInjector(modules);

        TradeBot tradeBot = injector.getInstance(TradeBot.class);
        tradeBot.connect("195.201.164.116", 25565);
    }

}
