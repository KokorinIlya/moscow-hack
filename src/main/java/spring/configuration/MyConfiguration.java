package spring.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class MyConfiguration {
//	@Override
//	public void saveBatch(final List<Employee> employeeList) {
//		final int batchSize = 500;
//
//		for (int j = 0; j < employeeList.size(); j += batchSize) {
//
//			final List<Employee> batchList = employeeList.subList(j, j + batchSize > employeeList.size() ? employeeList.size() : j + batchSize);
//
//			getJdbcTemplate().batchUpdate(QUERY_SAVE,
//					new BatchPreparedStatementSetter() {
//						@Override
//						public void setValues(PreparedStatement ps, int i)
//								throws SQLException {
//							Employee employee = batchList.get(i);
//							ps.setString(1, employee.getFirstname());
//							ps.setString(2, employee.getLastname());
//							ps.setString(3, employee.getEmployeeIdOnSourceSystem());
//						}
//
//						@Override
//						public int getBatchSize() {
//							return batchList.size();
//						}
//					});
//
//		}
//	}
}
