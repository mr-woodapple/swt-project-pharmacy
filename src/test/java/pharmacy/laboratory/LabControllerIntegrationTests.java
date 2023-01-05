package pharmacy.laboratory;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pharmacy.catalog.PharmacyCatalog;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.emptyIterable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class LabControllerIntegrationTests {
	@Autowired
	MockMvc mvc; //to perform a post request to app and verify that it responds as expected
	@Autowired
	LabController controller;
	@Autowired
	PharmacyCatalog pharmacyCatalog;
	@Mock
	UniqueInventoryItem mockItem;

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE,LABORANT")
	void LabOrdersAccessTo() throws Exception {
		mvc.perform(get("/lab-orders"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
		;
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE,LABORANT")
	void LabNewOrderAccessTo() throws Exception {
		mvc.perform(get("/lab-new-order"));
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE,LABORANT")
	void LabNewMixtureAccessTo() throws Exception {
		mvc.perform(get("/add-mixture-components"));
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE,LABORANT")
	void LabReadyOrdersAccessTo() throws Exception {

		mvc.perform(get("/lab-ready-orders")).
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void ProductOrdersAccessTo() throws Exception {

		mvc.perform(get("/product-orders"));

	}




}
