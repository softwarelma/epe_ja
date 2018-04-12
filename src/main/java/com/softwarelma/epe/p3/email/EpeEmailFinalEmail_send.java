package com.softwarelma.epe.p3.email;

import java.io.File;
import java.security.Security;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

/**
 * see import com.sun.mail.pop3.POP3SSLStore;
 * 
 * TODO for dependency 1.5.5 or above see a complete example at:
 * https://www.journaldev.com/2532/javamail-example-send-mail-in-java-smtp
 */
public final class EpeEmailFinalEmail_send extends EpeEmailAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "email_send, expected the props (required), to (required), cc (or null), bcc (or null), "
                + "subject (required), body (required) and attachments (or null) in that order.";

        String to = getStringAt(listExecResult, 1, postMessage);// required
        String cc = getStringAt(listExecResult, 2, postMessage, null);
        String bcc = getStringAt(listExecResult, 3, postMessage, null);
        String subject = getStringAt(listExecResult, 4, postMessage);// required
        String body = getStringAt(listExecResult, 5, postMessage);// required
        File[] attachments = null;

        boolean doLog = execParams.getGlobalParams().isPrintToConsole();
        Properties props = retrieveProps(listExecResult, postMessage);
        sendEmail(props, to, cc, bcc, subject, body, attachments, doLog);
        String str = "true";
        log(execParams, str);
        return createResult(str);
    }

    public static void sendEmail(final Properties props, String to, String cc, String bcc, String subject, String body,
            File[] attachments, boolean doLog) throws EpeAppException {
        setProxy(props);

        try {
            Session sess;
            // props.setProperty("mail.debug", "true");
            Object obj = props.get("mail.smtp.user");
            EpeAppUtils.checkNull("mail.smtp.user", obj);
            String from = obj.toString();

            if (props.getProperty("mail.smtp.ssl") != null
                    && props.getProperty("mail.smtp.ssl").equalsIgnoreCase("true")) {
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                String portStr = ((props.getProperty("mail.smtp.port") != null) ? (props.getProperty("mail.smtp.port"))
                        : "465");

                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.socketFactory.port", portStr);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");

                sess = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        String userName = ((props.getProperty("mail.smtp.user") != null)
                                ? props.getProperty("mail.smtp.user") : props.getProperty("mail.user"));
                        String passwd = ((props.getProperty("mail.smtp.passwd") != null)
                                ? props.getProperty("mail.smtp.passwd") : props.getProperty("mail.passwd"));
                        if (userName == null || passwd == null)
                            return null;
                        return new PasswordAuthentication(userName, passwd);
                    }
                });

            } else {
                // String portStr =
                // ((props.getProperty("mail.smtp.port") != null) ? (props
                // .getProperty("mail.smtp.port")) : "25");
                sess = Session.getInstance(props, null);
            }

            // sess.setDebug(true);
            MimeMessage mess = new MimeMessage(sess);
            mess.setSubject(subject);

            StringTokenizer toST = new StringTokenizer(to, ",;");
            while (toST.hasMoreTokens()) {
                Address addr = new InternetAddress(toST.nextToken());
                mess.addRecipient(Message.RecipientType.TO, addr);
            }

            if (from != null) {
                StringTokenizer fromST = new StringTokenizer(from, ",;");
                InternetAddress[] fromAddrs = new InternetAddress[fromST.countTokens()];
                for (int i = 0; fromST.hasMoreTokens(); i++) {
                    fromAddrs[i] = new InternetAddress(fromST.nextToken());
                }
                mess.addFrom(fromAddrs);
            }

            if (cc != null) {
                StringTokenizer ccST = new StringTokenizer(cc, ",;");
                while (ccST.hasMoreTokens()) {
                    Address addr = new InternetAddress(ccST.nextToken());
                    mess.addRecipient(Message.RecipientType.CC, addr);
                }
            }

            if (bcc != null) {
                StringTokenizer bccST = new StringTokenizer(bcc, ",;");
                while (bccST.hasMoreTokens()) {
                    Address addr = new InternetAddress(bccST.nextToken());
                    mess.addRecipient(Message.RecipientType.BCC, addr);
                }
            }

            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            if (body != null) {
                messageBodyPart.setText(body);
                multipart.addBodyPart(messageBodyPart);
            }

            if (attachments != null) {
                for (int i = 0; i < attachments.length; i++) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachments[i]);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(attachments[i].getName());
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            mess.setContent(multipart);

            Address[] allRecips = mess.getAllRecipients();
            if (doLog) {
                EpeAppLogger.log("Sending message (\"" + mess.getSubject() + "...\") to :");
                for (int i = 0; i < allRecips.length; i++)
                    EpeAppLogger.log(allRecips[i] + ";");
                EpeAppLogger.log("...");
            }

            Transport.send(mess);
            if (doLog)
                EpeAppLogger.log("Done.");
        } catch (MessagingException e) {
            throw new EpeAppException("Sending mail", e);
        }
    }

}
