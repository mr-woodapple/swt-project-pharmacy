package pharmacy.humanresources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.salespointframework.useraccount.UserAccountManagement;


@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	UserAccountManagement userAccountManagement;
	@Autowired
	EmployeeManagement employeeManagement;

	String firstname = "Vorname";
	String lastname = "Nachname";
	@Mock Employee employee;

	/*@Test
	void setFirstnameTest(){
		String newfirstname = "Hildegard";
		employee.setFirstname(newfirstname);
		employee.getFirstname();

		Assertions.assertEquals("Hildegard", employee.getFirstname());
	}*/

}
