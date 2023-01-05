package pharmacy.laboratory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.salespointframework.core.Currencies.EURO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pharmacy.catalog.Article;
import pharmacy.catalog.PharmacyCatalog;
import pharmacy.catalog.Article.ArticleType;

import org.javamoney.moneta.Money;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes = {LabController.class})
@ExtendWith(SpringExtension.class)
class LabControllerTest {
	@Autowired
	MockMvc mvc;
	@Autowired
	LabController labController;
	@Mock
	Model model;

	@MockBean
	private PharmacyCatalog pharmacyCatalog;

	@MockBean
	private UniqueInventory<UniqueInventoryItem> uniqueInventory;


	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE,LABORANT")
	public void labOrderSlipTest() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		Article article1 = new Article("Name", this.labController.generateID(), "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", ArticleType.INPREPARATION, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));
		this.pharmacyCatalog.save(article1);
		this.uniqueInventory.save(new UniqueInventoryItem(article1, Quantity.of(Integer.valueOf(1))));
		labController.labOrderSlip(article1, model);
		assertEquals("Name", article1.getName());

	}

	@Test
	void productOrders() throws Exception {
		when(this.pharmacyCatalog.findByType((Article.ArticleType) any())).thenReturn((Iterable<Article>) mock(Iterable.class));
		SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.labController).build().perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
	}


	@Test
	void addProductOrder() {
		String name = "Name";
		String customerName = "Anybody";
		String customerTel = "017535829273";
		String description = "Nothing";
		labController.addProductOrder(name, customerName, customerTel, description, model);

	}

	@Test
	@WithMockUser(roles = "BOSS,LABORANT,EMPLOYEE")
	void setOrderPrice() {
		double price1 = 15.0;
		double price2 = 20.0;

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		Article article1 = new Article("Name", this.labController.generateID(), "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", ArticleType.INPREPARATION, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));
		Article article2 = new Article("Name2", this.labController.generateID(), "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", ArticleType.ORDER, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));

		this.pharmacyCatalog.save(article1);
		this.uniqueInventory.save(new UniqueInventoryItem(article1, Quantity.of(Integer.valueOf(1))));
		labController.setOrderPrice(article1, price1);
		assertEquals(15.0, price1);

		this.pharmacyCatalog.save(article2);
		this.uniqueInventory.save(new UniqueInventoryItem(article2, Quantity.of(Integer.valueOf(1))));
		labController.setOrderPrice(article2, price2);
		assertEquals(20.0, price2);

	}

	@Test
	void readyOrder() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Article article1 = new Article("Name", this.labController.generateID(), "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", ArticleType.INPREPARATION, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));
		labController.readyOrder(article1);
		assertEquals(ArticleType.READYORDERS, article1.getType());

		Article article2 = new Article("Name2", 0, "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", ArticleType.INPREPARATION, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));
		assertEquals(ArticleType.INPREPARATION, article2.getType());

	}


	@Test
	void addLabOrder() {
		String name = "Name";
		String customerName = "Anybody";
		String customerTel = "017535829273";
		String description = "Nothing";
		labController.addLabOrder(name, customerName, customerTel, description, model);

	}

	@Test
	@WithMockUser(roles = "BOSS,EMPLOYEE")
	void addComponentTest() throws Exception {
		String component = "Component 15";
		labController.addComponent(component, model);

	}

	@Test
	void addMixtureOrder() {
		String name = "Name";
		String customerName = "Anybody";
		String customerTel = "017535829273";
		String description = "Nothing";
		labController.addMixtureOrder(name, customerName, customerTel, description, model);

	}


	@Test
	@WithMockUser(roles = "BOSS,LABORANT,EMPLOYEE")
	void updateMixtureComponents(){
		String component1 = "Component 13";
		String component2 = "Component 5";
		List<String> expectedList = new ArrayList<String>(Arrays.asList("Component 1", "Component 2", "Component 3", "Component 4", "Component 5", "Component 6", "Component 7", "Component 8", "Component 9", "Component 10", "Component 11", "Component 12", "Component 13"));

		labController.updateMixtureComponents(model, component1);
		assertEquals(expectedList, labController.mixtureIngredients);

		labController.updateMixtureComponents(model, component2);
		assertEquals(expectedList, labController.mixtureIngredients);

	}
}
