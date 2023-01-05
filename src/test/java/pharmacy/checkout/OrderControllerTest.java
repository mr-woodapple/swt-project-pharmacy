package pharmacy.checkout;


import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.*;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
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
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import static org.salespointframework.core.Currencies.EURO;

import pharmacy.accounting.AccountingManagement;

import javax.money.MonetaryAmount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

	@Autowired MockMvc mvc;
	@Autowired OrderController orderController;
	@Autowired private UserAccountManagement userAccountManagement;
	@Autowired AccountingManagement accountingManagement;
	@Mock Cart cart;
	@Mock Model model;


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
	@WithMockUser (roles = "BOSS, DOC")
	void AccessToDocCheckout() throws Exception{
		mvc.perform(get("/docCheckout"));
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
	void bossBuyProductsTest(){
		Optional<UserAccount> userAccount = this.userAccountManagement.findByUsername("HaKr10");

		orderController.buyProducts(cart, userAccount, model);
		assertThat(accountingManagement.findAll()).isNotEmpty();
	}

	@Test
	void customerBuyProductsTest(){
		Optional<UserAccount> userAccount = this.userAccountManagement.findByUsername("customer");

		orderController.buyProducts(cart, userAccount, model);
		assertThat(accountingManagement.findAll()).isNotEmpty();
	}

	@Test
	void buyDocProductsTest(){
		Optional<UserAccount> userAccount = this.userAccountManagement.findByUsername("5678");

		orderController.buyDocProducts(cart, userAccount, model);
		assertThat(accountingManagement.findAll()).isNotEmpty();
	}

	@Test
	void reorderTest(){
		Optional<UserAccount> userAccount = this.userAccountManagement.findByUsername("HaKr10");

		orderController.reorder(cart, userAccount, model);
		assertThat(accountingManagement.findAll()).isNotEmpty();
	}

	@Test
	void rejectTest(){
		Optional<UserAccount> userAccount = this.userAccountManagement.findByUsername("HaKr10");

		orderController.reject(cart, userAccount, model);
		assertThat(accountingManagement.findAll()).isNotEmpty();
	}
	

	@Test
	@WithMockUser (roles = "BOSS")
	void listOpenOrdersTest() throws Exception {
		mvc.perform(get("/accounting-open-orders"))
				.andExpect(model().attributeExists("openOrders"));
	}
}