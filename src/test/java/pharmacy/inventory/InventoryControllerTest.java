package pharmacy.inventory;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import pharmacy.catalog.Article;
import pharmacy.catalog.PharmacyCatalog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class InventoryControllerTest {

	@Autowired MockMvc mvc;
	@Autowired InventoryController inventoryController;
	@Autowired UniqueInventory<UniqueInventoryItem> inventory;
	@Autowired PharmacyCatalog catalog;

	@Mock Cart cart;
	@Mock Model model;
	@Mock CartItem cartItem;


	@Test
	@WithMockUser(roles = "BOSS")
	void select() throws Exception {

		Article a = catalog.findAll().stream().findFirst().get();

		inventoryController.select(a, cart);
		mvc.perform(get("/inventory-overview"))
				.andExpect(status().isOk());
	}

//	@Test
//	void testUpdate() {
//		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		Calendar cal = Calendar.getInstance();
//		int num = 2;
//		Article article1 = new Article("Name", 768392, "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", Article.ArticleType.CATALOG, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));
//		this.pharmacyCatalog.save(article1);
//		this.uniqueInventory.save(new UniqueInventoryItem(article1, Quantity.of(12)));
//		cart.addOrUpdateItem(article1, Quantity.of(1));
//		String id = "8";
//		inventoryController.update(id, num,cart);
//	}

	// Jaspers updated version
	@Test
	@WithMockUser(roles = "BOSS")
	void updateTest() throws Exception {
		int number = 5;

		// Get first item and the matching product identifier
		Article a = catalog.findAll().stream().findFirst().get();
		String id = String.valueOf(a.getId());

		// Add product to cart
		cart.addOrUpdateItem(a, Quantity.of(1));

		mvc.perform(get("/inventory-reorder"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void updateRestockTest() throws Exception {

		Quantity number = Quantity.of(5);
		Article a = catalog.findAll().stream().findFirst().get();
		String id = String.valueOf(a.getId());

		inventoryController.updateRestock(id, a, number, cart, model);

		mvc.perform(get("/inventory-restock"))
				.andExpect(status().isOk());
	}


	@Test
	@WithMockUser(roles = "BOSS")
	void updateReject() throws Exception {

		/* Would run if there's a method on how to input the "RequestParam("name")
		int number = 5;
		Article a = catalog.findAll().stream().findFirst().get();
		String id = String.valueOf(a.getId());

		inventoryController.updateReject(id, a, number, cart);
		*/

		mvc.perform(get("/inventory-reject"))
				.andExpect(status().isOk());
	}


	@Test
	@WithMockUser(roles = "BOSS")
	void cancel() {
		inventoryController.cancel(cart);
	}

}