package com.zilker.onlinejobsearch.utils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;

import com.zilker.onlinejobsearch.constants.Constants;
import com.zilker.onlinejobsearch.ui.RoleSeparated;
/*
 * class for sending mail to a user if a vacancy of his interest is published.
 */
public class NotifyUser {
	private static final Logger logger = Logger.getLogger(RoleSeparated.class.getName());
	public void sendNotification(String recipient){
		try{
			String to = recipient;
			boolean sessionDebug=false;
			
			Properties props = System.getProperties();
			
			props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", Constants.HOST);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(Constants.FROM));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(Constants.SUBJECT); msg.setSentDate(new Date());
            msg.setText(Constants.MESSAGETEXT);

           Transport transport=mailSession.getTransport("smtp");
           transport.connect(Constants.HOST, Constants.USER, Constants.PASS);
           transport.sendMessage(msg, msg.getAllRecipients());
           transport.close();
           logger.log(Level.INFO, "Please wait for few Seconds...");
        }catch(Exception ex)
        {
            System.out.println(ex);
        }
	}
}


