package pharmacy.humanresources;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.accountancy.Accountancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import javax.money.MonetaryAmount;

import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeManagementTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	EmployeeManagement employeeManagement;
	@Mock
	Model model;

	@Test
	void firstTwoTest(){
		String name = "Joachim";

		Assertions.assertEquals("Jo", employeeManagement.firstTwo(name));
	}

	void addEmployeeTest(){
		RegistrationForm form = new RegistrationForm("Mark","Otto", "123", "Lukasplatz 22","DE12 1001 0010 0348 9838 90", "Employee");


	}

}
