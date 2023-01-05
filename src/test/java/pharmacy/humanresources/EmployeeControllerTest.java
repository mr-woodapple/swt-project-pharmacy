package pharmacy.humanresources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

	@Autowired
	MockMvc mvc;
	@Autowired EmployeeController employeeController;
	@Autowired private UserAccountManagement userAccountManagement;



	@Test
	@WithMockUser(roles = "BOSS")
	void employeesTest() throws Exception {
		mvc.perform(get("/employee-overview"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void detailTest() throws Exception {
		mvc.perform(get("/employee/1"))
				.andExpect(model().attributeExists("employee"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void addEmployeeTest() throws Exception {
		mvc.perform(get("/register"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void editEmployeeTest() throws Exception {
		mvc.perform(get("/employee/1/editEmployee"))
				.andExpect(model().attributeExists("employee"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void addNewDocTest() throws Exception {
		int before = userAccountManagement.findAll().toList().size();
		employeeController.addNewDoc("Arztpraxis");
		assertEquals(before + 1, userAccountManagement.findAll().toList().size());
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void addDocTest() throws Exception {
		mvc.perform(get("/registerdoc"));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	void deleteDocTest() throws Exception {
		employeeController.deleteDoc("1234");
		assertEquals(Optional.empty(), userAccountManagement.findByUsername("1234"));
	}
}
