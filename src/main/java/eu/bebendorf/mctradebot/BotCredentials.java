package eu.bebendorf.mctradebot;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BotCredentials {

    String username;
    String password;

    public static BotCredentials fromFile(File file) {
        try {
            JsonObject json = new GsonBuilder().create().fromJson(new FileReader(file), JsonObject.class);
            return new BotCredentials(json.get("username").getAsString(), json.get("password").getAsString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
