package pharmacy.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class MessagesEntryTest {
	@Test
	void testConstructor() {
		MessagesEntry actualMessagesEntry = new MessagesEntry();
		assertNull(actualMessagesEntry.getId());
		assertNull(actualMessagesEntry.getText());
		assertNull(actualMessagesEntry.getTitle());
	}

	@Test
	void testConstructor2() {
		MessagesEntry actualMessagesEntry = new MessagesEntry("Title", "Text");

		assertEquals("Title", actualMessagesEntry.getTitle());
		assertEquals("Text", actualMessagesEntry.getText());
	}
}

