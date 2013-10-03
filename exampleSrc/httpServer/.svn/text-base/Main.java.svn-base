package httpServer;

import java.util.HashMap;

import org.actorsguildframework.DefaultAgent;
import org.actorsguildframework.Props;
import org.actorsguildframework.immutable.ImmutableMap;


/**
 * Main class for the HTTP server example. Creates a HTTPServer instance and
 * initializes it with 4 simple HTML pages to serve.
 * 
 * To test the example, open your HTML browser at http://localhost:8000/
 * 
 */
public class Main {
	/**
	 * Main method for the example
	 * @param args not used
	 * @throws Exception 
	 */
	public static void main(String args[]) throws Exception {
		DefaultAgent a = new DefaultAgent();
		
		HashMap<String, String> myPages = new HashMap<String, String>();;
		String htmlTemplate = "<html><head><title>ActorsGuild HTTP Server</title></head><body>%s</body></html>";
		myPages.put("/", String.format(htmlTemplate, "<h1>Index page</h1><ul><li><a href='/p1'>Page 1</a></li><li><a href='/p2'>Page 2</a></li><li><a href='/p3'>Page 3</a></li></ul>"));
		myPages.put("/p1", String.format(htmlTemplate, "<h1>Page 1</h1><a href='/'>back to index</a>"));
		myPages.put("/p2", String.format(htmlTemplate, "<h1>Page 2</h1><a href='/'>back to index</a>"));
		myPages.put("/p3", String.format(htmlTemplate, "<h1>Page 3</h1><a href='/'>back to index</a>"));
		
		HTTPServer hs = a.create(HTTPServer.class, new Props("htmlPages", new ImmutableMap<String, String>(myPages)));
		hs.listen(8000).get();
	}
}
