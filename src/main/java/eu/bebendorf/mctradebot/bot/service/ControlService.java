package eu.bebendorf.mctradebot.bot.service;

import java.util.Arrays;
import java.util.List;

public interface ControlService {

    default List<String> getAdmins() {
        return Arrays.asList(
                "JanHolger",
                "x7airworker"
        );
    }

}
