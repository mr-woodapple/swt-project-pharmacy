package pharmacy.humanresources;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Employee {

	// every Employee has an automatically generated unique id
	private @Id
	@GeneratedValue
	long id;

	private String username;
	private String firstname;
	private String lastname;
	private String address;
	private String bankinfo;

	@OneToOne
	private UserAccount userAccount;

	@SuppressWarnings("unused")
	public Employee() {
	}

	/**
	 * Entity Employee includes a UserAccount and all other employee information saved in Human Resources
	 * @param userAccount the userAccount with which the Employee can log in
	 * @param username the (automatically generated) username with which the Employee can log in
	 * @param firstname the Employee's first name(s)
	 * @param lastname the Employee's last name
	 * @param address the Employee's address
	 * @param bankinfo the Employee's bank information
	 */
	public Employee(UserAccount userAccount, String username, String firstname, String lastname, String address,
					String bankinfo) {
		this.userAccount = userAccount;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.bankinfo = bankinfo;
	}

	// (getters and setters are self-explanatory)

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBankinfo() {
		return bankinfo;
	}

	public void setBankinfo(String bankinfo) {
		this.bankinfo = bankinfo;
	}


	public UserAccount getUserAccount() {
		return userAccount;
	}
}