/* Programmer: Madison Leyens
 * Date: Fall 2020
 * Description: This is the Scraper Class for Twitter Bot. I am not using this functionality in this project.
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jaunt.*;
import com.jaunt.component.Form;

import java.util.regex.Matcher; //Used to search for the pattern
import java.util.regex.Pattern; //Defines a pattern (to be used in a search)

//This class uses the jaunt API to scrape web content and return the resulting text. 
//https://jaunt-api.com
public class Scraper {

	UserAgent userAgent; // ( an internal 'headless' browser)
	static String GOOGLE_URL = "http://google.com";
	static String SONG_URL = "https://search.azlyrics.com/search.php";
	static String HTTP = "http";

	Scraper() {

	}

	void scrape(String url, String searchTerm) throws JauntException {
		userAgent = new UserAgent(); // create new userAgent (headless browser)
		userAgent.settings.autoSaveAsHTML = true;
		userAgent.visit(url); // visit
		userAgent.doc.apply(searchTerm).submit(); // apply form input (starting at first editable field & submit)

		Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>"); // find search result links
		// find search result script
		for (Element link : links)
			System.out.println(link.getAt("href")); // print results -- expand this class here in order to use

	}

	void scrapeSongs(String url, String searchTerm) throws JauntException {
		userAgent = new UserAgent(); // create new userAgent (headless browser)
		userAgent.settings.autoSaveAsHTML = true;
		userAgent.visit(url); // visit
//		userAgent.doc.apply(searchTerm).submit(); // apply form input (starting at first editable field & submit)

		Form form = userAgent.doc.getForm(0);
		form.setTextField("q", searchTerm);
		form.submit();

		Elements links = userAgent.doc.findEvery("<td class=\"text-left visitedlyr\">").findEvery("<a>"); // find search
																											// result
																											// links

		// return every thing from <div> to </div> and print results
		// ATTEMPT 1
		String testResults = userAgent.doc.findEvery("<div>").getTextContent();
		// ATTEMPT 2
//		String testResults = userAgent.doc.findFirst("<div class=\"lyricsh\">").findEach("<br>").getChildText();
		// ATTEMPT 3
//		String testResults =  userAgent.doc.findFirst("<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->").findEach("<br>").getTextContent();

//		System.out.println(testResults);

		for (Element link : links)
			System.out.println(link.getAt("href")); // prints the links that are search results

	}

	// prints the text content of google results -- can modify to save this text
	ArrayList<String> scrapeAZResults(String searchTerm) throws JauntException {
		{

			userAgent = new UserAgent(); // create new userAgent (headless browser)
			userAgent.settings.autoSaveAsHTML = true;
//	    	userAgent.visit(GOOGLE_URL);       //visit 
			userAgent.visit(SONG_URL);
//			userAgent.doc.apply(searchTerm).submit(); // apply form input (starting at first editable field & submit)

			Form form = userAgent.doc.getForm(0);
			form.setTextField("q", searchTerm);
			form.submit();

//			Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
			Elements links = userAgent.doc.findEvery("<td class=\"text-left visitedlyr\">").findEvery("<a>");
			ArrayList<String> results = new ArrayList();

			for (Element link : links) {
//				if (href.equals(any of the links earlier on the page/menu)) { //could also be a if not statement
//				if (link != searchTerm)) {
//					//skip links from the nav bar
//				} else { //visit and train 				
				String strLink = link.getAt("href"); // this gives us the link to go to
//				int startIndex = strLink.indexOf(HTTP);
//				if (startIndex != -1) {
//					int endIndex = strLink.indexOf("&amp;sa=");
				UserAgent userAgent2 = new UserAgent();
				try {
//						userAgent2.visit(strLink.substring(startIndex + 1, endIndex)); // puts text into the search page
					userAgent2.visit(strLink);
					String body = userAgent2.doc.findFirst("<body>").innerHTML();
					results.add(body);

				} catch (Exception e) {
					System.out.println("Encountered an unsupported file type or webpage. Moving on...");
				}
//				}

			}

			results = cleanStrings(results);
			System.out.println(results);
			return results;
		}
//		} 
	}

	ArrayList<String> cleanStrings(ArrayList<String> lyricResults) {// write a method to get the string of everything in
																	// the body (index of -->
		// substring of the results you want)
	
		ArrayList<String> newResults = new ArrayList<String>();
		String disclaimerText = "<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->";
		for (int i = 0; i < lyricResults.size(); i++) {
			String res = lyricResults.get(i);
			int startIndex = res.indexOf(disclaimerText);
			res = res.substring(startIndex, res.length());
			int endIndex = res.indexOf("</div>");
			res = res.substring(0, endIndex);
			newResults.add(res);
		}
		return newResults;

	}

	private Object indexOf(String cleanBody) {
		// TODO Auto-generated method stub
		return null;
	}

	// GOOGLE STUFF
	// prints the text content of google results -- can modify to save this text
	ArrayList<String> scrapeGoogleResults(String searchTerm) throws JauntException {
		{
			userAgent = new UserAgent(); // create new userAgent (headless browser)
			userAgent.settings.autoSaveAsHTML = true;
			userAgent.visit(GOOGLE_URL); // visit
			userAgent.doc.apply(searchTerm).submit(); // apply form input (starting at first editable field & submit)

//	    Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
			Elements links = userAgent.doc.findEvery("<a>"); // find search result links

			ArrayList<String> results = new ArrayList();

			for (Element link : links) {
				String strLink = link.getAt("href");
				int startIndex = strLink.indexOf(HTTP);
				if (startIndex != -1) {
					int endIndex = strLink.indexOf("&amp;sa=");
					UserAgent userAgent2 = new UserAgent();
					try {
						userAgent2.visit(strLink.substring(startIndex + 1, endIndex));
						String body = userAgent2.doc.findFirst("<body>").getTextContent();
						results.add(body);
					} catch (Exception e) {
						System.out.println("Encountered an unsupported file type or webpage. Moving on...");
					}
				}
			}

			return results;

		}
	}
}
