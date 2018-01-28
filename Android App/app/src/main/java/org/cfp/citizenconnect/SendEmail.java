package org.cfp.citizenconnect;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import org.cfp.citizenconnect.Model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import io.realm.Realm;


/**
 * Created by shahzaibshahid on 27/01/2018.
 */

public class SendEmail {
    private com.google.api.services.gmail.Gmail mService = null;
    private String _subject;
    String toRecipient;
    String _body;
    User user;

    public SendEmail(GoogleAccountCredential credential, String toRecipient, String subject, String body) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Zero Point")
                .build();
        this.toRecipient = toRecipient;
        Realm realm = Realm.getDefaultInstance();
        user = User.getUserInstance(realm);
        this._subject = subject;
        this._body = body;
    }
    public String getDataFromApi() throws IOException {
        // getting Values for to Address, from Address, Subject and Body
        String user = "Zero Point";
        String to = toRecipient;
        String from = this.user.getEmail();
        String subject = _subject;
        String body = _body;
        MimeMessage mimeMessage;
        String response = "";
        try {
            mimeMessage = createEmail(to, from, subject, body);
            response = sendMessage(mService, user, mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return response;
    }

    // Method to send email
    private String sendMessage(Gmail service,
                               String userId,
                               MimeMessage email)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        // GMail's official method to send email with oauth2.0
        message = service.users().messages().send("me", message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message.getId();
    }

    // Method to create email Params
    private MimeMessage createEmail(String to,
                                    String from,
                                    String subject,
                                    String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        InternetAddress tAddress = new InternetAddress(to);
        InternetAddress fAddress = new InternetAddress(from);

        email.setFrom(fAddress);
        email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
        email.setSubject(subject);

        Multipart multipart = new MimeMultipart();

        BodyPart textBody = new MimeBodyPart();
        textBody.setText(bodyText);
        multipart.addBodyPart(textBody);

        email.setContent(multipart);
        return email;
    }

    private Message createMessageWithEmail(MimeMessage email)
            throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}