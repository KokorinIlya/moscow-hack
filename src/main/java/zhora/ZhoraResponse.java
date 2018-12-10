package zhora;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@ToString
public class ZhoraResponse {
	@AllArgsConstructor
	@Getter
	@ToString
	public class ZhoraDocument {
		private final Long id;
		private final ArrayList<String> keyPhrases;
	}
	private final ArrayList<ZhoraDocument> documents;
	private final ArrayList<String> errors;
}
