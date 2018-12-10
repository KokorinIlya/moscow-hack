package key_phrases_finder;

import aggregatorator.components.Company;
import aggregatorator.components.MyWebPage;
import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchAPI;
import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchManager;
import com.microsoft.azure.cognitiveservices.search.websearch.models.SearchResponse;
import com.microsoft.azure.cognitiveservices.search.websearch.models.WebPage;
import scalacode.InfoGetter;
import zhora.ZhoraMain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static aggregatorator.Main.SUBSCRIPTION_KEY;
import static zhora.ZhoraMain.parseFromJson;

public class Finder {
	private static long lastParsedCompany = 0;

	private static PrintWriter bingWriter;
	private static PrintWriter aggregatorWriter;
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

	public static void main(String[] args) {
		setUpWriters();

		try {
			Scanner sc = new Scanner(Files.newInputStream(Paths.get("src/main/resources/from_bing.ksv")));
			sc.useDelimiter(";; ");
			int i = 0;
			String company = "";
			while (sc.hasNext()) {
				String str = sc.next();
				// domain or "NAME"
				if (i == 0) {
					if (str.equals("NAME")) {
						if (!sc.hasNext()) {
							System.out.println("!!!!!KSV file is wrong");
						}
						company = sc.next();
						i = 0;
						System.out.println("Last parsed company=" + lastParsedCompany++);
						continue;
					}
				}
				// full url
				if (i == 1) {
					if (!str.equals("nan")) {
						zhora(
								InfoGetter.getInfo(str), // download, concat and clean
								company
						);                              // and then get key words
					}
				}
				i++;
				if (i == 5) i = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Can't parse ksv");
		}
		closeWriters();
	}

	private static List<Company> bingSearch(String query) {
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
					return webData.webPages().value().stream().map(Company::new).collect(Collectors.toList());
				} else {
					bingWriter.println("Couldn't find web results!");// TODO: change
					return new ArrayList<>();
				}
			} else {
				bingWriter.println("Didn't see any Web data..");// TODO: change
				return new ArrayList<>();
			}
		} catch (Exception e) {
//			aggregatorWriter.println("NEW DATA");
//			urlData.values().stream().filter((URLData data) -> data.getNumber() >= 2)
//					.sorted(Comparator.comparingInt(urlData1 -> -urlData1.getNumber())).forEach(
//					(URLData data) -> aggregatorWriter.println(data.toString()));
//
//			System.out.println("Last parsed company=" + lastParsedCompany);
//			System.out.println(e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private static void zhora(List<String> data, String company) {
		System.out.println("COMP=" + company);

		StringBuilder str = new StringBuilder();
		for (String innov : data) {
			if (str.length() + innov.length() > 5000) {
				System.out.println("TXT=" + str);
				System.out.println(parseFromJson(ZhoraMain.search(str.toString())).getDocuments().get(0).getKeyPhrases());
				str = new StringBuilder();
			} else {
				str.append(innov).append(". ");
			}
		}
		if (!str.toString().isEmpty()) {
			System.out.println(parseFromJson(ZhoraMain.search(str.toString())).getDocuments().get(0).getKeyPhrases());
		}
	}
}
