package spring.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "INN", "name"})
public class Company implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String fullName;

	@Column
	private String shortName;

	@Column
	private Long INN;

	@Column
	private Long OGRN;

	@Column
	private String okved;

	@Column
	private String okvedName;

	@Column
	private String site;

	public Company(String fullName, String shortName, Long INN, Long OGRN, String okved, String okvedName, String site, Category category, Innovation innovation) {
		this.fullName = fullName;
		this.shortName = shortName;
		this.INN = INN;
		this.OGRN = OGRN;
		this.okved = okved;
		this.okvedName = okvedName;
		this.site = site;
		this.category = category;
		this.innovation = innovation;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	@JsonManagedReference
	private Category category;

	@ManyToOne
	@JoinColumn(nullable = false)
	@JsonManagedReference
	private Innovation innovation;
}
