package test.cache;

import web.api.cache.HtmlCache;
import web.api.cache.key.HtmlCacheKey;

public class TestHtmlCache {

	public static void main(String[] args) {
		HtmlCache cache = HtmlCache.getInstance();
		String html = cache.get(new HtmlCacheKey("BidHtml"));
		
		System.out.println(html);
		System.out.println(cache.getHitRatio());
		cache.get(new HtmlCacheKey("BidHtml"));
		System.out.println(cache.getHitRatio());
		cache.get(new HtmlCacheKey("BidHtml"));
		System.out.println(cache.getHitRatio());
		cache.get(new HtmlCacheKey("BidHtml"));
		System.out.println(cache.getHitRatio());
	}
}
