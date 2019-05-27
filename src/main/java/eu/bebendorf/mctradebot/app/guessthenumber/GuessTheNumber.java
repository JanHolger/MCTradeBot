package eu.bebendorf.mctradebot.app.guessthenumber;

import java.util.Random;

import com.google.inject.Inject;
import eu.bebendorf.mctradebot.app.App;
import eu.bebendorf.mctradebot.bot.service.MSGService;
import eu.bebendorf.mctradebot.bot.event.PaymentEvent;
import eu.bebendorf.mctradebot.bot.service.PaymentService;
import eu.bebendorf.mctradebot.client.MCClient;
import eu.bebendorf.mctradebot.client.MCEvent;

public class GuessTheNumber implements App {

    private static final int MAX_NUMBER = 1000;
    private static final float PAYOUT_PERCENTAGE = 0.95f;

    private int guessNumber= generateNumber();
    private double pot = 0;
    @Inject
    private PaymentService paymentService;
    @Inject
    private MSGService msgService;

    public void init(MCClient client) {
        client.getBus().register(this);
    }

    private int generateNumber(){
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(MAX_NUMBER)+1;
    }

    private boolean isInt(double value){
        double intValue = Math.floor(value);
        return value - intValue == 0.0;
    }

    @MCEvent
    public void onPayment(MCClient client, PaymentEvent e) {
        if (e.getAmount() > MAX_NUMBER) {
            paymentService.pay(e.getUsername(), e.getAmount());
            msgService.send(e.getUsername(), "Die maximale Zahl liegt bei " + MAX_NUMBER + "! Dein Geld wurde dir wieder zurückgezahlt!");
        } else if(!isInt(e.getAmount())) {
            paymentService.pay(e.getUsername(), e.getAmount());
            msgService.send(e.getUsername(), "Die Zahl hat keine Nachkommastellen! Dein Geld wurde dir wieder zurückgezahlt!");
        } else {
            pot += e.getAmount();
            if (e.getAmount() == guessNumber) {
                msgService.send(e.getUsername(), "Du hast die Zahl erraten! Der Pot wird dir nun ausgezahlt!");
                client.sendMessage(e.getUsername()+" hat gewonnen! Die Zahl war: "+guessNumber);
                paymentService.pay(e.getUsername(), pot * PAYOUT_PERCENTAGE);
                guessNumber = generateNumber();
                pot = 0;
            } else if (e.getAmount() < guessNumber) {
                msgService.send(e.getUsername(), "Die Zahl ist größer!");
            } else if (e.getAmount() > guessNumber) {
                msgService.send(e.getUsername(), "Die Zahl ist kleiner!");
            }
        }
    }

}
