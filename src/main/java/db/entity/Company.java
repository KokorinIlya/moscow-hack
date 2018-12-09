package db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "companies", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(of = {"INN", "name"})
@Getter
@Setter
public class Company implements Serializable {
	@Id
	@Column(name = "INN")
	private Long INN;

	@Column(name = "name", nullable = false)
	private String name;
}
