package pharmacy.accounting;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.money.MonetaryAmount;

import static org.salespointframework.core.Currencies.EURO;


/**
 * AccountingManagement provides the required methods to update the financial statistics (which are called from
 * methods in OrderController).
 */
@Service
public class AccountingManagement {

	// Variables for the totalBalance, initial setup
	MonetaryAmount totalBalance = Money.of(0.00, EURO);
	MonetaryAmount totalIncome = Money.of(0.00, EURO);
	MonetaryAmount totalExpenses = Money.of(10.00, EURO);
	MonetaryAmount checkoutIncome = Money.of(510.87, EURO);
	MonetaryAmount selfCheckoutIncome = Money.of(24.30, EURO);
	MonetaryAmount docCheckoutIncome = Money.of(234.87, EURO);

	private final Accountancy pharmacyAccountancy;

	AccountingManagement(Accountancy pharmacyAccountancy) {
		this.pharmacyAccountancy = pharmacyAccountancy;
	}


	/**
	 * Updates the checkoutIncome value for the statistics on "accounting-overview".
	 *
	 * @param model
	 * @param amount is the total of the order from which this method was called
	 */
	public void updateCheckoutIncome(Model model, MonetaryAmount amount) {
		checkoutIncome = checkoutIncome.add(amount);
		model.addAttribute("checkoutIncome", checkoutIncome);
	}


	/**
	 * Updates the selfCheckoutIncome value for the statistics on "accounting-overview".
	 *
	 * @param model
	 * @param amount is the total of the order from which this method was called
	 */
	public void updateSelfCheckoutIncome(Model model, MonetaryAmount amount) {
		selfCheckoutIncome = selfCheckoutIncome.add(amount);
		model.addAttribute("selfCheckoutIncome", selfCheckoutIncome);
	}


	/**
	 * Updates the docCheckoutIncome value for the statistics on "accounting-overview".
	 *
	 * @param model
	 * @param amount is the total of the order from which this method was called
	 */
	public void updateDocCheckout(Model model, MonetaryAmount amount) {
		docCheckoutIncome = docCheckoutIncome.add(amount);
		model.addAttribute("totalExpenses", docCheckoutIncome);
	}


	/**
	 * Updates the totalExpenses value for the statistics on "accounting-overview".
	 *
	 * @param model
	 * @param amount is the total of the order from which this method was called
	 */
	public void updateExpenses(Model model, MonetaryAmount amount) {
		totalExpenses = totalExpenses.add(amount);
		model.addAttribute("totalExpenses", totalExpenses);
	}


	/**
	 * Method to get all pharmacyAccountancy entries.
	 *
	 * @return all entries available in pharmacyAccountancy.
	 */
	public Streamable<AccountancyEntry> findAll() {
		return pharmacyAccountancy.findAll();
	}

}
