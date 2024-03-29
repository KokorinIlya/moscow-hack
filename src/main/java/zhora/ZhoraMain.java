package zhora;

import com.google.gson.Gson;
import okhttp3.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

import static data.ZhoraData.makeTextRequestable;
import static data.CategoriesData1.CATEGORIES_DATA1;
import static data.CategoriesData2.CATEGORIES_DATA2;

public class ZhoraMain {
//	public static void main(String[] args) {
//		parseFromJson(search(ZhoraTexts.EXAMPLE)).getDocuments().get(0).getKeyPhrases();
//	}

	public static void main(String[] args) {
		StringBuilder str = new StringBuilder();
		String[] data = ArrayUtils.addAll(CATEGORIES_DATA1, CATEGORIES_DATA2);
		for (String innov : data) {
			if (str.length() + innov.length() > 5000) {
				System.out.println("str=" + str);
				System.out.println(parseFromJson(search(str.toString())).getDocuments().get(0).getKeyPhrases());
				str = new StringBuilder();
			} else {
				str.append(innov).append(". ");
			}
		}
		if (!str.toString().isEmpty()) {
			System.out.println(parseFromJson(search(str.toString())).getDocuments().get(0).getKeyPhrases());
		}
	}


	public static String search(String cleanedSite) {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, makeTextRequestable(cleanedSite));
		Request request = new Request.Builder()
				.url("https://westeurope.api.cognitive.microsoft.com/text/analytics/v2.0/keyPhrases")
				.post(body)
				.addHeader("ocp-apim-subscription-key", "a7b061147fc046458dfa6172abe05f9e")
				.addHeader("accept", "application/json")
				.addHeader("content-type", "application/json")
				.addHeader("cache-control", "no-cache")
				.addHeader("postman-token", "08f40841-9048-3261-e1f8-ae2a0a5a04ad")
				.build();

		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful()) {
				return response.body() != null ? response.body().string() : "a";
			} else {
				return response.body() != null ? response.body().string() : "aa";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "aaa";
		}
	}

	private static void analyse(String response) {
		if (!"".equals(response)) {

		}
	}

	public static ZhoraResponse parseFromJson(String jsonText) {
		Gson gson = new Gson();
		return gson.fromJson(jsonText, ZhoraResponse.class);
	}
}
