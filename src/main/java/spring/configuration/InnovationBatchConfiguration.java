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
import spring.entity.Innovation;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class InnovationBatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Innovation> readerInnovation() {
		return new FlatFileItemReaderBuilder<Innovation>()
				.name("innovationItemReader")
				.resource(new ClassPathResource("innovation-data.csv"))
				.delimited()
				.names(new String[]{"id", "name"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Innovation>() {{
					setTargetType(Innovation.class);
				}})
				.build();
	}

	@Bean
	public InnovationItemProcessor processorInnovation() {
		return new InnovationItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Innovation> writerInnovation(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Innovation>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO Innovation (id, name) VALUES " +
						"(:id, :name)")
				.dataSource(dataSource)
				.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importInnovationJob(InnovationJobCompletionNotificationListener listener, @Qualifier("stepInnovation") Step step1) {
		return jobBuilderFactory.get("importInnovations")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	public Step stepInnovation(JdbcBatchItemWriter<Innovation> writer) {
		return stepBuilderFactory.get("stepInnovation")
				.<Innovation, Innovation> chunk(10)
				.reader(readerInnovation())
				.processor(processorInnovation())
				.writer(writer)
				.build();
	}
	// end::jobstep[]
}