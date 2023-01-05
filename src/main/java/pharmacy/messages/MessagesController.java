
package pharmacy.messages;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;


@Controller
class MessagesController {

	private final MessagesRepository messages;

	/**
	 *
	 * @param messages
	 */
	MessagesController(MessagesRepository messages) {

		Assert.notNull(messages, "Message must not be null!");
		this.messages = messages;
	}


	/**
	 * Display messages on the login page
	 *
	 * @param model
	 * @param form
	 * @return
	 */
	@GetMapping("/")
	String viewMessages(Model model, @ModelAttribute(binding = false) MessagesForm form) {

		model.addAttribute("messageList", messages.findAll());

		return "login";
	}


	/**
	 * Display messages below the "Create message form"
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("messages")
	@PreAuthorize("hasRole('BOSS')")
	String viewMessagesBoss(Model model) {

		model.addAttribute("messageList", messages.findAll());
		return "messages";

	}


	/**
	 * Allows to create a new message entry
	 *
	 * @param form
	 * @return
	 */
	@PostMapping("addMessage")
	@PreAuthorize("hasRole('BOSS')")
	String addEntry(@Valid @ModelAttribute("form") MessagesForm form) {

		messages.save(form.toNewEntry());

		return "redirect:/messages";
	}


	/**
	 * Delete Entry via the unique id that every message has
	 *
	 * @param id Unique id for each message
	 * @return the messages.html view
	 */

	@GetMapping(path = "/removeEntry/{id}")
	@PreAuthorize("hasRole('BOSS')")
	String removeEntry(@PathVariable Long id) {

		messages.deleteById(id);

		return "redirect:/messages";
	}
}
