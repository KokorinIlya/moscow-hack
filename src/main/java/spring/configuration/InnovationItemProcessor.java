package spring.configuration;

import org.springframework.batch.item.ItemProcessor;
import spring.entity.Company;
import spring.entity.Innovation;

public class InnovationItemProcessor implements ItemProcessor<Innovation, Innovation> {

	@Override
	public Innovation process(final Innovation innovation) {
		return innovation;
	}

}