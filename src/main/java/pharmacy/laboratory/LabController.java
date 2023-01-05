package pharmacy.laboratory;

import pharmacy.catalog.Article;
import pharmacy.catalog.PharmacyCatalog;
import pharmacy.catalog.Article.ArticleType;

import static org.salespointframework.core.Currencies.EURO;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.javamoney.moneta.Money;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Spring MVC Controller responsible for the Laboratory.
 */
@Controller
public class LabController {

	private static final Quantity NONE = Quantity.of(0);
	private final PharmacyCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;
	//An array list for mixture components.
	public List<String> mixtureIngredients = new ArrayList<String>();


	LabController(PharmacyCatalog catalog, UniqueInventory<UniqueInventoryItem> inventory,
				  ArrayList<String> mixtureIngredients) {
		this.catalog = catalog;
		this.inventory = inventory;
		this.mixtureIngredients = mixtureIngredients;
		initialIngredients();

	}

	//Returns the html-page, where the data for a new mixture order can be filled.
	@GetMapping("/add-mixture-components")
	String newMixture() {
		return "add-mixture-components";
	}


	// Initial possible ingredients shown for Component Mixture order.
	public void initialIngredients() {
		mixtureIngredients.add("Component 1");
		mixtureIngredients.add("Component 2");
		mixtureIngredients.add("Component 3");
		mixtureIngredients.add("Component 4");
		mixtureIngredients.add("Component 5");
		mixtureIngredients.add("Component 6");
		mixtureIngredients.add("Component 7");
		mixtureIngredients.add("Component 8");
		mixtureIngredients.add("Component 9");
		mixtureIngredients.add("Component 10");
		mixtureIngredients.add("Component 11");
		mixtureIngredients.add("Component 12");
	}

	// List that represents small "DataInitializer" of existing in Lab mixture components.
	@ModelAttribute("mixtureIngredients")
	public List<String> getMixtureIngredients() {

		return mixtureIngredients;
	}

	// Represents orders with status "in preparation".
	@GetMapping("/lab-orders")
	String labOrders(Model model) {
		model.addAttribute("catalog", catalog.findByType(ArticleType.INPREPARATION));
		return "lab-orders";
	}

	//Represents a possibility to view an order slip of order "in preparation".
	@GetMapping("/lab-orders/{article}")
	String labOrderSlip(@PathVariable Article article, Model model) {

		var quantity =
				inventory.findByProductIdentifier(article.getId()).map(InventoryItem::getQuantity).orElse(NONE);

		model.addAttribute("article", article);
		model.addAttribute("name", article.getName());
		model.addAttribute("quantity", quantity);
		model.addAttribute("description", article.getDescription());
		model.addAttribute("customerName", article.getCustomerName());
		model.addAttribute("customerTel", article.getCustomerTel());
		model.addAttribute("id", article.getID());
		model.addAttribute("orderDate", article.getOrderDate());

		return "lab-order-slip";
	}


	/**
	 * represents all product orders
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/product-orders")
	String productOrders(Model model) {
		model.addAttribute("catalog", catalog.findByType(ArticleType.ORDER));
		return "product-orders";
	}

	//Represents all laboratory orders already marked as "ready"(completed) by labor worker.
	@GetMapping("/lab-ready-orders")
	String labReadyOrders(Model model) {
		model.addAttribute("catalog", catalog.findByType(ArticleType.READYORDERS));
		return "lab-ready-orders";
	}

	//Returns the html-page, where the data for a new laboratory order can be filled.
	@GetMapping("/lab-new-order")
	String newLabOrder(Model model) {
		return "lab-new-order";
	}

	/**
	 * Adds a new product Order to the system
	 *
	 * @param name
	 * @param customerName
	 * @param customerTel
	 * @param description
	 * @param model
	 * @return
	 */
	@PostMapping("/addProductOrder")
	String addProductOrder(@RequestParam("name") String name, @RequestParam("customerName") String customerName,
						   @RequestParam("customerTel") String customerTel,
						   @RequestParam("description") String description, Model model) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Article item;
		item = new Article(name, generateID(), "Custom sized", LocalDate.of(2022, 1,
				20), "image", Money.of(0, EURO), Money.of(0, EURO), description,
				ArticleType.ORDER, customerName, customerTel, true,
				"ordered on " + dateFormat.format(cal.getTime()));
		catalog.save(item);
		inventory.save(new UniqueInventoryItem(item, Quantity.of(Integer.valueOf(1))));
		return "redirect:/product-orders";
	}

	/**
	 * Sets a price in product-/lab-order
	 *
	 * @param article
	 * @param price
	 * @return
	 */
	@PostMapping("/orders/{article}/updateprice")
	String setOrderPrice(@PathVariable Article article, @RequestParam("price") Double price) {
		article.setPrice(Money.of(price, EURO));
		this.catalog.save(article);
		switch (article.getType()) {
			case ORDER:
				return "redirect:/product-orders";
			case INPREPARATION:
			default:
				return "redirect:/lab-orders";
		}
	}

	//Marks laboratory order as completed("ready").
	@PostMapping("/lab-orders/{article}/ready")
	String readyOrder(@PathVariable Article article) {
		article.setType(ArticleType.READYORDERS);
		this.catalog.save(article);

		return "redirect:/lab-orders";
	}

	//Adds a new laboratory order to the system.
	@PostMapping("/addLabOrder")
	String addLabOrder(@RequestParam("name") String name, @RequestParam("customerName") String customerName,
					   @RequestParam("customerTel") String customerTel, @RequestParam("description") String description,
					   Model model) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Article item = new Article(name, generateID(), "Custom sized", LocalDate.of(2022, 1,
				20), "image", Money.of(0, EURO), Money.of(0, EURO), description,
				ArticleType.INPREPARATION, customerName, customerTel, true,
				"ordered on " + dateFormat.format(cal.getTime()));
		catalog.save(item);
		inventory.save(new UniqueInventoryItem(item, Quantity.of(Integer.valueOf(1))));
		return "redirect:/lab-orders";
	}

	//Adds a new mixture order to the system.
	@PostMapping("/addMixtureOrder")
	String addMixtureOrder(@RequestParam("name") String name, @RequestParam("description") String description,
						   @RequestParam("customerName") String customerName,
						   @RequestParam("customerTel") String customerTel, Model model) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Article item = new Article(name + " Mixture", generateID(), "Custom sized",
				LocalDate.of(2022, 1, 20), "image", Money.of(0, EURO),
				Money.of(0, EURO), description, ArticleType.INPREPARATION, customerName, customerTel,
				true, "ordered on " + dateFormat.format(cal.getTime()));//);
		catalog.save(item);
		inventory.save(new UniqueInventoryItem(item, Quantity.of(Integer.valueOf(1))));

		return "redirect:/lab-orders";
	}

	/**
	 * Adds a new Component to choose from by mixture order.
	 *
	 * @param component
	 * @return
	 */
	@PostMapping("/addComponent")
	String addComponent(@RequestParam("component") String component, Model model) {
		updateMixtureComponents(model, component);
		return "add-mixture-components";
	}

	public void updateMixtureComponents(Model model, String component) {
		if (mixtureIngredients.contains(component)) {
			System.out.print("Component already exists");
		} else {
			mixtureIngredients.add(component);
			model.addAttribute("component", component);
		}
	}

	/**
	 * Function returns random-generated ID, that doesn't exist in inventory.
	 *
	 * @return
	 */
	public int generateID() {
		Random randomGenerator = new Random();
		int generatedID = randomGenerator.nextInt(1000000);
		boolean exists = catalog.existsArticleByID(generatedID);
		while (exists) {
			generatedID = randomGenerator.nextInt(1000000);
			exists = catalog.existsArticleByID(generatedID);
		}
		return generatedID;
	}

}
