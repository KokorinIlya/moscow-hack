//package db.controller;
//
//import db.entity.Companies;
//import db.repository.CompaniesRepo;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.Optional;
//
//import static org.springframework.data.domain.PageRequest.of;
//import static org.springframework.data.domain.Sort.by;
//
//@Controller
//public class MyController {
//	private final CompaniesRepo companiesRepo;
//
//	@PersistenceContext
//	private EntityManager em; // TODO: IS NULL
//
//	private Logger logger = LoggerFactory.getLogger("UserController");
//
//	@Autowired
//	public MyController(CompaniesRepo companiesRepo) {
//		this.companiesRepo = companiesRepo;
//	}
//
//	public void getProfileById() {
//		logger.info("getProfileById=");
//		Optional<Companies> company = companiesRepo.findById();
//		if (!company.isPresent()) {
//			//
//		}
//	}
//}
