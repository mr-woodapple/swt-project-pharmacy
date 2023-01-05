package pharmacy.messages;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
public class MessagesEntry {

	private @Id @GeneratedValue Long id;
	private final String title, text;

	/**
	 *
	 * @param title
	 * @param text
	 */
	public MessagesEntry(String title, String text) {

		Assert.hasText(title, "Title must not be null or empty!");
		Assert.hasText(text, "Text must not be null or empty!");

		this.title = title;
		this.text = text;
	}

	@SuppressWarnings("unused")
	public MessagesEntry() {
		this.title = null;
		this.text = null;
	}


	/**
	 * Returns the message title
	 *
	 * @return title as String
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * Returns the message body
	 *
	 * @return text as String
	 */
	public String getText() {
		return text;
	}


	/**
	 * Returns the id linked to a single message
	 *
	 * @return id as Long
	 */
	public Long getId() {
		return id;
	}
}
