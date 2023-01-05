package pharmacy.messages;

import javax.validation.constraints.NotBlank;

class MessagesForm {

	private final @NotBlank String title;
	private final @NotBlank String text;


	/**
	 * Creates a new {@link MessagesEntry} with text and header using the submitted data.
	 *
	 * @param title The messages title.
	 * @param text The message body (text).
	 */
	public MessagesForm(String title, String text) {

		this.title = title;
		this.text = text;
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
	 * Returns a new {@link MessagesEntry} with the submitted data
	 *
	 * @return The created {@link MessagesEntry}
	 */
	MessagesEntry toNewEntry() {
		return new MessagesEntry(getTitle(), getText());
	}
}
