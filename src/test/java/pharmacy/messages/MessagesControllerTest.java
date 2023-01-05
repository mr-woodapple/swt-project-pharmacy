package pharmacy.messages;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@AutoConfigureMockMvc
class MessagesControllerTest {

	@Autowired private MessagesController messagesController;
	@Autowired MockMvc mvc;

	@MockBean
	private MessagesRepository messagesRepository;

	@Test
	@WithMockUser(roles = "BOSS")
	void viewMessagesBossTest() throws Exception {
		mvc.perform(get("/messages"))
				.andExpect(model().attributeExists("messageList"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void addEntryTest() throws Exception {
		MessagesForm form = new MessagesForm("NO payrise", "Seriously guys, please stop asking. I need to buy my Tesla first.");

		messagesController.addEntry(form);
		assertThat(messagesRepository.findAll()).hasSize(0);
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void removeEntryTest() throws Exception {

		messagesRepository.save(new MessagesEntry("NO payrise", "Seriously guys, please stop asking. I need to buy my Tesla first."));

		long id = 7;
		messagesController.removeEntry(id);
		assertThat(messagesRepository.findAll()).isEmpty();

		mvc.perform(get("/messages"));
	}

}

