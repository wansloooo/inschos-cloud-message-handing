package com.inschos.cloud.message.handing.assist.kit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Properties;

public class MailKit {
    private static Logger log = LoggerFactory.getLogger(MailKit.class);

    public static void sendMessage(String to, String title,
                                   String messageText) throws MessagingException {
        String smtpHost = ConstantKit.MAIL_DEFAULT_SMTP_HOST;
        String from = ConstantKit.MAIL_DEFAULT_FROM;
        String fromUserPassword = ConstantKit.MAIL_DEFAULT_FROM_PASSWD;
        String messageType = "text/html;charset=utf-8";
        String nick = ConstantKit.MAIL_DEFAULT_FROM_NICK;
        int port = ConstantKit.MAIL_DEFAULT_SMTP_PORT;

        sendMessage(smtpHost,port,from,fromUserPassword,to,title,messageText,messageType,nick);
    }

    public static void sendMessage(String smtpHost,int port, String from,
                                   String fromUserPassword, String to, String title,
                                   String messageText, String messageType,String nick) throws MessagingException {
        // 第一步：配置javax.mail.Session对象

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.starttls.enable","true");//使用 STARTTLS安全连接

        props.put("mail.smtp.port", port);             //google使用465或587端口
        props.put("mail.smtp.auth", "true");        // 使用验证
//        props.put("mail.debug", "true");
//        props.put("mail.transport.protocol", "smtp");


        Session mailSession = Session.getInstance(props,new MailAuthenticator(from,fromUserPassword));

        // 第二步：编写消息
        if (log.isDebugEnabled()){
            log.debug("编写消息from——to:" + from + "——" + to);
        }
        try {
            nick=javax.mail.internet.MimeUtility.encodeText(nick);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("",e);
        }
        InternetAddress fromAddress = new InternetAddress(nick+" <"+from+">");
        InternetAddress toAddress = new InternetAddress(to);

        MimeMessage message = new MimeMessage(mailSession);

        message.setFrom(fromAddress);
        message.addRecipient(RecipientType.TO, toAddress);

        message.setSentDate(Calendar.getInstance().getTime());
        message.setSubject(title);
        message.setContent(messageText, messageType);

        // 第三步：发送消息
        Transport transport = mailSession.getTransport("smtp");

        transport.connect(smtpHost,port,from, fromUserPassword);

        transport.send(message, message.getRecipients(RecipientType.TO));
        //Transport.send(message);
        log.info("mail to "+to+" send success");
    }

    public static class MailAuthenticator extends Authenticator{
        String userName="";
        String password="";
        public MailAuthenticator(){

        }

        public MailAuthenticator(String userName,String password){
            this.userName=userName;
            this.password=password;
        }

        protected PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(userName, password);
        }
    }
}
