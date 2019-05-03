package io.github.gotonode.frostbook.main;

import io.github.gotonode.frostbook.MyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ComponentScan(value = "io.github.gotonode.frostbook")
public class MainTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void anonymousUserCanAccessLinks() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/register")).andExpect(status().isOk());
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/help")).andExpect(status().isOk());
        mockMvc.perform(get("/about")).andExpect(status().isOk());
        mockMvc.perform(get("/search")).andExpect(status().isOk());
    }

    @Test
    public void anonymousUserIsRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/friends")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/requests")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/gallery")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/id")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void accessingLogoutTriggersRedirect() throws Exception {
        mockMvc.perform(get("/logout")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void indexContainsAppName() throws Exception {

        MvcResult res = mockMvc.perform(get("/")).andReturn();

        String content = res.getResponse().getContentAsString();

        assertTrue(content.contains(MyApplication.APP_NAME));
    }

}