package spring.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import spring.entity.Company;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Company> reader() {
		return new FlatFileItemReaderBuilder<Company>()
				.name("companyItemReader")
				.resource(new ClassPathResource("company-data.csv"))
				.delimited()
				.names(new String[]{"fullName", "shortName", "INN", "OGRN", "site", "okved", "okvedNAME"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Company>() {{
					setTargetType(Company.class);
				}})
				.build();
	}

	@Bean
	public CompanyItemProcessor processor() {
		return new CompanyItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Company> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Company>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO Company (fullName, shortName, INN, OGRN, site, okved, okvedNAME) VALUES " +
						"(:fullName, :shortName, :INN, :OGRN, :site, :okved, :okvedNAME)")
				.dataSource(dataSource)
				.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Company> writer) {
		return stepBuilderFactory.get("step1")
				.<Company, Company> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer)
				.build();
	}
	// end::jobstep[]
}