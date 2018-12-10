package spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import spring.entity.Company;

@RepositoryRestResource
public interface CompanyRepo extends JpaRepository<Company, Long> {
	Company getById(Long id);
}
