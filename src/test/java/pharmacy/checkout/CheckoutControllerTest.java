package pharmacy.checkout;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import pharmacy.catalog.Article;
import pharmacy.catalog.CatalogController;
import pharmacy.catalog.PharmacyCatalog;

import javax.xml.catalog.Catalog;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckoutControllerTest {

	@Autowired MockMvc mvc;
	@Autowired CheckoutController checkoutController;
	@Autowired CatalogController catalogController;
	@Autowired UniqueInventory inventory;
	@Autowired PharmacyCatalog catalog;

	@Mock Cart cart;
	@Mock Model model;
	@Mock Article product;

	@Test
	@WithMockUser (roles = "BOSS, EMPLOYEE, DOC, CUSTOMER")
	void AccessToCart() throws Exception{
		mvc.perform(get("/cart"));
	}

	@Test
	@WithMockUser (roles = "BOSS, EMPLOYEE")
	void AccessToNewProductOrder() throws Exception{
		mvc.perform(get("/new-product-order"));
	}

	@Test
	@WithMockUser (roles = "BOSS, EMPLOYEE")
	void AccessToProductOrders() throws Exception{
		mvc.perform(get("/product-orders"));
	}

	@Test
	@WithMockUser (roles = "BOSS, EMPLOYEE")
	void AccessToAccountingReceipt() throws Exception{
		mvc.perform(get("/accounting-receipt"));
	}

	@Test
	void testAddOrder() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.checkoutController)
				.build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	void testAddProduct() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/cart");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.checkoutController)
				.build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	void testBasket() throws Exception {
		SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.checkoutController).build().perform(requestBuilder);
			actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void testPrintReceipt() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/print");
		MockMvcBuilders.standaloneSetup(this.checkoutController)
				.build()
				.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().size(1))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cart"))
				.andExpect(MockMvcResultMatchers.view().name("accounting-receipt"))
				.andExpect(MockMvcResultMatchers.forwardedUrl("accounting-receipt"));
	}

	@Test
	void testRemoveOneProduct() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/remove").param("pid", "foo");
		MockMvcBuilders.standaloneSetup(this.checkoutController)
				.build()
				.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(MockMvcResultMatchers.model().size(1))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cart"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/cart"))
				.andExpect(MockMvcResultMatchers.redirectedUrl("/cart"));
	}

	@Test
	void testRemoveProducts() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/delete");
		MockMvcBuilders.standaloneSetup(this.checkoutController)
				.build()
				.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(MockMvcResultMatchers.model().size(1))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cart"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/"))
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}

	@Test
	void testAddAndUpdateProduct() throws Exception {
		Article a = catalog.findAll().stream().findFirst().get();
		String id = String.valueOf(a.getId());
		int number = 5;

		// Add product to cart
		checkoutController.addProduct(a, cart, model);
		//assertEquals(cart.stream().findAny().get().getQuantity().getAmount().intValue(), 1);


		// Update cart
		//checkoutController.updateProduct(id, a, number, cart);

		mvc.perform(get("/cart"))
				.andExpect(status().isOk());
	}

	@Test
	void addOrderTest(){
		String c = this.checkoutController.basket();

		checkoutController.addOrder(product, cart);
		assertThat(c.equals(1));
	}
}
