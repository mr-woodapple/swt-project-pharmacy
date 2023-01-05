package pharmacy.inventory;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import pharmacy.catalog.PharmacyCatalog;

@Component
@Order(20)

public class InventoryInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final PharmacyCatalog pharmacyCatalog;

	InventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory, PharmacyCatalog pharmacyCatalog) {

		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(pharmacyCatalog, "PharmacyCatalog must not be null!");

		this.inventory = inventory;
		this.pharmacyCatalog = pharmacyCatalog;
	}

	@Override
	public void initialize() {

		pharmacyCatalog.findAll().forEach(article -> {

			if (inventory.findByProduct(article).isEmpty()) {
				inventory.save(new UniqueInventoryItem(article, Quantity.of(100)));
			}
		});
	}
}