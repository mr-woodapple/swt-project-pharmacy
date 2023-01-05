package pharmacy.humanresources;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

interface EmployeeRepository extends CrudRepository<Employee, Long> {

	@Override
	Streamable<Employee> findAll();
}