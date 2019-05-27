package eu.bebendorf.mctradebot.bot.impl.candycraft;

import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.google.inject.Inject;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.bot.service.PrepareService;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import eu.bebendorf.mctradebot.delay.Delay;

public class PrepareServiceImpl implements PrepareService {
    private boolean windowFound = false;

    @Inject
    private MCClient client;

    @Inject
    private PaymentService paymentService;

    @Override
    public void prepare() {
        client.getSession().send(new ClientPlayerChangeHeldItemPacket(1));
        client.getSession().send(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
    }

    @MCEvent
    public void onJoin(MCClient client, ServerJoinGamePacket p) {
        prepare();
    }

    @MCEvent
    public void onWindow(MCClient client, ServerOpenWindowPacket p) {
        if (windowFound)
            return;

        if (p.getName().equals("{\"text\":\"§cNavigator\"}")) {
            windowFound = true;
            client.getSession().send(new ClientWindowActionPacket(p.getWindowId(), 0, 33, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
            Delay.delay(1000, () -> {
                paymentService.checkBalance();
            });
        }
    }
}
