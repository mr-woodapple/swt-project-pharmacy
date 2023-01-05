package pharmacy.catalog;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import pharmacy.laboratory.LabController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CatalogControllerTest {
	@Autowired
	MockMvc mvc;
	@Autowired
	CatalogController catalogController;

	@Mock
	PharmacyCatalog pharmacyCatalog;

	@MockBean
	UniqueInventory<UniqueInventoryItem> uniqueInventory;

	@Mock
	Cart cart;

	@Mock
	Model model;

	@Test
	@WithMockUser(roles = "DOC")
	void docCatalogTest() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		Article article1 = new Article("Name", 4567890, "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", Article.ArticleType.INPREPARATION, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));
		this.pharmacyCatalog.save(article1);
		this.uniqueInventory.save(new UniqueInventoryItem(article1, Quantity.of(Integer.valueOf(1))));
		catalogController.docCatalog(article1,model);
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void prescriptionCatalogTest()  {

	}


	@Test
	void prescriptionFreeCatalog() {

	}

	@Test
	@WithMockUser(roles = "BOSS")
	void orderTest() throws Exception{
		mvc.perform(get("/order")).
				andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void searchArticleTest()  {

	}

	@Test
	void searchInventory() {
	}

	@Test
	void searchOrder() {
	}

	@Test
	void searchCheckout() {
	}

	@Test
	void searchLab() {
	}

	@Test
	void searchRdy() {
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void newArticle() throws Exception {
		mvc.perform(get("/newArticle")).
				andExpect(status().isOk());
	}

//	@Test
//	@WithMockUser(roles = "BOSS")
//	void editArticle() throws Exception {
//		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		Calendar cal = Calendar.getInstance();
//
//		Article article1 = new Article("Name", 4567890, "Custom sized", LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO), Money.of(0, EURO), "description", Article.ArticleType.CATALOG, "customerName", "customerTel", true, "ordered on " + dateFormat.format(cal.getTime()));
//		this.pharmacyCatalog.save(article1);
//		this.uniqueInventory.save(new UniqueInventoryItem(article1, Quantity.of(Integer.valueOf(1))));
//		String articleString ="";
//		if (uniqueInventory.findByProduct(article1).isPresent()) {
//			String article = uniqueInventory.findByProduct(article1).get().toString();
//			articleString = article;
//		}
//
//		mvc.perform(get("/editArticle/{article}",article1.getId())
//						.param("article",articleString)
//						.param("retailPrice", String.valueOf(article1.getPrice()))
//						.param("purchasePrice",String.valueOf(article1.getPurchasePrice())))
//						.andExpect(status().isOk());
//
//	}

	@Test
	void removeArticle() {
	}

	@Test
	void detail() {
	}

	@Test
	void editStock() {
	}

	@Test
	void ifIdExists() {
	}

	@Test
	void searchFunc() throws Exception {

	}
}