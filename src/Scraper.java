/* Programmer: Madison Leyens
 * Date: Fall 2020
 * Description: This is the Scraper Class for Twitter Bot. I am not using this functionality in this project.
 * 
 */

import java.io.IOException;
import java.util.ArrayList;

import com.jaunt.*;
import java.util.regex.Matcher; //Used to search for the pattern
import java.util.regex.Pattern; //Defines a pattern (to be used in a search)

//This class uses the jaunt API to scrape web content and return the resulting text. 
//https://jaunt-api.com
public class Scraper {
	
	UserAgent userAgent; //( an internal 'headless' browser)
	static String GOOGLE_URL = "http://google.com";
	static String SONG_URL = "https://www.azlyrics.com/";
	static String HTTP = "=http"; 

	Scraper() {    

	}
	
	void scrape(String url, String searchTerm) throws JauntException{
	    userAgent = new UserAgent();      //create new userAgent (headless browser)
	    userAgent.settings.autoSaveAsHTML = true;
	    userAgent.visit(url);       //visit 
	    userAgent.doc.apply(searchTerm).submit();     //apply form input (starting at first editable field & submit) 
	    
	    Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
	   					//find search result script
	    for(Element link : links) System.out.println(link.getAt("href"));            //print results -- expand this class here in order to use
/*		Pattern pattern = Pattern.compile("word user passes in", Pattern.CASE_INSENSITIVE);
 * 		Matcher matcher = pattern.matcher
 * */
	}		
	
	void scrapeSongs(String url, String searchTerm) throws JauntException {
		//LOOK AT JAUNT API for how this works
	    userAgent = new UserAgent();      //create new userAgent (headless browser)
	    userAgent.settings.autoSaveAsHTML = true;
	    userAgent.visit(url);       //visit 
	    userAgent.doc.apply(searchTerm).submit();     //apply form input (starting at first editable field & submit)
		 Elements links = userAgent.doc.findEvery("<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->").findEvery("</div>");	
	
		 //return every div
		 //take the text until the /div
	}
	
	//prints the text content of google results -- can modify to save this text
	ArrayList<String> scrapeAZResults(String searchTerm) throws JauntException{
	{
	    userAgent = new UserAgent();      //create new userAgent (headless browser)
	    userAgent.settings.autoSaveAsHTML = true;
//	    userAgent.visit(GOOGLE_URL);       //visit 
	    userAgent.visit(SONG_URL);
	    userAgent.doc.apply(searchTerm).submit();     //apply form input (starting at first editable field & submit) 
	    
//	    Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
	    Elements links = userAgent.doc.findEvery("<a>");   //find search result links 

		
		ArrayList<String> results = new ArrayList();

	    for(Element link : links) 
	    {
	    	//if not statement skip menu href 
	    	String strLink = link.getAt("href"); 
	    	int startIndex = strLink.indexOf(HTTP); 
	    	if(startIndex != -1)
	    	{	    		
	    		int endIndex = strLink.indexOf("&amp;sa=");
	    		UserAgent userAgent2 = new UserAgent();
	    		try {
	    			userAgent2.visit(strLink.substring(startIndex+1, endIndex)); //change this
	    			String body = userAgent2.doc.findFirst("<body>").getTextContent();
	    			results.add(body); 
	    			//write a method to do the string stuff -- index of --> substring 
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