package eu.bebendorf.mctradebot.bot.impl.bausucht;

import com.google.inject.AbstractModule;
import eu.bebendorf.mctradebot.bot.service.ControlService;
import eu.bebendorf.mctradebot.bot.service.MSGService;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.bot.service.PrepareService;

public class BausuchtModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PrepareService.class).to(PrepareServiceImpl.class);
        bind(PaymentService.class).to(PaymentServiceImpl.class);
        bind(ControlService.class).to(ControlServiceImpl.class);
        bind(MSGService.class).to(MSGServiceImpl.class);
    }
}
