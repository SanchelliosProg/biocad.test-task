package com.biocad.tests;


import com.biocad.models.User;
import com.biocad.pages.AlternativeLoginPage;
import com.biocad.utils.MailRuSmtpClient;
import com.biocad.pages.LoginPage;
import com.biocad.pages.MailPage;
import com.biocad.pages.MessagePage;
import com.biocad.utils.TargetAccountsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EmailTest extends TestBase {

    private Logger log = LogManager.getLogger(getClass());

    @Test
    public void testMailCheck() throws Exception {
        //Preconditions
        final String mailTopic = "Hello";
        final String mailContent = "How are you doing, buddy?";

        MailRuSmtpClient senderSmtpClient = new MailRuSmtpClient(senderUser);
        try {
            senderSmtpClient.sendEmail(targetUser.getEmail(), mailTopic, mailContent);
        } catch (MessagingException e) {
            log.error("Something went wrong while sending email from {} to {} - tearing down case", senderUser.toString(), targetUser.toString());
            e.printStackTrace();
            throw new MessagingException("Finishing test due to exception");
        }

        //Test
        MailPage mailPage = new LoginPage(driver).navigateTo()
                .login(targetUser);

        MessagePage messagePage = mailPage.openMessageWithTitle(mailTopic);

        String actualTopic = messagePage.getTopic();
        String actualSender = messagePage.getSenderEmail();
        String actualMessageContent = messagePage.getMessageContent();

        assertThat(
                String.format("Was expecting '%s' in topic of the email but have got '%s'", mailTopic, actualTopic),
                actualTopic, equalTo(mailTopic));
        assertThat(
                String.format("Was expecting '%s' as sender email but have got '%s'", senderUser.getEmail(), actualSender),
                actualSender, equalTo(senderUser.getEmail()));
        assertThat(
                String.format("Was expecting '%s' in mail content but have got '%s'", mailContent, actualMessageContent),
                actualMessageContent, equalTo(mailContent));
    }
}
