package pharmacy.humanresources;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
class EmployeeDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeDataInitializer.class);

	private final UserAccountManagement userAccountManagement;
	private final EmployeeManagement employeeManagement;


	/**
	 * EmployeeDataInitializer initializes the database
	 * @param userAccountManagement
	 * @param employeeManagement
	 */
	EmployeeDataInitializer(UserAccountManagement userAccountManagement, EmployeeManagement employeeManagement) {

		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(employeeManagement, "EmployeeRepository must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.employeeManagement = employeeManagement;
	}


	/**
	 * Function that initializes the database so there are already some generic Users and Employees to work with
	 */
	@Override
	public void initialize() {

		// skip initializing if database is already populated
		if (userAccountManagement.findByUsername("boss").isPresent()) {
			return;
		}

		LOG.info("Creating default users and customers.");

		userAccountManagement.create("boss", UnencryptedPassword.of("123"), Role.of("BOSS"));
		userAccountManagement.create("employee", UnencryptedPassword.of("123"), Role.of("EMPLOYEE"));
		userAccountManagement.create("laborant", UnencryptedPassword.of("123"), Role.of("LABORANT"));
		userAccountManagement.create("1234", UnencryptedPassword.of("password"),
				"Zahnarztpraxis Dr. Gabriele Best", Role.of("DOC"));
		userAccountManagement.create("5678", UnencryptedPassword.of("password"),
				"Praxis Dr. med. Joachim Krohn", Role.of("DOC"));
		userAccountManagement.create("customer", UnencryptedPassword.of("123"), Role.of("CUSTOMER"));
		var password = "123";

		List.of(//
				new RegistrationForm("Mark", "Otto", password, "Lukasplatz 22",
						"DE12 1001 0010 0348 9838 90", "Employee"),
				new RegistrationForm("Jakob", "Niebuhr", password, "Hohe Straße 8b",
						"DE23 0101 1001 0453 8734 67", "Employee"),
				new RegistrationForm("Julia", "Meier", password, "Liebigstraße 5",
						"DE34 1000 0010 0654 2731 21", "Employee"),
				new RegistrationForm("Georg", "Spitzauer", password, "Uhlandstraße 38",
						"DE09 1100 1000 0387 2258 91", "Employee"),
				new RegistrationForm("Thea", "Hoffmann", password, "Parkstraße 14",
						"DE37 0111 1001 0362 3893 29", "Employee")
		).forEach(employeeManagement::addEmployee);

		List.of(//
				new RegistrationForm("Hanna", "Kramer", password, "Elisenstraße 15",
						"DE67 1011 0010 0369 8290 33", "Boss")
		).forEach(employeeManagement::addManager);

	}
}