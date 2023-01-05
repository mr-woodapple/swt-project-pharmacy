package pharmacy.humanresources;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class EmployeeManagement {

	public static final Role EMPLOYEE_ROLE = Role.of("EMPLOYEE");
	public static final Role BOSS_ROLE = Role.of("BOSS");
	public static final Role LABORANT_ROLE = Role.of("LABORANT");
	public static final Role DOC_ROLE = Role.of("DOC");

	private final EmployeeRepository employees;
	private final UserAccountManagement userAccounts;

	/**
	 * EmployeeManagement manages both the userAccount and employee databases
	 * @param employees database for the employees
	 * @param userAccounts database for the userAccounts
	 */
	EmployeeManagement(EmployeeRepository employees, UserAccountManagement userAccounts) {

		Assert.notNull(employees, "EmployeeRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

		this.employees = employees;
		this.userAccounts = userAccounts;
	}

	/**
	 * Function that returns all Employees
	 * @return all Employees found in the employee database
	 */
	public Streamable<Employee> findAll() {

		return employees.findAll();
	}

	/**
	 * Function that returns a specific Employee (identified by their unique id)
	 * @param id the employee's unique id
	 * @return the Employee whom the id belongs to
	 */
	public Employee findEmployee(long id) {

		return employees.findById(id).get();
	}

	/**
	 * Function that returns a specific Employee (identified by their unique username)
	 * @param username the employee's unique username
	 * @return the Employee whom the username belongs to
	 */
	public Employee findEmployeeByUsername(String username) {

		// a list with all the employees is created
		List<Employee> employeelist = employees.findAll().toList();
		Iterator i = employeelist.iterator();
		Employee employee;

		// i iterates over the list until an employee with the matching username is found
		do {
			if (!i.hasNext()) {
				return null;
			}
			employee = (Employee) i.next();
		} while (!employee.getUserAccount().getUsername().equals(username));

		return employee;
	}

	/**
	 * Function that returns the employee that is currently logged in (needed for checking if the Boss that is
	 * currently logged in wants to delete himself which isn't supposed to be possible)
	 * (Only an Employee with boss role must be stopped from deleting himself which is why only the employee
	 * database needs to be checked)
	 * @return the Employee which is currently logged in
	 */
	public Employee getLoggedIn() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		return findEmployeeByUsername(username);
	}

	/**
	 * Function that returns the first two Letters of a String (needed for username creation)
	 * @param name the String of which the first two letters should be returned
	 * @return the first two Letters of the String name
	 */
	public String firstTwo(@NotNull String name) {

		return name.length() < 2 ? name : name.substring(0, 2);
	}

	/**
	 * Function that creates and returns a unique username for a new Employee
	 * @param form the Registration Form the boss filled in
	 * @return a unique username created from the information on the Registration Form
	 */
	public String createUsername(@org.jetbrains.annotations.NotNull RegistrationForm form) {

		// the first two letters of the first name and the first two letters of the last name are combined and the
		// number 10 is added to the username
		int number = 10;
		var username = firstTwo(form.getFirstname()) + firstTwo(form.getLastname()) + number;

		// the userAccount database is checked for the generated username. If it already exists, the number is
		// increased until an unused username is found
		boolean username_exists = true;
		while (username_exists) {
			if (userAccounts.findByUsername(username).isEmpty()) {
				username_exists = false;

			} else {
				number++;
				username = firstTwo(form.getFirstname()) + firstTwo(form.getLastname()) + number;
			}
		}
		return username;
	}

	/**
	 * Function that creates a new Employee with Role of Employee
	 * @param form the Registration Form the boss filled in
	 * @return the new Employee with Employee Role created from the information on the Registration Form
	 */
	public Employee addEmployee(RegistrationForm form) {

		Assert.notNull(form, "Registration form must not be null!");

		var password = Password.UnencryptedPassword.of(form.getPassword());
		var username = createUsername(form);
		var userAccount = userAccounts.create(username, password, EMPLOYEE_ROLE);

		return employees.save(new Employee(userAccount, username, form.getFirstname(), form.getLastname(),
				form.getAddress(), form.getBankinfo()));
	}

	/**
	 * Function that creates a new Employee with Role of Boss
	 * @param form the Registration Form the boss filled in
	 * @return the new Employee with Boss Role created from the information on the Registration Form
	 */
	public Employee addManager(RegistrationForm form) {

		Assert.notNull(form, "Registration form must not be null!");

		var password = Password.UnencryptedPassword.of(form.getPassword());
		var username = createUsername(form);
		var userAccount = userAccounts.create(username, password, BOSS_ROLE);

		return employees.save(new Employee(userAccount, username, form.getFirstname(), form.getLastname(),
				form.getAddress(), form.getBankinfo()));
	}

	/**
	 * Function that creates a new Employee with Role of Laborant
	 * @param form the Registration Form the boss filled in
	 * @return the new Employee with Lab Role created from the information on the Registration Form
	 */
	public Employee addLaborant(RegistrationForm form) {

		Assert.notNull(form, "Registration form must not be null!");

		var password = Password.UnencryptedPassword.of(form.getPassword());
		var username = createUsername(form);
		var userAccount = userAccounts.create(username, password, LABORANT_ROLE);

		return employees.save(new Employee(userAccount, username, form.getFirstname(), form.getLastname(),
				form.getAddress(), form.getBankinfo()));
	}

	/**
	 * Function that creates and returns a unique pin for a new Doctor
	 * @return a unique 4-digit pin
	 */
	public String createPin(){

		// create a random 4-digit number (String format so it can be used as username)
		Random random = new Random();
		int number = random.nextInt(10000);
		String pin = String.format("%04d", number);

		// check if pin is already used by other Doc
		// if so, create new random pin until an unused one is found
		boolean pin_exists = true;
		while (pin_exists) {
			if (userAccounts.findByUsername(pin).isEmpty()) {
				pin_exists = false;

			} else {
				pin = createPin();
			}
		}

		return pin;
	}

	/**
	 * Function that saves a new Employee to the database
	 * @param employee the Employee which should be saved
	 */
	public void saveEmployee(Employee employee) {

		employees.save(employee);
	}

	/**
	 * Function that deletes an Employee and their userAccount from the database
	 * @param employee the Employee which should be deleted
	 */
	public void deleteEmployee(@org.jetbrains.annotations.NotNull Employee employee) {

		employees.deleteById(employee.getId());
		userAccounts.delete(employee.getUserAccount());
	}

	/**
	 * Function that creates a new userAccount with Role of Doc (Doctors aren't added to the employee database)
	 * @param docname the name of the doctor's office
	 * @return the new UserAccount with Doc Role
	 */
	public UserAccount addDoc(String docname){

		Assert.notNull(docname, "docname must not be null!");

		var pin = createPin();
		var password = Password.UnencryptedPassword.of("password");

		return userAccounts.create(pin, password, docname, DOC_ROLE);
	}
}