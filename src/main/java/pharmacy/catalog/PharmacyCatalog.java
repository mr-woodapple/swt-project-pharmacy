package pharmacy.catalog;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.util.Assert;
import pharmacy.catalog.Article.ArticleType;

public interface PharmacyCatalog extends Catalog<Article> {

	static final Sort DEFAULT_SORT = Sort.by("name").descending();

	@Query("select (count(a) > 0) from Article a where a.id = ?1")
	boolean existsArticleByID(int id);

	Iterable<Article> findByType(ArticleType type, Sort sort);

	Iterable<Article> findByTypeAndPrescription(ArticleType type, boolean prescription, Sort sort);


	default Iterable<Article> findByType(ArticleType type) {
		return findByType(type, DEFAULT_SORT);
	}

	default Iterable<Article> findByTypeAndPrescription(ArticleType type, boolean prescription) {
		return findByTypeAndPrescription(type, prescription, DEFAULT_SORT);
	}
}



