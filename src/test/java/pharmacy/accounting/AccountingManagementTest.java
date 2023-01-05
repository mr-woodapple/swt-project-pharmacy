package pharmacy.accounting;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.accountancy.Accountancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import javax.money.MonetaryAmount;

import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountingManagementTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	AccountingManagement accountingManagement;
	@Mock
	Model model;
	

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void updateCheckoutIncomeTest() throws Exception {

		MonetaryAmount checkoutIncome = Money.of(10.00, EURO);

		accountingManagement.updateCheckoutIncome(model, Money.of(19.99, EURO));
		Assertions.assertEquals(Money.of(10.00, EURO), checkoutIncome);
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void updateSelfCheckoutIncomeTest() throws Exception {

		MonetaryAmount selfCheckoutIncome = Money.of(10.00, EURO);

		accountingManagement.updateSelfCheckoutIncome(model, Money.of(19.99, EURO));
		Assertions.assertEquals(Money.of(10.00, EURO), selfCheckoutIncome);
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void updateDocCheckoutTest() throws Exception {

		MonetaryAmount docCheckoutIncome = Money.of(10.00, EURO);

		accountingManagement.updateDocCheckout(model, Money.of(19.99, EURO));
		Assertions.assertEquals(Money.of(10.00, EURO), docCheckoutIncome);
	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void updateExpensesTest() throws Exception {

		MonetaryAmount totalExpenses = Money.of(10.00, EURO);

		accountingManagement.updateExpenses(model, Money.of(19.99, EURO));
		Assertions.assertEquals(Money.of(10.00, EURO), totalExpenses);
	}
}
