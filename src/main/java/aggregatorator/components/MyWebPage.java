package aggregatorator.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.cognitiveservices.search.websearch.models.ImageObject;
import com.microsoft.azure.cognitiveservices.search.websearch.models.Thing;
import com.microsoft.azure.cognitiveservices.search.websearch.models.WebMetaTag;
import com.microsoft.azure.cognitiveservices.search.websearch.models.WebPage;

import java.net.URI;
import java.util.List;

public class MyWebPage extends WebPage {
	/**
	 * The display URL of the webpage. The URL is meant for display purposes
	 * only and is not well formed.
	 */
	@JsonProperty(value = "displayUrl", access = JsonProperty.Access.WRITE_ONLY)
	private String displayUrl;

	/**
	 * A snippet of text from the webpage that describes its contents.
	 */
	@JsonProperty(value = "snippet", access = JsonProperty.Access.WRITE_ONLY)
	private String snippet;

	/**
	 * A list of links to related content that Bing found in the website that
	 * contains this webpage. The Webpage object in this context includes only
	 * the name, url, urlPingSuffix, and snippet fields.
	 */
	@JsonProperty(value = "deepLinks", access = JsonProperty.Access.WRITE_ONLY)
	private List<WebPage> deepLinks;

	/**
	 * The last time that Bing crawled the webpage. The date is in the form,
	 * YYYY-MM-DDTHH:MM:SS. For example, 2015-04-13T05:23:39.
	 */
	@JsonProperty(value = "dateLastCrawled", access = JsonProperty.Access.WRITE_ONLY)
	private String dateLastCrawled;

	/**
	 * A list of search tags that the webpage owner specified on the webpage.
	 * The API returns only indexed search tags. The name field of the MetaTag
	 * object contains the indexed search tag. Search tags begin with search.*
	 * (for example, search.assetId). The content field contains the tag's
	 * value.
	 */
	@JsonProperty(value = "searchTags", access = JsonProperty.Access.WRITE_ONLY)
	private List<WebMetaTag> searchTags;

	/**
	 * The primaryImageOfPage property.
	 */
	@JsonProperty(value = "primaryImageOfPage", access = JsonProperty.Access.WRITE_ONLY)
	private ImageObject primaryImageOfPage;

	/**
	 * The URL to a thumbnail of the item.
	 */
	@JsonProperty(value = "thumbnailUrl", access = JsonProperty.Access.WRITE_ONLY)
	private String thumbnailUrl;

	/**
	 * The source of the creative work.
	 */
	@JsonProperty(value = "provider", access = JsonProperty.Access.WRITE_ONLY)
	private List<Thing> provider;

	/**
	 * The text property.
	 */
	@JsonProperty(value = "text", access = JsonProperty.Access.WRITE_ONLY)
	private String text;

	/**
	 * The name of the thing represented by this object.
	 */
	@JsonProperty(value = "name", access = JsonProperty.Access.WRITE_ONLY)
	private String name;

	/**
	 * The URL to get more information about the thing represented by this
	 * object.
	 */
	@JsonProperty(value = "url", access = JsonProperty.Access.WRITE_ONLY)
	private String url;

	/**
	 * The image property.
	 */
	@JsonProperty(value = "image", access = JsonProperty.Access.WRITE_ONLY)
	private ImageObject image;

	/**
	 * A short description of the item.
	 */
	@JsonProperty(value = "description", access = JsonProperty.Access.WRITE_ONLY)
	private String description;

	/**
	 * An ID that uniquely identifies this item.
	 */
	@JsonProperty(value = "bingId", access = JsonProperty.Access.WRITE_ONLY)
	private String bingId;

	/**
	 * The URL To Bing's search result for this item.
	 */
	@JsonProperty(value = "webSearchUrl", access = JsonProperty.Access.WRITE_ONLY)
	private String webSearchUrl;

	/**
	 * A String identifier.
	 */
	@JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
	private String id;

	public MyWebPage(WebPage webPage) {
		this.displayUrl = webPage.displayUrl();
		this.snippet = webPage.snippet();
		this.deepLinks = webPage.deepLinks();
		this.dateLastCrawled = webPage.dateLastCrawled();
		this.searchTags = webPage.searchTags();
		this.primaryImageOfPage = webPage.primaryImageOfPage();
		this.thumbnailUrl = webPage.thumbnailUrl();
		this.provider = webPage.provider();
		this.text = webPage.text();
		this.name = webPage.name();
		this.url = webPage.url();
		this.image = webPage.image();
		this.description = webPage.description();
		this.bingId = webPage.bingId();
		this.webSearchUrl = webPage.webSearchUrl();
		this.id = webPage.id();
	}

	public String toString() {
		String sprtr = ";; ";
		return URI.create(url).getHost() + sprtr +
				url + sprtr +
				name + sprtr +
				snippet + sprtr +
				dateLastCrawled + sprtr;
	}
}
