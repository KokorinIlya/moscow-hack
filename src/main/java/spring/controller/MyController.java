package spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.entity.Category;
import spring.entity.Company;
import spring.entity.Innovation;
import spring.repository.CategoryRepo;
import spring.repository.CompanyRepo;
import spring.repository.InnovationRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RepositoryRestController
@RequestMapping("/my")
public class MyController {
	private final CompanyRepo companyRepo;
	private final InnovationRepo innovationRepo;
	private final CategoryRepo categoryRepo;

	@PersistenceContext
	private EntityManager em; // TODO: IS NULL

	private Logger logger = LoggerFactory.getLogger("MyController");

	@Autowired
	public MyController(CompanyRepo companyRepo, InnovationRepo innovationRepo, CategoryRepo categoryRepo) {
		this.companyRepo = companyRepo;
		this.innovationRepo = innovationRepo;
		this.categoryRepo = categoryRepo;
	}

	@GetMapping("/companies/{companyId}/categorySet/add")
	public ResponseEntity setCompanyCategory(@PathVariable("companyId") long companyId,
	                                         @RequestParam(value = "categoryId") long categoryId) {
		logger.info("Adding category");
		Company company = companyRepo.getById(companyId);
		Category category = categoryRepo.getById(categoryId);
		company.addCategory(category);
		companyRepo.save(company);
		categoryRepo.save(category);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/companies/{companyId}/innovationSet/add")
	public ResponseEntity setCompanyInnovation(@PathVariable("companyId") long companyId,
	                                 @RequestParam(value = "innovationId") long innovationId) {
		logger.info("Adding innovation");
		Company company = companyRepo.getById(companyId);
		Innovation innovation = innovationRepo.getById(innovationId);
		company.addInnovation(innovation);
		companyRepo.save(company);
		innovationRepo.save(innovation);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
