package spring.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.entity.Company;

@Component
public class CompanyJobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(CompanyJobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public CompanyJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("companySet added");

//			jdbcTemplate.query("SELECT id, full_name, short_name, inn, ogrn, site, okved, okved_name FROM Company",
//					(rs, row) -> new Company(
//							rs.getLong(1),
//							rs.getString(2),
//							rs.getString(3),
//							rs.getLong(4),
//							rs.getLong(5),
//							rs.getString(6),
//							rs.getString(7),
//							rs.getString(8))
//					).subList(0, 1).forEach(person -> log.info("Found <" + person + "> in the database."));
		}
	}
}
