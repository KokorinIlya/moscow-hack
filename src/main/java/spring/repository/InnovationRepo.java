package spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import spring.entity.Category;
import spring.entity.Innovation;

@RepositoryRestResource
public interface InnovationRepo extends JpaRepository<Innovation, Long> {
}
