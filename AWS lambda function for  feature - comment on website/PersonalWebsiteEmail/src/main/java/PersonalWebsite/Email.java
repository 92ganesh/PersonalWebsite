package PersonalWebsite;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 *   AWS Lambda function to send email by REST API
 *   this lambda function has been developed for a feature on my website. It will send me email notification
 *   when someone comments on the website.
 *   request should be a post request with body as { email:"valid email id", body:"details to be sent"}
 * */
public class Email implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>{
    private static final String senderEmail = "";
    private static final String senderPassword = "";
    private static final String recipientEmail = "";

    public static void main(String[] args) {
        send(senderEmail, senderPassword, recipientEmail,"sub text","body text");
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context)
    {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        CommentDetails comment = gson.fromJson(event.getBody(), CommentDetails.class);

        LambdaLogger logger = context.getLogger();

        boolean emailSent = send(senderEmail, senderPassword, recipientEmail,	comment.getName()+" commented on website", comment.toString());
        if(!emailSent){
            System.out.println("*** Could not send email ***");
            System.out.println("*** Email content: " + comment.toString() + " ***");
        }
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        response.setBody((emailSent)?"{\"result\":\"message delivered successfully\"}":
                "{\"result\":\"message could not be sent! Check if email address is valid and exists\"}");
        return response;
    }

    /**
     * sends mail using gmail SMTP server and javaMail API
     * @param from      sender's email
     * @param password  sender's password
     * @param to        email list of all receivers
     * @param sub       subject of the email
     * @param msg       body of the email
     * @return true if email is sent succesfully otherwise false
     */
    public static boolean send(final String from,final String password,String[] to,String sub,String msg){
        boolean emailSent = false;

        //Get properties object
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        // Here 465 states that SSL encryption will be used. But i guess its not working in our case as security detail of received mails shows TLS.
        // May be we need to provide details of SSL. As of now its not a major issue
        /* For Gmail use following properties
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        */

        // use following properties for outlook
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");

        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                }
        );

        //compose and send message
        try {
            MimeMessage messenger = new MimeMessage(session);

            messenger.setFrom(new InternetAddress(from));

            InternetAddress[] addresses=new InternetAddress[to.length];
            for(int i=0;i<to.length;i++)
                addresses[i]=new InternetAddress(to[i]);

            messenger.setRecipients(Message.RecipientType.TO, addresses);
            messenger.setSubject(sub);
            messenger.setText(msg);
            Transport.send(messenger);
            System.out.println("mail sent successfully to:- ");
            for(String each:to)
                System.out.println(each);
            emailSent = true;
        } catch (MessagingException mex) {
            System.out.println("\n--Exception handling in msgsendsample.java");

            mex.printStackTrace();
            System.out.println();
            Exception ex = mex;
            do {
                if (ex instanceof SendFailedException) {
                    SendFailedException sfex = (SendFailedException)ex;
                    Address[] invalid = sfex.getInvalidAddresses();
                    if (invalid != null) {
                        System.out.println("    ** Invalid Addresses");
                        for (int i = 0; i < invalid.length; i++)
                            System.out.println("         " + invalid[i]);
                    }
                    Address[] validUnsent = sfex.getValidUnsentAddresses();
                    if (validUnsent != null) {
                        System.out.println("    ** ValidUnsent Addresses");
                        for (int i = 0; i < validUnsent.length; i++)
                            System.out.println("         "+validUnsent[i]);
                    }
                    Address[] validSent = sfex.getValidSentAddresses();
                    if (validSent != null) {
                        System.out.println("    ** ValidSent Addresses");
                        for (int i = 0; i < validSent.length; i++)
                            System.out.println("         "+validSent[i]);
                    }
                }
                System.out.println();
                if (ex instanceof MessagingException)
                    ex = ((MessagingException)ex).getNextException();
                else
                    ex = null;
            } while (ex != null);
        }

        return emailSent;
    }

    /**
     * overloaded function which sends mail to single recipient using gmail SMTP server and javaMail API
     * @param from      sender's email
     * @param password  sender's password
     * @param to        receiver's email
     * @param sub       subject of the email
     * @param msg       body of the email
     * @return  true if email is sent succesfully otherwise false
     */
    public static boolean send(String from,String password,String to,String sub,String msg){
        String[] recipient = {to};
        return send(from,password,recipient,sub,msg);  // enter your details
    }
}

/**
 * model class to map json string to object
 */
class CommentDetails{
    private String name;
    private String contact;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        contact = (contact.length()==0) ? "" :  " ("+contact+")";
        return name+contact+" says:-\n"+message;
    }
}