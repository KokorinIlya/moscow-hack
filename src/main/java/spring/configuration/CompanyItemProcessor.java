package spring.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import spring.entity.Category;
import spring.entity.Company;
import spring.entity.Innovation;

public class CompanyItemProcessor implements ItemProcessor<Company, Company> {

	@Override
	public Company process(final Company company) {
		final String fullName = company.getFullName().toLowerCase();
		final String shortName = company.getShortName().toLowerCase();
		final String okvedName = company.getOkvedName().toLowerCase();
		final String site = company.getSite().toLowerCase();

		return new Company(company.getId(), fullName, shortName, company.getINN(), company.getOGRN(),
				company.getOkved(), okvedName, site);
	}

}