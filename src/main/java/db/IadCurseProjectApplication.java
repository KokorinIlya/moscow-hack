package db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import ru.ifmo.cs.iad.iadcurseproject.entity.Repost;

@SpringBootApplication
//@ImportResource("classpath:spring-config.xml")
//@EnableJpaRepositories(basePackages = "ru.ifmo.cs.iad.iadcurseproject.repository")
//@EntityScan(basePackages = {"ru.ifmo.cs.iad.iadcurseproject"})
public class IadCurseProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(IadCurseProjectApplication.class, args);
	}

//	@Bean
//	public LocalEntityManagerFactoryBean entityManagerFactory() {
//		LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
//		factoryBean.setPersistenceUnitName("cpJpaPu");
//		return factoryBean;
//	}
}
