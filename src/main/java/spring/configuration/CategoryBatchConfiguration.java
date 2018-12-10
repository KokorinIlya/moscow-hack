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
import spring.entity.Category;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class CategoryBatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Category> readerCategory() {
		return new FlatFileItemReaderBuilder<Category>()
				.name("categoryItemReader")
				.resource(new ClassPathResource("category-data.csv"))
				.delimited()
				.names(new String[]{"id", "name"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Category>() {{
					setTargetType(Category.class);
				}})
				.build();
	}

	@Bean
	public CategoryItemProcessor processorCategory() {
		return new CategoryItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Category> writerCategory(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Category>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO Category (id, name) VALUES " +
						"(:id, :name)")
				.dataSource(dataSource)
				.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importCategoryJob(CategoryJobCompletionNotificationListener listener, @Qualifier("stepCategory") Step step1) {
		return jobBuilderFactory.get("importCategories")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	public Step stepCategory(JdbcBatchItemWriter<Category> writer) {
		return stepBuilderFactory.get("stepCategory")
				.<Category, Category> chunk(10)
				.reader(readerCategory())
				.processor(processorCategory())
				.writer(writer)
				.build();
	}
	// end::jobstep[]
}