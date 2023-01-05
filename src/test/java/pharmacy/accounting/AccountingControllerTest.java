package pharmacy.accounting;

import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.money.MonetaryAmount;


@SpringBootTest
@AutoConfigureMockMvc
public class AccountingControllerTest {

	@Autowired
	MockMvc mvc;
	@Autowired AccountingController accountingController;


	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void AccessToAccountingOverview() throws Exception {
		mvc.perform(get("/accounting-overview"));
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void AccessToAccountingOpenOrders() throws Exception {
		mvc.perform(get("/accounting-open-orders"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void financialStatisticsTest() throws Exception {

		mvc.perform(get("/accounting-overview"))
				.andExpect(model().attributeExists("balance"))
				.andExpect(model().attributeExists("totalIncome"))
				.andExpect(model().attributeExists("totalExpenses"))
				.andExpect(model().attributeExists("checkoutIncome"))
				.andExpect(model().attributeExists("selfCheckoutIncome"))
				.andExpect(model().attributeExists("docCheckoutIncome"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void getOrderDetailTest() throws Exception {

		String identifier = "";
		String orderDate = "";
		String orderValue = "";
		String orderAccount = "";

		mvc.perform(post("/getOrderDetails")
						.param("identifier", identifier)
						.param("orderDate", orderDate)
						.param("orderValue", orderValue)
						.param("orderAccount", orderAccount))
				.andExpect(model().attributeExists("orderId"))
				.andExpect(model().attributeExists("orderValue"))
				.andExpect(model().attributeExists("orderDate"))
				.andExpect(model().attributeExists("orderAccount"));
	}
}
