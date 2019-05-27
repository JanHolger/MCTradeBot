package eu.bebendorf.mctradebot.bot.impl.bausucht;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
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
import eu.bebendorf.mctradebot.delay.MovementAnimation;

public class PrepareServiceImpl implements PrepareService {
    private boolean windowFound = false;

    @Inject
    private MCClient client;

    @Inject
    private PaymentService paymentService;

    public void prepare() {
        client.getSession().send(new ClientPlayerChangeHeldItemPacket(0));
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

        if (p.getName().equals("{\"text\":\"§bTeleporter\"}")) {
            windowFound = true;
            client.getSession().send(new ClientWindowActionPacket(p.getWindowId(), 0, 14, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
            Delay.delay(500, () -> {
                double[] startPos = new double[]{166.5, 96, 219.5};
                double[] endPos = new double[]{166.54, 96, 220.551};
                new Thread(new MovementAnimation(client, startPos, endPos)).start();
            });
            Delay.delay(1500, () -> {
                client.getSession().send(new ClientPlayerPlaceBlockPacket(new Position(166, 98, 224), BlockFace.NORTH, Hand.MAIN_HAND, 0.5f, 0.5f, 0.9f));
            });
            Delay.delay(2000, () -> {
                paymentService.checkBalance();
            });
        }
    }
}
