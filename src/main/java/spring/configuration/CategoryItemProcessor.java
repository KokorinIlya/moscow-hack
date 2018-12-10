package spring.configuration;

import org.springframework.batch.item.ItemProcessor;
import spring.entity.Category;
import spring.entity.Company;

public class CategoryItemProcessor implements ItemProcessor<Category, Category> {

	@Override
	public Category process(final Category category) {
		return category;
	}

}