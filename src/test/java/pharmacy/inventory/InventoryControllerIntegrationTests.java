package pharmacy.inventory;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pharmacy.catalog.PharmacyCatalog;
import pharmacy.laboratory.LabController;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerIntegrationTests {
	@Autowired
	MockMvc mvc; //to perform a post request to app and verify that it responds as expected
	@Autowired
	InventoryController inventoryController;
	@Autowired
	PharmacyCatalog pharmacyCatalog;
	@MockBean
	private UniqueInventory<UniqueInventoryItem> uniqueInventory;

	@Mock
	Cart cart;

	@Test
	@WithMockUser(roles = "BOSS")
	void inventoryOverviewTest() throws Exception{
		mvc.perform(get("/inventory-overview")).
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void restock() throws Exception{
		mvc.perform(get("/inventory-restock")).
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void reorder() throws Exception {
		mvc.perform(get("/inventory-reorder")).
				andExpect(status().isOk());
	}


	@Test
	@WithMockUser(roles = "BOSS")
	void reject() throws Exception {
		mvc.perform(get("/inventory-reject")).
				andExpect(status().isOk());
	}

	//@Test
	//@WithMockUser(roles = "BOSS")
	//void expiry() throws Exception {
	//	mvc.perform(get("/inventory-expiry")).
	//			andExpect(status().isOk());
	//}

}