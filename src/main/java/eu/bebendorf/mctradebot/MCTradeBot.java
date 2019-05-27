package eu.bebendorf.mctradebot;

import eu.bebendorf.mctradebot.bot.TradeBot;
import eu.bebendorf.mctradebot.delay.Delay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MCTradeBot {

    private List<TradeBot> bots = new ArrayList<>();

    public MCTradeBot(){
        File botsFolder = new File("bots");
        if(!botsFolder.exists())
            botsFolder.mkdir();
        for(File botFolder : botsFolder.listFiles()){
            if(botFolder.isFile())
                continue;
            File credentialsFile = new File(botFolder, "credentials.json");
            if(!credentialsFile.exists())
                continue;
            File configFile = new File(botFolder, "config.json");
            if(!configFile.exists())
                continue;
            BotCredentials credentials = BotCredentials.fromFile(credentialsFile);
            BotConfig config = BotConfig.fromFile(configFile);
            if(credentials != null && config != null)
                bots.add(new TradeBot(config, credentials));
        }
    }

    public void start(){
        long delay = 0;
        for(TradeBot bot : bots){
            Delay.delay(delay, bot::connect);
            delay += 4100;
        }
    }

    public static void main(String[] args) {
        MCTradeBot app = new MCTradeBot();
        app.start();
    }

}
