package pharmacy.catalog;

import static org.salespointframework.core.Currencies.*;

import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import pharmacy.catalog.Article.ArticleType;

@Component
@Order(20)

public class CatalogDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);

	private final PharmacyCatalog pharmacyCatalog;

	CatalogDataInitializer(PharmacyCatalog pharmacyCatalog) {

		Assert.notNull(pharmacyCatalog, "pharmacyCatalog must not be null!");

		this.pharmacyCatalog = pharmacyCatalog;
	}

	public String beipackzettel = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
			+ "Morbi sit amet porttitor ex. Donec ultricies ipsum non leo mattis, "
			+ "ac sodales nunc facilisis. Proin interdum erat venenatis, congue turpis a, "
			+ "dignissim turpis. Sed eu nisl ac justo egestas iaculis. "
			+ "Donec sollicitudin imperdiet diam et mattis. Pellentesque tincidunt risus id enim dictum mattis. "
			+ "Pellentesque sit amet orci elementum, ornare nulla hendrerit, hendrerit risus. "
			+ "Nulla auctor congue mi, sed fringilla sapien tincidunt nec. "
			+ "Aliquam mollis mattis augue, tincidunt tincidunt felis semper sit amet. "
			+ "Mauris ut ante commodo, volutpat diam vitae, imperdiet quam. Quisque at blandit odio.";

	@Override
	public void initialize() {

		if (pharmacyCatalog.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default catalog entries.");
		
		//prescription drugs
		pharmacyCatalog.save(new Article("Vicodin", 100000, "30x 300mg", LocalDate.of(2023,
				1, 19), "generic-drug", Money.of(100, EURO), Money.of(10, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", true, ""));
		pharmacyCatalog.save(new Article("OxyContin", 200000, "30x 300mg", LocalDate.of(2023,
				1, 19), "generic-drug", Money.of(19.99, EURO), Money.of(10, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", true, ""));
		pharmacyCatalog.save(new Article("Kadian", 300000, "30x 300mg", LocalDate.of(2022,
				1, 19), "generic-drug", Money.of(29.99, EURO), Money.of(10, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", true, ""));
		pharmacyCatalog.save(new Article("Percocet", 400000, "30x 300mg", LocalDate.of(2022,
				1, 19), "generic-drug", Money.of(9.99, EURO), Money.of(5, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", true, ""));
		pharmacyCatalog.save(new Article("Alprazolam", 500000, "30x 300mg", LocalDate.of(2021,
				1, 19), "generic-drug", Money.of(39.99, EURO), Money.of(10, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", true, ""));

		//prescription free drugs
		pharmacyCatalog.save(new Article("Aspirin", 600000, "30x 300mg", LocalDate.of(2023,
				1, 19), "generic-drug", Money.of(6.99, EURO), Money.of(3, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", false, ""));
		pharmacyCatalog.save(new Article("Ibuprofen", 700000, "30x 300mg", LocalDate.of(2023,
				1, 19), "generic-drug", Money.of(9.99, EURO), Money.of(5, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", false, ""));
		pharmacyCatalog.save(new Article("Panandol", 800000, "30x 300mg", LocalDate.of(2022,
				1, 19), "generic-drug", Money.of(9.99, EURO), Money.of(5, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", false, ""));
		pharmacyCatalog.save(new Article("Ricola", 900000, "30x 300mg", LocalDate.of(2021,
				1, 19), "generic-drug", Money.of(3.99, EURO), Money.of(2, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", false, ""));
		pharmacyCatalog.save(new Article("Pflaster", 110000, "30x 300mg", LocalDate.of(2020,
				1, 19), "generic-drug", Money.of(9.99, EURO), Money.of(5, EURO),
				beipackzettel, ArticleType.CATALOG, "", "", false, ""));

		//mixtures
		pharmacyCatalog.save(new Article("Mixture Folidol + Orthocide 70g", 800000, "Custom sized",
				LocalDate.of(2022, 1, 19), "generic-drug", Money.of(0, EURO),
				Money.of(0, EURO), "Methyl Parathion,Captane", ArticleType.INPREPARATION,
				"Fabian Donarumma", "017534823942", true, ""));
		pharmacyCatalog.save(new Article("Hydrophil ointment DAB 100g", 900000, "Custom sized",
				LocalDate.of(2022, 1, 19), "generic-drug", Money.of(0, EURO),
				Money.of(0, EURO), "Erythromycin 2g", ArticleType.INPREPARATION,
				"Lucia Previtera", "017225634781", true, ""));
		pharmacyCatalog.save(new Article("Mixture Sevin 850 SC + Folisuper + Orthocide", 110000,
				"Custom sized", LocalDate.of(2022, 1, 19), "generic-drug",
				Money.of(11.99, EURO), Money.of(0, EURO), "Carbaril, Methyl Parathion, Captane",
				ArticleType.READYORDERS, "Alessandro Bisogni", "017453879248",
				true, ""));


	}
}