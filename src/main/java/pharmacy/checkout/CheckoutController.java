package pharmacy.checkout;


import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.*;
import org.salespointframework.quantity.Quantity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pharmacy.catalog.Article;
import pharmacy.inventory.*;


@Controller
@SessionAttributes("cart")
public
class CheckoutController {

	private UniqueInventory<UniqueInventoryItem> inventory;


	/**
	 * creates a new {@link CheckoutController}
	 *
	 * @param inventory
	 */
	CheckoutController(UniqueInventory<UniqueInventoryItem> inventory) {
		this.inventory = inventory;
	}

	/**
	 * creates a new {@link Cart}
	 * Initializes the cart
	 *
	 * @return a new {@link Cart}
	 */
	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	/**
	 * add {@link Article} to {@link Cart}
	 * Generate order
	 *
	 * @param product
	 * @param cart
	 * @return
	 */
	@PostMapping("/order")
	public String addOrder(@RequestParam("pid") Article product, @ModelAttribute Cart cart) {

		cart.addOrUpdateItem(product, Quantity.of(1));

		return "redirect:view-order";
	}

	/**
	 * add {@link Article} to {@link Cart}
	 * Add a product to cart
	 *
	 * @param product
	 * @param cart
	 * @param model
	 * @return cart
	 */
	@PostMapping("/cart")
	String addProduct(@RequestParam("pid") Article product, @ModelAttribute Cart cart, Model model) {

		UniqueInventoryItem item = inventory.findByProduct(product).get();

		if (item.getQuantity().isLessThan(Quantity.of(1))) {
			model.addAttribute("message", "No more Items available");
		} else {
			cart.addOrUpdateItem(product, Quantity.of(1));
		}

		return "redirect:cart";
	}

	/**
	 * Update item quantity
	 *
	 * @param id
	 * @param name
	 * @param number
	 * @param cart
	 * @return cart
	 */
	@PostMapping("/cart/{id}/updatecart")
	String updateProduct(@PathVariable String id, @RequestParam("name") Article name, @RequestParam int number,
						 @ModelAttribute Cart cart) {

		CartItem product = cart.getItem(id).get();
		cart.removeItem(id);
		UniqueInventoryItem item = inventory.findByProduct(name).get();

		if (Quantity.of(number).isGreaterThan(item.getQuantity())) {
			cart.addOrUpdateItem(product.getProduct(), item.getQuantity());
		} else {
			cart.addOrUpdateItem(product.getProduct(), Quantity.of(number));
		}

		return "redirect:/cart";
	}

	/**
	 * @return cart
	 */
	@GetMapping("/cart")
	String basket() {
		return "cart";
	}

	/**
	 * remove all products from cart
	 *
	 * @param cart
	 * @return
	 */
	@PostMapping("/delete")
	String removeProducts(Cart cart) {
		cart.clear();
		return "redirect:/";
	}

	/**
	 * remove selected product from cart
	 *
	 * @param pid
	 * @param cart
	 * @return cart
	 */
	@PostMapping("/remove")
	String removeOneProduct(@RequestParam String pid, @ModelAttribute Cart cart) {
		cart.removeItem(pid);
		return "redirect:/cart";
	}

	/**
	 * Where we actually want to generate and print the receipt
	 *
	 * @param cart
	 * @param model
	 * @return accounting-receipt
	 */
	@PostMapping("/print")
	String printReceipt(@ModelAttribute Cart cart, Model model) {

		model.addAttribute("cart", cart);

		return "accounting-receipt";
	}
}
