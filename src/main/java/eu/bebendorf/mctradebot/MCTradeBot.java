package eu.bebendorf.mctradebot;

import eu.bebendorf.mctradebot.app.guessthenumber.GuessTheNumber;
import eu.bebendorf.mctradebot.bot.TradeBot;

import java.io.File;

public class MCTradeBot {

    public static void main(String [] args){
        Credentials credentials = Credentials.fromFile(new File("credentials.json"));
        TradeBot tradeBot = new TradeBot(credentials.getUsername(), credentials.getPassword(), new GuessTheNumber());
        tradeBot.connect("195.201.164.116", 25565);
    }

}
