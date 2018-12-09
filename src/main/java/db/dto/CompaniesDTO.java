//package db.dto;
//
//import db.entity.Companies;
//import lombok.Getter;
//import lombok.ToString;
//import org.springframework.hateoas.core.Relation;
//
//import java.sql.Timestamp;
//
//@Getter
//@Relation(value = "news", collectionRelation = "news")
//@ToString
//public class CompaniesDTO {
//	private Long id;
//	private UserDTO author;
//	private String title;
//	private String content;
//	private Timestamp creationDate;
//	private Long commentsNumber;
//	private Long loopsNumber;
//	private Boolean loopWasPut;
//	private Long poopsNumber;
//	private Boolean poopWasPut;
//
//	public CompaniesDTO(){}
//
//	public CompaniesDTO(Companies companies, Long commentsNumber, Long loopsNumber, boolean loopWasPut, Long poopsNumber, boolean poopWasPut) {
//		this.id = companies.getId();
//		this.author = new UserDTO(companies.getAuthor());
//		this.title = companies.getTitle();
//		this.content = companies.getContent();
//		this.creationDate = companies.getCreationDate();
//		this.commentsNumber = commentsNumber;
//		this.loopsNumber = loopsNumber;
//		this.loopWasPut = loopWasPut;
//		this.poopsNumber = poopsNumber;
//		this.poopWasPut = poopWasPut;
//	}
//}
