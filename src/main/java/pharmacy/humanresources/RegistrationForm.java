package pharmacy.humanresources;

import javax.validation.constraints.NotEmpty;

class RegistrationForm {

	@NotEmpty(message = "{RegistrationForm.username.NotEmpty}")
	private final String firstname;

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}")
	private final String lastname;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}")
	private final String password;

	@NotEmpty(message = "{RegistrationForm.address.NotEmpty}")
	private final String address;

	@NotEmpty(message = "{RegistrationForm.bankinfo.NotEmpty}")
	private final String bankinfo;

	@NotEmpty(message = "{RegistrationForm.role.NotEmpty}")
	private final String role;

	/**
	 * RegistrationForm is used to contain all the information for Employee creation
	 * @param firstname
	 * @param lastname
	 * @param password
	 * @param address
	 * @param bankinfo
	 * @param role
	 */
	public RegistrationForm(String firstname, String lastname, String password, String address, String bankinfo,
							String role) {

		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.bankinfo = bankinfo;
		this.address = address;
		this.role = role;
	}

	// (getters and setters are self-explanatory)

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getPassword() {
		return password;
	}

	public String getAddress() {
		return address;
	}

	public String getBankinfo() {
		return bankinfo;
	}

	public String getRole() {
		return role;
	}

}