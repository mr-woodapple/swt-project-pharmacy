package pharmacy.catalog;


import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.*;
import pharmacy.catalog.Article.ArticleType;
import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.EURO;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;
import java.util.ArrayList;

import javax.money.NumberValue;

@Controller
public class CatalogController {

	private static final Quantity NONE = Quantity.of(0);

	private final PharmacyCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final BusinessTime businessTime;


	CatalogController(PharmacyCatalog catalog, UniqueInventory<UniqueInventoryItem> inventory, BusinessTime businessTime) {

		this.catalog = catalog;
		this.inventory = inventory;
		this.businessTime = businessTime;
	}

//------------------------------------- Redirects ----------------------------------------- 

	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/product-overview")
	String stock(Model model) {

		model.addAttribute("catalog", inventory.findAll());

		return "product-overview";
	}


	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE', 'DOC')")
	@GetMapping("/doc-catalog")
	String populateDocCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(ArticleType.CATALOG));

		return "doc-catalog";
	}


	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/catalog")
	String populateCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(ArticleType.CATALOG));

		return "catalog";
	}


	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE', 'DOC')")
	@GetMapping("/doc-catalog/{article}")
	String docCatalog(@PathVariable Article article, Model model) {

		var findID = inventory.findByProduct(article);
		model.addAttribute("findID", findID);

		return "doc-catalog";
	}


	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE', 'CUSTOMER')")
	@GetMapping("/customer-catalog")
	String populateCustomerCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByTypeAndPrescription(ArticleType.CATALOG, false));

		return "catalog";
	}

//------------------------------------- Search bar redirects ----------------------------------------- 

	//Prescription only filter
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/prescription/{num}")
	String prescriptionCatalog(Model model, @PathVariable int num) {

		filter(model, 1);
		if (num == 1) {
			return "inventory-overview";
		} else {
			return "product-overview";
		}

	}

	//Prescription-free only filter
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/prescription-free/{num}")
	String prescriptionFreeCatalog(Model model, @PathVariable int num) {

		filter(model, 2);
		if (num == 1) {
			return "inventory-overview";
		} else {
			return "product-overview";
		}
	}

	//Orders only filter
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/order")
	String order(Model model) {

		filter(model, 3);


		return "product-overview";
	}

	//Search-bar catalog
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/search-article")
	String searchArticle(@RequestParam("search") String search, Model model) {

		searchFuncInv(model, search);


		return "product-overview";
	}

	//Search-bar inventory
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/search-inventory")
	String searchInventory(@RequestParam("search") String search, Model model) {

		searchFuncInv(model, search);


		return "inventory-overview";
	}

	//Search-bar orders
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/search-orders")
	String searchOrder(@RequestParam("search") String search, Model model) {

		int type = 1;
		searchFunc(model, search, type);


		return "product-orders";
	}

	//Search-bar checkout
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/search-checkout")
	String searchCheckout(@RequestParam("search") String search, Model model) {

		int type = 2;
		searchFunc(model, search, type);


		return "catalog";
	}

	//Search-bar lab-orders
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE', 'LABORANT')")
	@GetMapping("/search-lab-orders")
	String searchLab(@RequestParam("search") String search, Model model) {

		int type = 3;
		searchFunc(model, search, type);


		return "lab-orders";
	}

	//Search-bar lab-ready-orders
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE', 'LABORANT')")
	@GetMapping("/search-lab-ready-orders")
	String searchRdy(@RequestParam("search") String search, Model model) {

		int type = 4;
		searchFunc(model, search, type);

		return "lab-ready-orders";
	}

//------------------------------------- Product-overview ----------------------------------------- 

	//Creating new article
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/newArticle")
	String newArticle(Model model) {


		return "add-article";
	}

	//Edit existing article	
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/editArticle/{article}")
	String editArticle(@PathVariable Article article, Model model) {

		NumberValue retail = article.getPrice().getNumber();
		NumberValue purchase = article.getPurchasePrice().getNumber();
		double retailPrice = retail.doubleValue();
		double purchasePrice = purchase.doubleValue();
		model.addAttribute("article", inventory.findByProduct(article).get());
		model.addAttribute("retailPrice", retailPrice);
		model.addAttribute("purchasePrice", purchasePrice);

		return "edit-article";
	}

	//Delete article
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/removeArticle/{article}")
	String removeArticle(@PathVariable Article article, Model model) {

		inventory.delete(inventory.findByProduct(article).get());
		inventory.findByProduct(article);
		catalog.delete(article);

		return "redirect:/product-overview";
	}

	//Show article description
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/article/{article}")
	String detail(@PathVariable Article article, Model model) {

		var quantity = inventory.findByProductIdentifier(article.getId()).map(InventoryItem::getQuantity).orElse(NONE);

		model.addAttribute("article", article);
		model.addAttribute("quantity", quantity);

		return "article-details";
	}

	/**
	 * Creates a new UniqueInventoryItem entry & redirects to product-overview once required parameter are provided
	 *
	 * @param name
	 * @param quantity
	 * @param id
	 * @param price
	 * @param purchasePrice
	 * @param description
	 * @param prescription
	 * @param packSize
	 * @param expiryDate
	 * @param model
	 * @return product-overview page
	 */
	
	@PostMapping("/addStock")
	String addStock(@RequestParam("name") String name,
					@RequestParam("quantity") Integer quantity,
					@RequestParam("id") Integer id,
					@RequestParam("price") Double price,
					@RequestParam("purchasePrice") Double purchasePrice,
					@RequestParam("description") String description,
					@RequestParam("prescription") Boolean prescription,
					@RequestParam("packSize") String packSize,
					/*@RequestParam("expiryDate") LocalDate expiryDate,*/
					Model model) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Article item = new Article(name,
				ifIdExists(id),
				packSize,
				//problems with expiry Dates, wip 
				LocalDate.of(2022, 1, 19),
				"image",
				Money.of(price, EURO),
				Money.of(purchasePrice, EURO),
				description,
				ArticleType.CATALOG,
				"", "",
				prescription,
				"added on " + dateFormat.format(cal.getTime()));

		catalog.save(item);
		inventory.save(new UniqueInventoryItem(item, Quantity.of(Integer.valueOf(quantity))));
		return "redirect:/product-overview";
	}
	
	/**
	 * Updates the selected UniqueInventoryItem entry & redirects to product-overview once required parameter are provided
	 *
	 * @param name
	 * @param quantity
	 * @param id
	 * @param price
	 * @param purchasePrice
	 * @param description
	 * @param prescription
	 * @param packSize
	 * @param model
	 * @return product-overview page
	 */

	@PostMapping("/editStock")
	String editStock(@RequestParam("pid") Article article,
					 @RequestParam("name") String name,
					 @RequestParam("quantity") Integer quantity,
					 @RequestParam("id") Integer id,
					 @RequestParam("price") Double price,
					 @RequestParam("purchasePrice") Double purchasePrice,
					 @RequestParam("description") String description,
					 @RequestParam("prescription") Boolean prescription,
					 @RequestParam("packSize") String packSize,
			/*@RequestParam("expiryDate") LocalDate expiryDate*/
					 Model model) {

		inventory.delete(inventory.findByProduct(article).get());
		catalog.delete(article);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Article item = new Article(name,
				ifIdExists(id),
				packSize,
				LocalDate.of(2022, 1, 19),
				"image",
				Money.of(price, EURO),
				Money.of(purchasePrice, EURO),
				description,
				ArticleType.CATALOG,
				"", "",
				prescription,
				"added on " + dateFormat.format(cal.getTime()));
		catalog.save(item);
		inventory.save(new UniqueInventoryItem(item, Quantity.of(Integer.valueOf(quantity))));
		return "redirect:/product-overview";
	}


//------------------------------------- Functions ----------------------------------------- 


	//Function checks if input ID already exists: 1)if exists -> new random not existing ID generation;
	//											  2)if not    -> input value remains.
	public int ifIdExists(int id) {
		boolean exists = catalog.existsArticleByID(id);
		while (exists) {
			Random randomGenerator = new Random();
			int newGeneratedID = randomGenerator.nextInt(1000000);
			id = newGeneratedID;
			exists = catalog.existsArticleByID(id);
		}
		return id;
	}

	/**
	 * Filtering search results according to product type
	 *
	 * @param type
	 * @param model
	 * @return search results
	 */
	
	private Model filter(Model model, int type) {

		ArrayList<UniqueInventoryItem> products = new ArrayList<>();

		if (type == 1) {
			catalog.findByTypeAndPrescription(ArticleType.CATALOG, true).forEach(article -> {
				products.add(inventory.findByProduct(article).get());
			});
			model.addAttribute("catalog", products);
		} else if (type == 2) {
			catalog.findByTypeAndPrescription(ArticleType.CATALOG, false).forEach(article -> {
				products.add(inventory.findByProduct(article).get());
			});
			model.addAttribute("catalog", products);
		} else if (type == 3) {
			catalog.findByType(ArticleType.ORDER).forEach(article -> {
				products.add(inventory.findByProduct(article).get());
			});
			model.addAttribute("catalog", products);
		}
		return model;

	}

	/**
	 * Search bar function for the inventory (for quantity purposes), filtering out any items containing key words or/and letters
	 *
	 * @param model
	 * @param search
	 * @return search results
	 */

	private Model searchFuncInv(Model model, String search) {

		ArrayList<UniqueInventoryItem> products = new ArrayList<>();
		boolean results = false;

		if (search.equals("")) {
			model.addAttribute("catalog", inventory.findAll());
		} else {
			catalog.findAll().forEach(article -> {
				if (article.getName().toLowerCase().contains(search.toLowerCase())) {
					products.add(inventory.findByProduct(article).get());
				}
			});
			results = products.isEmpty();
			model.addAttribute("catalog", products);

		}
		model.addAttribute("noresult", results);
		return model;
	}

	/**
	 * Search bar function for any other catalog pages (including product-overview, orders, checkout, lab)
	 * filtering out any items containing key words or/and letters
	 *
	 * @param model
	 * @param search
	 * @param type
	 * @return search results
	 */
	
	private Model searchFunc(Model model, String search, int type) {

		ArrayList<Article> products = new ArrayList<>();
		boolean results = false;

		switch (type) {
		case 1:
			catalog.findByType(ArticleType.ORDER).forEach(article -> {
				if (article.getName().toLowerCase().contains(search.toLowerCase())) {
					products.add(article);
				}
			});
			results = products.isEmpty();
			model.addAttribute("catalog", products);
			break;

		case 2:
			catalog.findByType(ArticleType.CATALOG).forEach(article -> {
				if (article.getName().toLowerCase().contains(search.toLowerCase())) {
					products.add(article);
				}
			});
			results = products.isEmpty();
			model.addAttribute("catalog", products);
			break;

		case 3:
			catalog.findByType(ArticleType.INPREPARATION).forEach(article -> {
				if (article.getName().toLowerCase().contains(search.toLowerCase())) {
					products.add(article);
				}
			});
			results = products.isEmpty();
			model.addAttribute("catalog", products);
			break;

		default:
			catalog.findByType(ArticleType.READYORDERS).forEach(article -> {
				if (article.getName().toLowerCase().contains(search.toLowerCase())) {
					products.add(article);
				}
			});
			results = products.isEmpty();
			model.addAttribute("catalog", products);
			break;

		}
		model.addAttribute("noresult", results);
		return model;
	}

}