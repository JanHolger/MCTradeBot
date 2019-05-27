package eu.bebendorf.mctradebot;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import eu.bebendorf.mctradebot.bot.Server;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class BotConfig {

    Server server;

    public static BotConfig fromFile(File file){
        try {
            JsonObject json = new GsonBuilder().create().fromJson(new FileReader(file), JsonObject.class);
            return new BotConfig(Server.valueOf(json.get("server").getAsString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
