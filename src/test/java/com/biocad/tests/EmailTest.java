package com.biocad.tests;


import com.biocad.pages.LoginPage;
import com.biocad.pages.MailPage;
import com.biocad.pages.MessagePage;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EmailTest extends TestBase {

    @Test
    public void testMailCheck() {
        MailPage mailPage = new LoginPage(driver).navigateTo()
                .login(username, password);
        MessagePage messagePage = mailPage.openMessageWithTitle("TEST_001");

        assertThat(messagePage.isMessageFound(), equalTo(true));

        assertThat(messagePage.getTheme(), equalTo("TEST_001"));
        assertThat(messagePage.getSenderEmail(), equalTo("vasyania2009@gmail.com"));
        assertThat(messagePage.getMessageContent(), equalTo("beep-beep"));
    }

    @Test
    public void testAnotherMailCheck() {
        MailPage mailPage = new LoginPage(driver).navigateTo()
                .login(username, password);
        MessagePage messagePage = mailPage.openMessageWithTitle("TEST_002");

        assertThat(messagePage.isMessageFound(), equalTo(true));

        assertThat(messagePage.getTheme(), equalTo("TEST_002"));
        assertThat(messagePage.getSenderEmail(), equalTo("vasyania2009@gmail.com"));
        assertThat(messagePage.getMessageContent(), equalTo("beep-beep"));
    }

}
