//package db.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.Set;
//
//@Entity
//@Table(name = "companies", schema = "public")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@ToString(of = {"id", "author", "title", "creationDate", "alteringDate", "content", "contentPreview"})
//@Getter
//@Setter
//public class Companies implements Serializable {
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_id_seq")
//	@SequenceGenerator(name = "news_id_seq", sequenceName = "news_id_seq", allocationSize = 1)
//	@Column(name = "id")
//	private Long id;
//
//	@ManyToOne
//	@JoinColumn(name = "id_user", nullable = false)
//	@JsonManagedReference
//	private User author;
//
//	@Column(name = "title", nullable = false)
//	private String title;
//
//	@Column(name = "creation_date", nullable = false)
//	private Timestamp creationDate;
//
//	@Column(name = "altering_date")
//	private Timestamp alteringDate;
//
//	@Column(name = "content", nullable = false)
//	private String content;
//
//	@Column(name = "content_preview", nullable = false)
//	private String contentPreview;
//
//	@OneToMany(mappedBy="news", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
//	@JsonBackReference
////	@Column(nullable = false) // TODO: nullable for columns, fetch type, cascade
//	private Set<Comment> comments;
//
//	@OneToMany(mappedBy="news", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
//	@JsonBackReference
//	private Set<NewsLoop> newsLoops;
//
//	@OneToMany(mappedBy="news", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
//	@JsonBackReference
//	private Set<NewsPoop> newsPoops;
//
////	@OneToMany(mappedBy="news", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
////	@JsonBackReference
////	private Set<Repost> reposts;
//}
