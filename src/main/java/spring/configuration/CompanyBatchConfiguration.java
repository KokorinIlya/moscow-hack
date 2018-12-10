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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import spring.entity.Company;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class CompanyBatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Company> readerCompany() {
		return new FlatFileItemReaderBuilder<Company>()
				.name("companyItemReader")
				.resource(new ClassPathResource("company-data.csv"))
				.delimited()
				.names(new String[]{"id", "fullName", "shortName", "INN", "OGRN", "site", "okved", "okvedName"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Company>() {{
					setTargetType(Company.class);
				}})
				.build();
	}

	@Bean
	public CompanyItemProcessor processorCompany() {
		return new CompanyItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Company> writerCompany(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Company>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO Company (id, full_name, short_name, inn, ogrn, site, okved, okved_name) VALUES " +
						"(:id, :fullName, :shortName, :INN, :OGRN, :site, :okved, :okvedName)")
				.dataSource(dataSource)
				.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importCompanyJob(CompanyJobCompletionNotificationListener listener, @Qualifier("stepCompany") Step step1) {
		return jobBuilderFactory.get("importCompanies")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	public Step stepCompany(JdbcBatchItemWriter<Company> writer) {
		return stepBuilderFactory.get("stepCompany")
				.<Company, Company> chunk(10)
				.reader(readerCompany())
				.processor(processorCompany())
				.writer(writer)
				.build();
	}
	// end::jobstep[]
}