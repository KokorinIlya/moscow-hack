package spring.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.entity.Category;
import spring.entity.Company;
import spring.entity.Innovation;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			jdbcTemplate.query("SELECT * FROM Company",
					(rs, row) -> new Company(
							rs.getString(1),
							rs.getString(2),
							rs.getLong(3),
							rs.getLong(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7),
							(Category) rs.getObject(8),
							(Innovation) rs.getObject(9))
					).forEach(person -> log.info("Found <" + person + "> in the database."));
		}
	}
}
