package spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import spring.entity.Category;
import spring.entity.Company;

@RepositoryRestResource
public interface CategoryRepo extends JpaRepository<Category, Long> {
}
