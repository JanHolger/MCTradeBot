package eu.bebendorf.mctradebot.bot.service;


public interface PrepareService {

    void prepare();

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
