package eu.bebendorf.mctradebot.bot;

import com.google.inject.Module;
import eu.bebendorf.mctradebot.bot.impl.bausucht.BausuchtModule;
import eu.bebendorf.mctradebot.bot.impl.candycraft.CandyCraftModule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Server {

    CANDYCRAFT("195.201.164.116", 25565),
    BAUSUCHT("play.bausucht.net", 25565);

    String ip;
    int port;

    Module getModule() {
        switch (this) {
            case CANDYCRAFT:
                return new CandyCraftModule();
            case BAUSUCHT:
                return new BausuchtModule();
            default:
                return null;
        }
    }
}
