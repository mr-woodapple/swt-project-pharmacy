package pharmacy.catalog;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import static org.hamcrest.Matchers.*;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogControllerIntegrationTests {
	@Autowired
	MockMvc mvc; //to perform a post request to app and verify that it responds as expected
	@Autowired
	CatalogController catalogController;
	@Autowired
	PharmacyCatalog pharmacyCatalog;

	@MockBean
	private UniqueInventory<UniqueInventoryItem> uniqueInventory;
	@Mock
	Model model;


	@Test
	@WithMockUser(roles = "BOSS")
	void stock () throws Exception {
		mvc.perform(get("/product-overview")).
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}



	@Test
	@WithMockUser(roles = "BOSS")
	void populateCatalogTest() throws Exception {
		mvc.perform(get("/catalog")).
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));

	}

	@Test
	@WithMockUser(roles = "DOC")
	void populateDocCatalogTest() throws Exception {
		mvc.perform(get("/doc-catalog")).
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));

	}

	@Test
	@WithMockUser(roles = "CUSTOMER")
	void populateCustomerCatalogTest() throws Exception {
		mvc.perform(get("/customer-catalog"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

//	@Test
//	@WithMockUser(roles = "EMPLOYEE")
//	void prescriptionCatalogTest1() throws Exception {
//		int num1 = 1;
//		catalogController.prescriptionCatalog(model,num1);
//		mvc.perform(get("/inventory-overview"))
//				.andExpect(status().isOk())
//				.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
//
//	}
//	@Test
//	@WithMockUser(roles = "EMPLOYEE")
//	void prescriptionCatalogTest2() throws Exception {
//		int num2 = 2;
//		catalogController.prescriptionCatalog(model,num2);
//		mvc.perform(get("/product-overview"))
//				.andExpect(status().isOk())
//				.andExpect(model().attribute("catalog", is(not(emptyIterable()))));
//
//	}

	@Test
	void addStock() {
		String name = "Name";
		int quantity = 2;
		int id = 845629;
		double price = 10.0;
		double purchasePrice = 15.0;
		String description = "Nothing";
		boolean prescription = true;
		String packSize = "No info";
		catalogController.addStock(name, quantity, id, price, purchasePrice, description, prescription, packSize, model);

	}

}