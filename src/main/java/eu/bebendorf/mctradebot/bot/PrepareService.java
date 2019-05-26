package eu.bebendorf.mctradebot.bot;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.*;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;
import eu.bebendorf.mctradebot.delay.Delay;
import eu.bebendorf.mctradebot.delay.MovementAnimation;

public class PrepareService {

    private MCClient client;
    private boolean windowFound = false;

    public PrepareService(MCClient client){
        this.client = client;
        client.getBus().register(this);
        client.registerService("prepare", this);
    }

    public void prepare(){
        if(client.getServer() == Server.CANDYCRAFT){
            client.getSession().send(new ClientPlayerChangeHeldItemPacket(1));
            client.getSession().send(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
        }
        if(client.getServer() == Server.BAUSUCHT){
            client.getSession().send(new ClientPlayerChangeHeldItemPacket(0));
            client.getSession().send(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
        }
    }

    @MCEvent
    public void onJoin(MCClient client, ServerJoinGamePacket p){
        prepare();
    }

    @MCEvent
    public void onWindow(MCClient client, ServerOpenWindowPacket p){
        if(windowFound)
            return;
        if(client.getServer() == Server.CANDYCRAFT){
            if(p.getName().equals("{\"text\":\"§cNavigator\"}")){
                windowFound = true;
                client.getSession().send(new ClientWindowActionPacket(p.getWindowId(), 0, 33, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
                Delay.delay(1000, () -> {
                    PaymentService paymentService = client.getService("payment");
                    paymentService.checkBalance();
                });
            }
        }
        if(client.getServer() == Server.BAUSUCHT){
            if(p.getName().equals("{\"text\":\"§bTeleporter\"}")){
                windowFound = true;
                client.getSession().send(new ClientWindowActionPacket(p.getWindowId(), 0, 14, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
                Delay.delay(500, () -> {
                    double[] startPos = new double[]{166.5, 96, 219.5};
                    double[] endPos = new double[]{166.54, 96, 220.551};
                    new Thread(new MovementAnimation(client, startPos, endPos)).start();
                });
                Delay.delay(1500, () -> {
                    client.getSession().send(new ClientPlayerPlaceBlockPacket(new Position(166, 98, 224),BlockFace.NORTH, Hand.MAIN_HAND, 0.5f, 0.5f, 0.9f));
                });
                Delay.delay(2000, () -> {
                    PaymentService paymentService = client.getService("payment");
                    paymentService.checkBalance();
                });
            }
        }
    }

    /*
    @MCEvent
    public void onItems(MCClient client, ServerWindowItemsPacket p){
        if(p.getWindowId() != 1)
            return;
        int i = 0;
        for(ItemStack stack : p.getItems()){
            String id = "null";
            if(stack != null)
                id = ""+stack.getId();
            System.out.println("Items["+i+"]: "+id);
            i++;
        }
    }
    */

}
