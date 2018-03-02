package com.softwarelma.epe.p3.email;

import java.io.File;
import java.security.Security;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

/**
 * see import com.sun.mail.pop3.POP3SSLStore;
 */
public final class EpeEchoFinalEmail_send extends EpeEmailAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        // TODO Auto-generated method stub
        return null;
    }

    public static boolean sendEmail(String username, String subject, String body) {
        ResourceBundle resourceBundleEmail = ResourceBundle.getBundle("email");
        String pwd = resourceBundleEmail.getString("mail.smtp.passwd");

        if (pwd == null || pwd.isEmpty() || pwd.equals("*")) {
            return false;
        }

        Properties props = getProperties(resourceBundleEmail);
        String from = resourceBundleEmail.getString("mail.smtp.user");
        String to = resourceBundleEmail.getString("to");
        String cc = null;
        String bcc = null;
        File[] attachments = null;
        boolean toStdOut = false;// for debug true;

        try {
            if (sendMail(props, from, to, cc, bcc, subject, attachments, body, toStdOut) == 0) {
                return true;
            }
        } catch (Exception e) {
            new EpeAppException("Wrapping exception for send mail. User " + username, e);
            return false;
        }

        return false;
    }

    private static Properties getProperties(ResourceBundle resourceBundleEmail) {
        Properties props = new Properties();
        String key, value;
        Enumeration<String> enumKey = resourceBundleEmail.getKeys();

        while (enumKey.hasMoreElements()) {
            key = enumKey.nextElement();
            value = resourceBundleEmail.getString(key);

            if (key.startsWith("http.proxy") || key.startsWith("http.nonProxy")) {
                if (value == null || value.isEmpty() || value.equals("*")) {
                    continue;
                }

                System.setProperty(key, value);
                continue;
            }

            props.put(key, value);
        }

        return props;
    }

    /**
     * from: http://forums.sun.com/thread.jspa?threadID=591321&start=15
     * 
     * google search: javax.mail.NoSuchProviderException: No provider for smtps
     * 
     * @param props
     *            not null
     * @param from
     *            not null
     * @param to
     *            not null
     * @param cc
     * @param bcc
     * @param subject
     *            not null
     * @param attachments
     * @param body
     * @param toStdOut
     *            true for log output
     * @return 0 if it sends successfully.
     * @throws javax.mail.internet.AddressException
     * @throws javax.mail.MessagingException
     * @throws javax.mail.NoSuchProviderException
     */
    private static int sendMail(final Properties props, String from, String to, String cc, String bcc, String subject,
            File[] attachments, String body, boolean toStdOut) throws javax.mail.internet.AddressException,
            javax.mail.MessagingException, javax.mail.NoSuchProviderException {
        Session sess;
        // props.setProperty("mail.debug", "true");
        if (props.getProperty("mail.smtp.ssl") != null && props.getProperty("mail.smtp.ssl").equalsIgnoreCase("true")) {
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
                            ? props.getProperty("mail.smtp.user")
                            : props.getProperty("mail.user"));
                    String passwd = ((props.getProperty("mail.smtp.passwd") != null)
                            ? props.getProperty("mail.smtp.passwd")
                            : props.getProperty("mail.passwd"));
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
        if (toStdOut) {
            // System.out.println("Sending message (\"" +
            // mess.getSubject().substring(0,10) + "...\") to :");
            System.out.println("Sending message (\"" + mess.getSubject() + "...\") to :");
            for (int i = 0; i < allRecips.length; i++) {
                System.out.print(allRecips[i] + ";");
            }
            System.out.println("...");
        }

        Transport.send(mess);
        if (toStdOut) {
            System.out.println("Done.");
        }
        return 0;
    }

}
