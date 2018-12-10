package spring.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "INN", "shortName"})
public class Company implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public Company(Long id, String fullName, String shortName, Long INN, Long OGRN, String okved, String okvedName, String site) {
		this.id = id;
		this.fullName = fullName;
		this.shortName = shortName;
		this.INN = INN;
		this.OGRN = OGRN;
		this.okved = okved;
		this.okvedName = okvedName;
		this.site = site;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	@JoinTable(name = "company_category", joinColumns = { @JoinColumn(name = "company_id") },
			inverseJoinColumns = { @JoinColumn(name = "category_id") })
	@JoinColumn(nullable = false)
	@JsonManagedReference
	private Set<Category> categorySet = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	@JoinTable(name = "company_innovation", joinColumns = { @JoinColumn(name = "company_id") },
			inverseJoinColumns = { @JoinColumn(name = "innovation_id") })
	@JoinColumn(nullable = false)
	@JsonManagedReference
	private Set<Innovation> innovationSet = new HashSet<>();


	public void addCategory(Category category) {
		categorySet.add(category);
		category.getCompanySet().add(this);
	}

	public void addInnovation(Innovation innovation) {
		innovationSet.add(innovation);
		innovation.getCompanySet().add(this);
	}
}
