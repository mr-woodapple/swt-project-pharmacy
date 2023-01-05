package pharmacy.checkout;


import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.*;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import pharmacy.accounting.AccountingManagement;
import java.util.Optional;


@Controller
@SessionAttributes("cart")
public class OrderController {

	private final OrderManagement<Order> orderManagement;
	private UniqueInventory<UniqueInventoryItem> inventory;
	private AccountingManagement accountingManagement;

	OrderController(OrderManagement<Order> orderManagement, UniqueInventory<UniqueInventoryItem> inventory,
					AccountingManagement accountingManagement) {

		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		this.orderManagement = orderManagement;
		this.inventory = inventory;
		this.accountingManagement = accountingManagement;

	}

	/**
	 * checks current state of {@link Cart}
	 * Buy the products from the cart
	 *
	 * @param cart
	 * @param userAccount
	 * @param model
	 * @return
	 */
	@PostMapping("/checkout")
	String buyProducts(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Model model) {
		return userAccount.map(account -> {

			var order = new Order(account, Cash.CASH);

			cart.addItemsTo(order);
			orderManagement.payOrder(order); // sets the order status to "paid"
			orderManagement.completeOrder(order); // sets the order status to "completed"

			/* Update the balance on "finances-overview", depending on the role the system updates the self-checkout
			   or the checkout statistics */
			if (account.hasRole(Role.of("BOSS")) || account.hasRole(Role.of("EMPLOYEE"))) {
				accountingManagement.updateCheckoutIncome(model, order.getTotal());
			} else {
				accountingManagement.updateSelfCheckoutIncome(model, order.getTotal());
			}

			cart.clear(); // When the order has been completed, the shopping cart will be emptied

			return "redirect:/";
		}).orElse("redirect:/cart");
	}

	/**
	 * Checkout for the docs
	 * Checks current state of {@link Cart}
	 * Buy the products from the cart
	 *
	 * @param cart
	 * @param userAccount
	 * @param model
	 * @return
	 */
	@PostMapping("/docCheckout")
	String buyDocProducts(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Model model) {
		return userAccount.map(account -> {

			var order = new Order(account, Cash.CASH);

			cart.addItemsTo(order);
			orderManagement.save(order); // the order is not paid/completed for the moment
			cart.clear();

			return "redirect:/";
		}).orElse("redirect:/cart");
	}

	/**
	 * @param identifier
	 * @return
	 */
	@PostMapping("markAsPayed")
	@PreAuthorize("hasRole('BOSS')")
	public String markAsPayed(@RequestParam OrderIdentifier identifier, Model model) {

		Order order = orderManagement.get(identifier).get();

		orderManagement.payOrder(order);
		orderManagement.completeOrder(order);

		accountingManagement.updateDocCheckout(model, order.getTotal());

		return "redirect:/accounting-overview";
	}


	/**Generating invoice for reorders 
	 * 
	 * @param cart
	 * @param userAccount
	 * @param model
	 * @return
	 */
	@PostMapping("/reorder")
	String reorder(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Model model) {
		return userAccount.map(account -> {


			var reorder = new Order(account, Cash.CASH);

			cart.addItemsTo(reorder);
			orderManagement.payOrder(reorder);

			accountingManagement.updateExpenses(model, reorder.getTotal());

			cart.clear();

			return "redirect:/inventory-overview";
		}).orElse("redirect:/inventory-overview");
	}


	/**Generating invoice for rejects (not working as intended)
	 * 
	 * @param cart
	 * @param userAccount
	 * @param model
	 * @return
	 */
	@PostMapping("/reject")
	String reject(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Model model) {
		return userAccount.map(account -> {

			var reject = new Order(account, Cash.CASH);

			cart.addItemsTo(reject);

			orderManagement.payOrder(reject);
			orderManagement.completeOrder(reject);

			cart.clear();

			return "redirect:/inventory-overview";
		}).orElse("redirect:/inventory-overview");
	}


	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/orders")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String orders(Model model) {

		String bool = "bool";
		model.addAttribute("ordersCompleted", orderManagement.findBy(OrderStatus.valueOf(bool)));

		return "orders";
	}


	/**
	 * @param model
	 * @return accounting-open-orders
	 */
	@GetMapping("accounting-open-orders")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	public String listOpenOrders(Model model) {

		model.addAttribute("openOrders", orderManagement.findBy(OrderStatus.OPEN));

		return "accounting-open-orders";
	}
}
