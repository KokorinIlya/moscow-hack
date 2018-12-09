package aggregatorator;

import aggregatorator.components.*;
import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchAPI;
import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchManager;
import com.microsoft.azure.cognitiveservices.search.websearch.models.SearchResponse;
import com.microsoft.azure.cognitiveservices.search.websearch.models.WebPage;


import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
	public static final String SUBSCRIPTION_KEY = "7973e4b8cd2b4ccbb3033451b77ca262";

//	// Enter a valid subscription key.
//	static String SUBSCRIPTION_KEY = "1bae017978464fbc85cb7bad185e5c0c";
//
//	/*
//	 * If you encounter unexpected authorization errors, double-check these values
//	 * against the endpoint for your Bing Web search instance in your Azure
//	 * dashboard.
//	 */
//	static String host = "https://api.cognitive.microsoft.com";
//	static String path = "/bing/v7.0/search";
//	static String searchTerm = "Microsoft Cognitive Services";

	private static HashMap<String, URLData> urlData = new HashMap<>();
	public static PrintWriter bingWriter;
	public static PrintWriter aggregatorWriter;
	private static long lastParsedCompany = 0;

	public static void main(String[] args) {
    	setUpWriters();
//		// Confirm the SUBSCRIPTION_KEY is valid.
//		if (SUBSCRIPTION_KEY.length() != 32) {
//			System.out.println("Invalid Bing Search API subscription key!");
//			System.out.println("Please paste yours into the source code.");
//			System.exit(1);
//		}
//
//		// Call the SearchWeb method and print the response.
//		try {
//			components.SearchResults result = searchWeb(searchTerm);
//			JsonParser parser = new JsonParser();
//			JsonObject json = parser.parse(result.getJsonResponse()).getAsJsonObject();
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		}
//		catch (Exception e) {
//			e.printStackTrace(System.out);
//			System.exit(1);
//		}
		try {
			parseKML();
		} catch (Exception e) {
			System.err.println("Can't parse xml:");
			e.printStackTrace();
			// TODO
			return;
		}
		aggregatorWriter.println("NEW DATA");
		urlData.values().stream().filter((URLData data) -> data.getNumber() >= 2)
				.sorted(Comparator.comparingInt(urlData1 -> -urlData1.getNumber())).forEach(
						(URLData data) -> aggregatorWriter.println(data.toString()));
		closeWriters();
//		try {
//			// Enter a valid subscription key for your account.
//			// Instantiate the client.
//			// Make a call to the Bing Web Search API.
//			search();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
	}

	private static void setUpWriters() {
		try {
			FileWriter fw = new FileWriter("bing.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bingWriter = new PrintWriter(bw);
			bingWriter.println("NEW DATA");
		} catch (IOException e) {
			System.err.println("Can't write to file bing.log");
		}

		try {
			FileWriter fw1 = new FileWriter("aggregator.log", true);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			aggregatorWriter = new PrintWriter(bw1);
			aggregatorWriter.println("NEW DATA");
		} catch (IOException e) {
			System.err.println("Can't write to file aggregator.log");
		}
	}

	private static void closeWriters() {
		bingWriter.close();
		aggregatorWriter.close();
	}

	private static void parseXML() throws Exception {
		try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(Paths.get(
				"/Users/macbook/Documents/IdeaProjects/gson/src/main/resources/dataset_to_parse_short.xml")))) {
			XMLStreamReader reader = processor.getReader();
			int counter = 0;

			//0  - SparkID
			//1  - Полное наименование
			//2  - Краткое наименование
			//3  - Наименование на англ.
			//4  - ИНН
			//5  - КПП
			//6  - ОГРН
			//7  - Дата регистрации
			//8  - Генеральный директор
			//9  - Юридический адрес
			//10 - Сайт
			//11 - EMAIL
			//12 - Телефоны
			//13 - ОКВЭД_Код
			//14 - ОКВЭД_Наименование
			//15 - тип МСП: 1-ЮЛ, 2-ИП
			//16 - категория МСП
			//17 - ПП
			List<String> row = new ArrayList<>(18);
			while (reader.hasNext()) {       // while not end of XML
				int event = reader.next();   // read next event
				if (event == XMLEvent.START_ELEMENT && "Data".equals(reader.getLocalName())) {
					counter++;
					if (counter == 18) {
						search(row.get(2));
						row.clear();
						counter = 0;
						lastParsedCompany++;
					} else {
						row.add(reader.getElementText());
					}
				}
			}
		}
	}

	private static void parseKML() throws Exception {
		Scanner sc = new Scanner(Files.newInputStream(Paths.get("src/main/resources/dataset.ksv")));
		sc.useDelimiter(";; ");
		while (sc.hasNext()) {
			for (int i = 0; i < 7; i++) {
				if (i == 1) {
					String shortName = sc.next();
					if (!shortName.equals("nan")) {
						search(shortName);
					}
					lastParsedCompany++;
				} else {
					sc.next();
				}
			}
		}
	}

	/*
	 *
	 * Performs a search based on the .withQuery and prints the name and
	 * url for the first web pages, image, news, and video result
	 * included in the response.
	 *
	 * @param client instance of the Bing Web Search API client
	 * @return true if sample runs successfully
	 */
	private static void search(String query) {
		try {
			bingWriter.println("Site=" + query);
			BingWebSearchAPI client = BingWebSearchManager.authenticate(SUBSCRIPTION_KEY);
			// Construct the request.
			SearchResponse webData = client.bingWebs().search()
					.withQuery(query)// TODO: change
					.withMarket("ru-RU")
					.withCount(30)
					.execute();
			if (webData != null && webData.webPages() != null && webData.webPages().value() != null &&
					webData.webPages().value().size() > 0) {
				// find the first web page
				WebPage firstWebPagesResult = webData.webPages().value().get(0);

				if (firstWebPagesResult != null) {
					webData.webPages().value().forEach((WebPage page) -> bingWriter.println("NEXT PAGE\n" + (new MyWebPage(page)).toString()));
					analyse(webData);
				} else {
					bingWriter.println("Couldn't find web results!");// TODO: change
				}
			} else {
				bingWriter.println("Didn't see any Web data..");// TODO: change
			}
		} catch (Exception e) {
			aggregatorWriter.println("NEW DATA");
			urlData.values().stream().filter((URLData data) -> data.getNumber() >= 2)
					.sorted(Comparator.comparingInt(urlData1 -> -urlData1.getNumber())).forEach(
					(URLData data) -> aggregatorWriter.println(data.toString()));

			System.out.println("Last parsed company=" + lastParsedCompany);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void analyse(SearchResponse webData) {
		//
		List<Company> companies = webData.webPages().value().stream().map(Company::new).collect(Collectors.toList());
		for (int i = 0; i < companies.size(); i++) {
			Company company = companies.get(i);
			String host = URI.create(company.getUrl()).getHost();
			if (urlData.containsKey(host)) {
				URLData data = urlData.get(host);
				data.changeAveragePosition(i + 1);
				data.incrementNumber();
				urlData.put(host, data);
			} else {
				urlData.put(host, new URLData(host));
			}
		}
	}

//	public static components.SearchResults searchWeb (String searchQuery) throws Exception {
//		// Construct the URL.
//		URL url = new URL(host + path + "?q=" +  URLEncoder.encode(searchQuery, "UTF-8"));
//
//		// Open the connection.
//		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
//		connection.setRequestProperty("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY);
//
//		// Receive the JSON response body.
//		InputStream stream = connection.getInputStream();
//		String response = new Scanner(stream).useDelimiter("\\A").next();
//
//		// Construct the result object.
//		components.SearchResults results = new components.SearchResults(new HashMap<String, String>(), response);
//
//		// Extract Bing-related HTTP headers. TODO: ????
//		Map<String, List<String>> headers = connection.getHeaderFields();
//		for (String header : headers.keySet()) {
//			if (header == null) continue;      // may have null key
//			if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")){
//				results.relevantHeaders.put(header, headers.get(header).get(0));
//			}
//		}
//		stream.close();
//		return results;
//	}

}
