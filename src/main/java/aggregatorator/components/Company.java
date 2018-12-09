package aggregatorator.components;

import com.microsoft.azure.cognitiveservices.search.websearch.models.WebPage;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Company {
	private static final int LAST_TIME_CRAWLED = 100; // e.x. 100 days TODO: check format

	private String fullName;
	private String shortName;
	private String ITN;
	private String url;
	private String dateLastCrawled;
	// private ImageObject image; TODO: add image

	public Company(WebPage page) {
		this.url = page.url();
		this.dateLastCrawled = page.dateLastCrawled();

		//TODO: ANALYSER to eject names, ITN ...
	}
}
