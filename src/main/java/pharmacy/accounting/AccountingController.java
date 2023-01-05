package pharmacy.accounting;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * The AccountingController handles the communication between Spring and the HTML providing the requested values.
 */
@Controller
public class AccountingController {

	private final AccountingManagement accountingManagement;

	public AccountingController(AccountingManagement accountingManagement) {

		Assert.notNull(accountingManagement, "AccountancyManagement must not be null!");
		this.accountingManagement = accountingManagement;

	}


	/**
	 * Setup the "accounting-overview" page, adding the model attributes and lists all existing orders. Returns
	 * the "accounting-overview" element as the view.
	 *
	 * @param model
	 * @return the view element for accounting-overview
	 */
	@GetMapping("accounting-overview")
	@PreAuthorize("hasRole('BOSS')")
	public String financialStatistics(Model model) {

		// Calculate totalBalance & totalIncome
		accountingManagement.totalIncome = accountingManagement.checkoutIncome
				.add(accountingManagement.selfCheckoutIncome)
				.add(accountingManagement.docCheckoutIncome);

		accountingManagement.totalBalance = accountingManagement.totalIncome
				.subtract(accountingManagement.totalExpenses);

		// Add the model views
		model.addAttribute("balance", accountingManagement.totalBalance);
		model.addAttribute("totalIncome", accountingManagement.totalIncome);
		model.addAttribute("totalExpenses", accountingManagement.totalExpenses);
		model.addAttribute("checkoutIncome", accountingManagement.checkoutIncome);
		model.addAttribute("selfCheckoutIncome", accountingManagement.selfCheckoutIncome);
		model.addAttribute("docCheckoutIncome", accountingManagement.docCheckoutIncome);

		model.addAttribute("orders", accountingManagement.findAll());

		return "accounting-overview";
	}

	/**
	 * Forwards the order detail from the "accounting-overview" page to the individual detail page when the user
	 * clicks on "View Details" on an accountancy entry.
	 *
	 * @param identifier unique identifier for each order
	 * @param orderValue the total amount of an order
	 * @param orderDate the date the order was created
	 * @param orderAccount the useraccount the order is linked to
	 * @param model
	 * @return the view for the detail page (for the specific order entry)
	 */
	@PostMapping("getOrderDetails")
	String getOrderDetail(@RequestParam OrderIdentifier identifier, @RequestParam String orderValue,
						  @RequestParam String orderDate, @RequestParam String orderAccount, Model model) {

		model.addAttribute("orderId", identifier);
		model.addAttribute("orderValue", orderValue);
		model.addAttribute("orderDate", orderDate);
		model.addAttribute("orderAccount", orderAccount);

		return "accounting-order-detail";
	}
}
