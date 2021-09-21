package com.sales.market.service;

import com.sales.market.config.MailConfig;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private MailConfig mailConfig;

    public MailServiceImpl(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    @Override
    public boolean sendMessageForItemInventoryLowerBoundThreshold(String itemName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Item.Inventory.System");
        message.setTo("antonycc.123@gmail.com");
        message.setSubject("Item Inventory Lower Bound Threshold");
        message.setText("The stock quantity from " + itemName + " in the item inventory is minor than the lower bound threshold allowed.");
        mailConfig.getJavaMailSender().send(message);
        return true;
    }
}
