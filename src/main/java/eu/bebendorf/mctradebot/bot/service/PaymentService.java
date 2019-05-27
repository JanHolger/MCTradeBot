package eu.bebendorf.mctradebot.bot.service;

public interface PaymentService {

    void pay(String username, double amount);

    void checkBalance();

    double getBalance();
}
