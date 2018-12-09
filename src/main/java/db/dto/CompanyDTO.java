package db.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CompanyDTO {
	private Long INN;
	private String name;

	public CompanyDTO(){}
}
