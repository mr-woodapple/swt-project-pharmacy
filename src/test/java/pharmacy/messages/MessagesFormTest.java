package pharmacy.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessagesFormTest {
	@Test
	void testConstructor() {
		MessagesForm actualMessagesForm = new MessagesForm("Title", "Text");

		assertEquals("Text", actualMessagesForm.getText());
		assertEquals("Title", actualMessagesForm.getTitle());
	}

	@Test
	void testToNewEntry() {
		MessagesEntry actualToNewEntryResult = (new MessagesForm("Title", "Text")).toNewEntry();
		assertEquals("Title", actualToNewEntryResult.getTitle());
		assertEquals("Text", actualToNewEntryResult.getText());
	}
}

