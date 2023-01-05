package pharmacy.messages;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface MessagesRepository extends CrudRepository<MessagesEntry, Long> {

	/**
	 *
	 * @param title
	 * @param sort
	 * @return
	 */
	Streamable<MessagesEntry> findByTitle(String title, Sort sort);

	Streamable<MessagesEntry> findById(long id, Sort sort);
}

