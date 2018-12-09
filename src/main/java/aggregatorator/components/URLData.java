package aggregatorator.components;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
public class URLData {
	private String url;
	private int number = 0;
	private double averagePosition = 0.0;

	public URLData(String url) {
		this.url = url;
	}

	public void incrementNumber() {
		number++;
	}

	// USE ONLY BEFORE incrementNumber function !!!
	public void changeAveragePosition(int position) {
		averagePosition = (averagePosition * number + position) / (number + 1);
	}

	public String toString() {
		String sprtr = ";; ";
		return URI.create(url).getHost() + sprtr + url + sprtr + number + sprtr + averagePosition + sprtr;
	}
}
