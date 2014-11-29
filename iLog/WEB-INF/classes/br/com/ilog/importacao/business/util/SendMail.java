package br.com.ilog.importacao.business.util;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;


public class SendMail {

	public static final String EMAIL_SEPARATOR = ",";
	
	public static final String CHARSET = "ISO-8859-1";
	
	
	
	public static String getSeperator() {
		return EMAIL_SEPARATOR;
	}

	public String postMail(String subject, 
			String message ) throws Exception
	{
		Logger log = Logger.getRootLogger();
		Properties props = new Properties();

		ResourceBundle resBundleConf =  TemplateMessageHelper.getResourceBundle(MensagensSistema.EMAIL, Locale.getDefault());
		
		
		System.setProperty("mail.mime.charset", CHARSET);
		props.put("mail.smtp.host", resBundleConf.getString("mail.smtp.host"));
		props.put("mail.transport.protocol", "smtp"); 
		props.put("mail.smtp.auth", "false");

		
		
		//Authenticator autenticador = new SMTPAuthenticator();
		//Session session = Session.getDefaultInstance(props, autenticador);
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(false);
		
		Message msg = new MimeMessage(session);

		InternetAddress addressFrom = new InternetAddress(resBundleConf.getString("mail.smtp.from"));
		msg.setFrom(addressFrom);
		
		String[] recipients = resBundleConf.getString("destinatarios").split(";");
		
		log.info("***\nOriginal Email list:\t" + recipients.toString());
		
		log.info("***\nMessage:\n" + message);
		
		StringBuilder sentAddresses = new StringBuilder();
		StringBuilder notSentAddresses = new StringBuilder();
		StringBuilder invalidAddresses = new StringBuilder();
		
		msg.setSubject(subject);
		msg.setContent(message, "text/HTML");
		
		InternetAddress[] addressArray = new InternetAddress[1];
		
		for (String ia : recipients) {
			
			addressArray[0] = new InternetAddress(ia);
			msg.setRecipients(Message.RecipientType.TO, addressArray);
			
			try {
				Transport.send(msg);
				sentAddresses.append(addressArray[0].toString() + EMAIL_SEPARATOR);
				
			} catch (SendFailedException sfe) {
				if (sfe.getValidUnsentAddresses().length > 0) {
					for (Address a : sfe.getValidUnsentAddresses()) {
						notSentAddresses.append(a.toString() + EMAIL_SEPARATOR);
					}
				}
				if (sfe.getInvalidAddresses().length > 0) {
					for (Address a : sfe.getInvalidAddresses()) {
						invalidAddresses.append(a.toString() + EMAIL_SEPARATOR);
					}
				}
			}
			
			if (notSentAddresses.length() > 0) {
				notSentAddresses.setLength(notSentAddresses.length() - EMAIL_SEPARATOR.length());
			}
			
			if (invalidAddresses.length() > 0) {
				invalidAddresses.setLength(invalidAddresses.length() - EMAIL_SEPARATOR.length());
			}
		}
		
		if (sentAddresses.length() > 0) {
			sentAddresses.setLength(sentAddresses.length() - EMAIL_SEPARATOR.length());
		}
		
		if (notSentAddresses.length() > 0) {
			notSentAddresses.setLength(notSentAddresses.length() - EMAIL_SEPARATOR.length());
		}
		
		if (invalidAddresses.length() > 0) {
			invalidAddresses.setLength(invalidAddresses.length() - EMAIL_SEPARATOR.length());
		}
		
		log.info("***\nSuccess:\t" + sentAddresses.toString() +
				"***\nFailure:\t" + notSentAddresses.toString() +
				"***\nInvalid:\t" + invalidAddresses.toString());

		return invalidAddresses.toString();
	}
	
    public class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
		public PasswordAuthentication getPasswordAuthentication() {
        	//String usuario = SetfApplicationBean.getMensagem("mail.smtp.username");
        	//String senha = SetfApplicationBean.getMensagem("mail.smtp.password");
			//return new PasswordAuthentication(usuario, senha);
        	return null; 
        }
    }

	public String postMail(String subject, 
						   String message,
						   String destinatarios) throws Exception{
		Logger log = Logger.getRootLogger();
		Properties props = new Properties();

		ResourceBundle resBundleConf = TemplateMessageHelper.getResourceBundle(MensagensSistema.EMAIL, Locale.getDefault());
		
		
		System.setProperty("mail.mime.charset", CHARSET);
		props.put("mail.smtp.host", resBundleConf.getString("mail.smtp.host"));
		props.put("mail.transport.protocol", "smtp"); 
		props.put("mail.smtp.auth", "false");
		
		//Authenticator autenticador = new SMTPAuthenticator();
		//Session session = Session.getDefaultInstance(props, autenticador);
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(false);
		
		Message msg = new MimeMessage(session);

		InternetAddress addressFrom = new InternetAddress(resBundleConf.getString("mail.smtp.from"));
		msg.setFrom(addressFrom);
		
		String[] recipients = destinatarios.split(";");
		
		log.info("***\nOriginal Email list:\t" + recipients.toString());
		
		log.info("***\nMessage:\n" + message);
		
		StringBuilder sentAddresses = new StringBuilder();
		StringBuilder notSentAddresses = new StringBuilder();
		StringBuilder invalidAddresses = new StringBuilder();
		
		msg.setSubject(subject);
		msg.setContent(message, "text/HTML");
		
		InternetAddress[] addressArray = new InternetAddress[1];
		
		for (String ia : recipients) {
			
			addressArray[0] = new InternetAddress(ia);
			msg.setRecipients(Message.RecipientType.TO, addressArray);
			
			try {
				Transport.send(msg);
				sentAddresses.append(addressArray[0].toString() + EMAIL_SEPARATOR);
				
			} catch (SendFailedException sfe) {
				if (sfe.getValidUnsentAddresses().length > 0) {
					for (Address a : sfe.getValidUnsentAddresses()) {
						notSentAddresses.append(a.toString() + EMAIL_SEPARATOR);
					}
				}
				if (sfe.getInvalidAddresses().length > 0) {
					for (Address a : sfe.getInvalidAddresses()) {
						invalidAddresses.append(a.toString() + EMAIL_SEPARATOR);
					}
				}
			}
			
			if (notSentAddresses.length() > 0) {
				notSentAddresses.setLength(notSentAddresses.length() - EMAIL_SEPARATOR.length());
			}
			
			if (invalidAddresses.length() > 0) {
				invalidAddresses.setLength(invalidAddresses.length() - EMAIL_SEPARATOR.length());
			}
		}
		
		if (sentAddresses.length() > 0) {
			sentAddresses.setLength(sentAddresses.length() - EMAIL_SEPARATOR.length());
		}
		
		if (notSentAddresses.length() > 0) {
			notSentAddresses.setLength(notSentAddresses.length() - EMAIL_SEPARATOR.length());
		}
		
		if (invalidAddresses.length() > 0) {
			invalidAddresses.setLength(invalidAddresses.length() - EMAIL_SEPARATOR.length());
		}
		
		log.info("***\nSuccess:\t" + sentAddresses.toString() +
				"***\nFailure:\t" + notSentAddresses.toString() +
				"***\nInvalid:\t" + invalidAddresses.toString());

		return invalidAddresses.toString();
	}
    
}
