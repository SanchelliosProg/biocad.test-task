package com.biocad.tests;


import com.biocad.com.biocad.utils.MailRuSmtpClient;
import com.biocad.pages.LoginPage;
import com.biocad.pages.MailPage;
import com.biocad.pages.MessagePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EmailTest extends TestBase {

    private Logger log = LogManager.getLogger(getClass());

    @Test
    public void testMailCheck() {
        //Preconditions
        final String mailTopic = "Hello";
        final String mailContent = "How are you doing, buddy?";

        MailRuSmtpClient senderSmtpClient = new MailRuSmtpClient(senderUser);
        try {
            senderSmtpClient.sendEmail(targetUser.getEmail(), mailTopic, mailContent);
        } catch (MessagingException e) {
            log.error("Something went wrong while sending email from {} to {}", senderUser.toString(), targetUser.toString());
            e.printStackTrace();
        }

        //Test
        MailPage mailPage = new LoginPage(driver).navigateTo()
                .login(targetUser.getUsername(), targetUser.getPassword());
        MessagePage messagePage = mailPage.openMessageWithTitle(mailTopic);

        assertThat(messagePage.isMessageFound(), equalTo(true));

        assertThat(messagePage.getTopic(), equalTo(mailTopic));
        assertThat(messagePage.getSenderEmail(), equalTo(senderUser.getEmail()));
        assertThat(messagePage.getMessageContent(), equalTo(mailContent));
    }
}
