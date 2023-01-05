package pharmacy.humanresources;

import javax.validation.Valid;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class EmployeeController {

	private final EmployeeManagement employeeManagement;
	private final UserAccountManagement userAccountManagement;

	/**
	 * EmployeeController is used to manage the Post- and GetMapping for human resources
	 * @param userAccountManagement database for UserAccounts
	 * @param employeeManagement database for Employees
	 */
	EmployeeController(UserAccountManagement userAccountManagement, EmployeeManagement employeeManagement) {

		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(employeeManagement, "EmployeeRepository must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.employeeManagement = employeeManagement;
	}

	/**
	 * Function to find all Employees and Doctors in the database to display in employee-overview
	 * @param model
	 * @return the view for the employee overview page
	 */
	@GetMapping("/employee-overview")
	@PreAuthorize("hasRole('BOSS')")
	String employees(Model model) {

		// find all users in userAccountManagement
		var users = userAccountManagement.findAll();
		// create empty Streamable for docs
		Streamable<UserAccount> docs = Streamable.empty();

		// if a user has Doc Role, add it to docs
		for (UserAccount user : users) {
			if (user.hasRole(Role.of("DOC"))) {
				docs = docs.and(user);
			}
		}

		// add all docs to the model
		model.addAttribute("docList", docs);

		// find all employees in employeeManagement and add them to the model
		model.addAttribute("employeeList", employeeManagement.findAll());

		return "employee-overview";
	}

	/**
	 * Function to show detailed Employee information and display it in employee-detail
	 * @param id the unique Employee id of the selected employee
	 * @param model
	 * @return the view for the detail page (for the specific employee)
	 */
	@GetMapping("/employee/{id}")
	@PreAuthorize("hasRole('BOSS')")
	String detail(@PathVariable long id, Model model) {

		// find the employee in employeeManagement and add it to the model
		model.addAttribute("employee", employeeManagement.findEmployee(id));

		return "employee-detail";
	}

	/**
	 * Function that is triggered when a boss wants to add a new employee or boss or lab technician by
	 * filling in a Registration Form (PostMapping)
	 * The authorization the new employee should have depends on the role checked in the registration form
	 * @param form the Registration Form the boss filled in
	 * @param result the result from the error check that checks the form
	 * @return the url for the employee overview page after the new employee is created
	 */
	@PostMapping("/register")
	String addNewEmployee(@Valid RegistrationForm form, Errors result) {

		if (result.hasErrors()) {
			return "register";
		}
		if (form.getRole().equals("Boss")) {
			employeeManagement.addManager(form);
		}
		if (form.getRole().equals("Employee")) {
			employeeManagement.addEmployee(form);
		}
		if (form.getRole().equals("Laborant")) {
			employeeManagement.addLaborant(form);
		}

		return "redirect:/employee-overview";
	}

	/**
	 * Function that is triggered when a boss wants to add a new employee or boss or lab technician by
	 * filling in a Registration Form (GetMapping)
	 * @param model
	 * @param form the Registration Form
	 * @return the url for the employee register page
	 */
	@GetMapping("/register")
	@PreAuthorize("hasRole('BOSS')")
	String addEmployee(Model model, RegistrationForm form) {
		return "register";
	}

	/**
	 * Function that is triggered when a boss wants to edit the information of an employee by filling
	 * in a new Registration Form with the updated information (PostMapping)
	 * @param id
	 * @param form the updated Registration Form the boss filled in
	 * @return the url for the employee overview page after the employee was edited
	 */
	@PostMapping("/employee/{id}/editEmployee")
	String editEmployee(@PathVariable long id, RegistrationForm form) {

		Employee employee = employeeManagement.findEmployee(id);

		employee.setFirstname(form.getFirstname());
		employee.setLastname(form.getLastname());
		employee.setAddress(form.getAddress());
		employee.setBankinfo(form.getBankinfo());

		employeeManagement.saveEmployee(employee);

		return "redirect:/employee-overview";
	}

	/**
	 * Function that is triggered when a boss wants to edit the information of an employee by filling
	 * in a new Registration Form with the updated information (GetMapping)
	 * @param id the unique Employee id of the selected employee
	 * @param model
	 * @return the url for the employee's editing page
	 */
	@GetMapping("/employee/{id}/editEmployee")
	@PreAuthorize("hasRole('BOSS')")
	String editEmployee(@PathVariable long id, Model model) {

		model.addAttribute("employee", employeeManagement.findEmployee(id));
		return "employee-edit";
	}

	/**
	 * Function that deletes a selected Employee (identified by their id)
	 * @param id the unique Employee id of the selected employee
	 * @return the url for the employee overview page after the employee was deleted
	 */
	@GetMapping("/deleteEmployee/{id}")
	@PreAuthorize("hasRole('BOSS')")
	String deleteEmployee(@PathVariable long id) {

		Employee employee = employeeManagement.findEmployee(id);
		Employee loggedin = employeeManagement.getLoggedIn();

		if (employee == loggedin) {
			return "redirect:/employee-overview";
		} else {
			employeeManagement.deleteEmployee(employee);
		}
		return "redirect:/employee-overview";
	}

	/**
	 * Function that is triggered when a boss wants to add a new UserAccount for a Doctor
	 * (Doctors aren't saved as Employees)
	 * @param docname the name of the doctor's office
	 * @return the url for the employee overview page after the new doctor was created
	 */
	@PostMapping("/registerdoc")
	String addNewDoc(String docname) {

		employeeManagement.addDoc(docname);

		return "redirect:/employee-overview";
	}

	/**
	 * Function that is triggered when a boss wants to add a new UserAccount for a Doctor
	 * (Doctors aren't saved as Employees)
	 * @param model
	 * @param docname the name of the doctor's office
	 * @return the url for the doctor register page
	 */
	@GetMapping("/registerdoc")
	@PreAuthorize("hasRole('BOSS')")
	String addDoc(Model model, String docname) {
		return "registerdoc";
	}

	/**
	 * Function that deletes a selected Employee (identified by their username (pin))
	 * @param username the selected doctor's username (their pin)
	 * @return the url for the employee overview page after the doctor was deleted
	 */
	@GetMapping("/deleteDoc/{username}")
	@PreAuthorize("hasRole('BOSS')")
	String deleteDoc(@PathVariable String username) {

		UserAccount doc = userAccountManagement.findByUsername(username).get();
		userAccountManagement.delete(doc);

		return "redirect:/employee-overview";
	}
}