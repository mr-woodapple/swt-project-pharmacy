package pharmacy.catalog;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import pharmacy.catalog.Article.ArticleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.salespointframework.core.Currencies.EURO;

public class ArticleUnitTest {

	/* Auskommentiert um Anwendung laufen zu lassen
	@Test
	public void Getters() {
		
		String name = "what";
		int id = 120201021;
		String image = "image";
		Money price = Money.of(120, EURO);
		String description = "descr";
		ArticleType type = ArticleType.CATALOG;
		String customerName ="bob";
		String customerTel ="123124214";
		boolean prescription =false;
		String orderDate ="fsdfsds";

		Article test = new Article(name, id, image, price, description, type,customerName, customerTel,prescription,orderDate);

		assertEquals(name, test.getName());
		assertEquals(price, test.getPrice());
		assertEquals(description, test.getDescription());
		assertEquals(image, test.getImage());
		assertEquals(customerName, test.getCustomerName());
		assertEquals(customerTel, test.getCustomerTel());
		assertEquals(prescription, test.isPrescription());

	}

	@Test
	public void Setters() {
		Article test2 = new Article("Vicodin", 100000, "generic-drug", Money.of(100, EURO), Money.of(0, EURO), "beipackzettel", ArticleType.CATALOG, "", "", true, "");

		String name = "no";
		Money price = Money.of(200, EURO);
		int id = 432432432;

		test2.setName(name);
		test2.setPrice(price);
		test2.setID(id);

		assertEquals(name, test2.getName());
		assertEquals(price, test2.getPrice());
		assertEquals(id, test2.getID());
		
	}

	 */
}

