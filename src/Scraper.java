/* Programmer: Madison Leyens
 * Date: Fall 2020
 * Description: This is the Scraper Class for Twitter Bot. I am not using this functionality in this project.
 * 
 */

import java.io.IOException;
import java.util.ArrayList;

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
	static String HTTP = "=http";

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
		/*Elements links = userAgent.doc.findEvery(
				"<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->")
				.findEvery("</div>");
		*/
		Form form = userAgent.doc.getForm(0);
		form.setTextField("q", searchTerm);
		form.submit();
		
//		Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>"); // find search result links
		Elements links = userAgent.doc.findEvery("<td class=\"text-left visitedlyr\">").findEvery("<a>");
		for (Element link : links)
			System.out.println(link.getAt("href")); // print results 
		// return every div
		// take the text until the /div
	}

	// prints the text content of google results -- can modify to save this text
	ArrayList<String> scrapeAZResults(String searchTerm) throws JauntException {
		{
//replace all below w scrape songs
			userAgent = new UserAgent(); // create new userAgent (headless browser)
			userAgent.settings.autoSaveAsHTML = true;
//	    	userAgent.visit(GOOGLE_URL);       //visit 
			userAgent.visit(SONG_URL);
			userAgent.doc.apply(searchTerm).submit(); // apply form input (starting at first editable field & submit)

			Form form = userAgent.doc.getForm(0);
			form.setTextField("q", searchTerm);
			form.submit();
			
//			Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
			Elements links = userAgent.doc.findEvery("<td class=\"text-left visitedlyr\">").findEvery("<a>");

			ArrayList<String> results = new ArrayList();
// replace all above w scrape songs except for the printing
			for (Element link : links) {
				// if not statement 
				String strLink = link.getAt("href"); //this gives us the link to go to
				int startIndex = strLink.indexOf(HTTP); 
				if (startIndex != -1) {
					int endIndex = strLink.indexOf("&amp;sa="); 
					UserAgent userAgent2 = new UserAgent();
					try {
						userAgent2.visit(strLink.substring(startIndex + 1, endIndex)); // puts text into the search page
						String body = userAgent2.doc.findFirst("<body>").getTextContent();
						results.add(body);
						//skip links from the nav bar
						//then go to the link -- cleanStrings method will go here

					} catch (Exception e) {
						System.out.println("Encountered an unsupported file type or webpage. Moving on...");
					}
				}
				
			}

			return results;
		}

	}

	void cleanStrings() {// write a method to get the string of everything in the body (index of --> substring of the results you want)
		//STEPS:
		// open the content by using AZ to search
		//if href.equals(any of the links earlier on the page/menu href) else you'll visit it and train so link.getat
			//this if will go in the for (Element link : links) on the next line
			//could also be a if not statement 
		// in body.getChildText take the indexOf(the string)
			//start index = indexOf that copyright thing before the lyrics + the size of that whole string
			//end index = </div>
		//take a substring of the lyrics (everything below) until the <div>
		//put it through the text tokenizer and separate the <br>
		
		
//		try {
//			UserAgent userAgent3 = new UserAgent();
//			userAgent3.visit(strLink.substring(startIndex+1, endIndex)); //change this			
//			Element body = userAgent3.doc.findFirst(<div>).getTextContent();
//			results.add(body); 
//			System.out.println("body's childtext: " + body.getChildText(indexOf(body)));   
//			System.out.println("-----------");
//			System.out.println("all body's text: " + body.getTextContent());
//		} catch (Exception e) {
//			System.out.println("Encountered an unsupported file type or webpage. Moving on...");
//		}
	}
	
	//GOOGLE STUFF
	//prints the text content of google results -- can modify to save this text
	ArrayList<String> scrapeGoogleResults(String searchTerm) throws JauntException{
	{
	    userAgent = new UserAgent();      //create new userAgent (headless browser)
	    userAgent.settings.autoSaveAsHTML = true;
	    userAgent.visit(GOOGLE_URL);       //visit 
	    userAgent.doc.apply(searchTerm).submit();     //apply form input (starting at first editable field & submit) 
	    
//	    Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
	    Elements links = userAgent.doc.findEvery("<a>");   //find search result links 

		
		ArrayList<String> results = new ArrayList();

	    for(Element link : links) 
	    {
	    	String strLink = link.getAt("href"); 
	    	int startIndex = strLink.indexOf(HTTP); 
	    	if(startIndex != -1)
	    	{	    		
	    		int endIndex = strLink.indexOf("&amp;sa=");
	    		UserAgent userAgent2 = new UserAgent();
	    		try {
	    			userAgent2.visit(strLink.substring(startIndex+1, endIndex));
	    			String body = userAgent2.doc.findFirst("<body>").getTextContent();
	    			results.add(body); 
	    		}
	    		catch (Exception e)
	    		{
	    			System.out.println("Encountered an unsupported file type or webpage. Moving on...");
	    		}
	    	}
	    }
	    
		return results; 

	  }		
	}
}
