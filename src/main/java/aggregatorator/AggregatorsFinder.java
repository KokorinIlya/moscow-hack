package aggregatorator;

import aggregatorator.components.Site;
import aggregatorator.components.SiteCounter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class AggregatorsFinder {
	public static long MIN_AGGREGATOR_REPETITION_NUMBER = 3;

	HashMap<String, SiteCounter> data;

	public void addSite(Site site) {
		SiteCounter siteCounter = data.get(site.getUrl());
		if (siteCounter != null) {
			siteCounter.incrementNumber();
			if (isAggregator(siteCounter)) {
				// TODO: add to DB
				data.remove(site.getUrl());
			}
		} else {
			data.put(site.getUrl(), new SiteCounter(site, 1));
		}
	}

	private boolean isAggregator(SiteCounter siteCounter) {
		return siteCounter.getNumber() >= MIN_AGGREGATOR_REPETITION_NUMBER;
	}
}
