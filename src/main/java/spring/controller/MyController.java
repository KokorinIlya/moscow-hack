package spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import spring.repository.CategoryRepo;
import spring.repository.CompanyRepo;
import spring.repository.InnovationRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RepositoryRestController
public class MyController {
	private final CompanyRepo companyRepo;
	private final InnovationRepo innovationRepo;
	private final CategoryRepo categoryRepo;

	@PersistenceContext
	private EntityManager em; // TODO: IS NULL

	private Logger logger = LoggerFactory.getLogger("UserController");

	@Autowired
	public MyController(CompanyRepo companyRepo, InnovationRepo innovationRepo, CategoryRepo categoryRepo) {
		this.companyRepo = companyRepo;
		this.innovationRepo = innovationRepo;
		this.categoryRepo = categoryRepo;
	}

	public void setCompanyCategory(Long companyId, Long categoryId) {

	}

	public void getProfileById() {
		logger.info("getProfileById=");
//		Optional<Company> company = companiesRepo.findById();
//		if (!company.isPresent()) {
//			//
//		}
	}
}
