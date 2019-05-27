package eu.bebendorf.mctradebot.bot.impl.bausucht;

import com.google.inject.AbstractModule;
import eu.bebendorf.mctradebot.bot.service.ControlService;
import eu.bebendorf.mctradebot.bot.service.MSGService;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.bot.service.PrepareService;

public class BausuchtModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PrepareService.class).toInstance(new PrepareServiceImpl());
        bind(PaymentService.class).toInstance(new PaymentServiceImpl());
        bind(ControlService.class).toInstance(new ControlServiceImpl());
        bind(MSGService.class).toInstance(new MSGServiceImpl());
    }
}
