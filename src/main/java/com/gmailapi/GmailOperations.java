package com.gmailapi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

public class GmailOperations {

	public static void sendMessage(Gmail service, String userId, MimeMessage email)
			throws MessagingException, IOException {
		Message message = createMessageWithEmail(email);
		message = service.users().messages().send(userId, message).execute();

		System.out.println("Message id: " + message.getId());
		System.out.println(message.toPrettyString());
	}
	public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(from)); //me
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to)); //
		email.setSubject(subject);
        email.setText(bodyText);
		return email;
	}
	public static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		email.writeTo(baos);
		String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	public static void main(String[] args) throws IOException, GeneralSecurityException, MessagingException {
			Gmail service=GmailApi.getGmailService();
			// mesaj olusturma islemi
			MimeMessage Mimemessage=createEmail(email@, header, body, inf);
			Message message=createMessageWithEmail(Mimemessage);

			message= service.users().messages().send("me",message).execute();

			System.out.println("Message id: "+ message.getId());
			System.out.println(message.toPrettyString());

	}
}