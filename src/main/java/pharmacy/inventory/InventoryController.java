package pharmacy.inventory;

import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pharmacy.accounting.AccountingController;
import pharmacy.catalog.Article;
import pharmacy.catalog.CatalogDataInitializer;
import pharmacy.catalog.PharmacyCatalog;
import pharmacy.catalog.Article.ArticleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


@Controller
@SessionAttributes("cart")
class InventoryController {

	private final PharmacyCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;



	private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);


	InventoryController(UniqueInventory<UniqueInventoryItem> inventory, PharmacyCatalog catalog) {
		this.inventory = inventory;
		this.catalog = catalog;
	}

	
	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

//------------------------------------- Redirects ----------------------------------------- 

	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/inventory-overview")
	String inventoryOverview(Model model) {

		model.addAttribute("catalog", inventory.findAll());

		return "inventory-overview";
	}


	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/inventory-restock")
	String restock() {

		return "inventory-restock";
	}


	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/inventory-reorder")
	String reorder() {

		return "inventory-reorder";
	}


	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/inventory-reject")
	String reject(Model model) {

		return "inventory-reject";
	}

	//------------------------------------- Expired ----------------------------------------- 
	
	/**
	 * Shows items that either have or will expire within the next 7 days
	 *
	 * @param model
	 * @param cart
	 * @return inventory-overview page
	 */
	
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	@GetMapping("/inventory-expiry")
	String expiry(Model model, Cart cart) {
		
		ArrayList<UniqueInventoryItem> products = new ArrayList<>();

		catalog.findAll().forEach(article -> {
			if (LocalDate.now().minusDays(7).isAfter(article.getExpiryDate()) )
				{products.add(inventory.findByProduct(article).get());}
			});
		model.addAttribute("catalog", products);

		return "inventory-expiry";
	}


//------------------------------------- Re-order ----------------------------------------- 

	/**
	 * Adding items to list for further actions (reorder, reject, restock)
	 *
	 * @param product
	 * @param cart
	 * @return inventory-overview page
	 */
	
	@PostMapping("/select-quantity")
	String select(@RequestParam("pid") Article product, @ModelAttribute Cart cart) {

		product.setPrice(product.getPurchasePrice());
		cart.addOrUpdateItem(product, Quantity.of(1));
		

		return "redirect:/inventory-overview";
	}

	/**
	 * Updating amount of items in temporary list for further actions
	 *
	 * @param id
	 * @param number
	 * @param cart
	 * @return inventory-reorder
	 */
	

	@PostMapping("/update-quantity/{id}")
	String update(@PathVariable String id, @RequestParam int number, @ModelAttribute Cart cart) {


		CartItem product = cart.getItem(id).get();
		cart.removeItem(id);
		cart.addOrUpdateItem(product.getProduct(), Quantity.of(number));

		return "inventory-reorder";
	}

//------------------------------------- Re-stock ----------------------------------------- 	

	/**
	 * Increasing the Quantity of chosen items in the Inventory
	 *
	 * @param id
	 * @param product
	 * @param number
	 * @param cart
	 * @param model
	 * @return inventory-restock page
	 */
	
	@PostMapping("/restock-add/{id}")
	String updateRestock(@PathVariable String id, @RequestParam("pid") Article product, @RequestParam Quantity number,
						 @ModelAttribute Cart cart, Model model) {

		UniqueInventoryItem item = inventory.findByProduct(product).get();
		cart.removeItem(id);
		item.increaseQuantity(number);
		inventory.save(item);

		return "redirect:/inventory-restock";


	}

//------------------------------------- Reject ----------------------------------------- 
	
	/**
	 * Removes items from stock by specified quantity
	 *
	 * @param id
	 * @param name
	 * @param number
	 * @param cart
	 * @return inventory-reject page
	 */
	
	@PostMapping("/update-reject/{id}")
	String updateReject(@PathVariable String id, @RequestParam("name") Article name, @RequestParam int number,
						@ModelAttribute Cart cart) {

		CartItem product = cart.getItem(id).get();
		cart.removeItem(id);
		UniqueInventoryItem item = inventory.findByProduct(name).get();

		if (Quantity.of(number).isGreaterThan(item.getQuantity())) {
			cart.addOrUpdateItem(product.getProduct(), item.getQuantity());
		} else {
			cart.addOrUpdateItem(product.getProduct(), Quantity.of(number));
		}
		;

		return "inventory-reject";
	}

	
//------------------------------------- Functions ----------------------------------------- 	

	/**
	 * Canceling the order
	 *
	 * @param cart
	 */
	
	@PostMapping("/cancel")
	String cancel(@ModelAttribute Cart cart) {

		cart.clear();

		return "redirect:/inventory-overview";
	}

}
