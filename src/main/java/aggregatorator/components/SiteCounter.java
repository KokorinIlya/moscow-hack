package aggregatorator.components;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SiteCounter {
	private Site site;
	private long number;

	public void incrementNumber() {
		number++;
	}
}
