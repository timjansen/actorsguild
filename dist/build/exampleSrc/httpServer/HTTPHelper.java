package httpServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Contains primitive helper methods for HTTP.
 */
public class HTTPHelper {

	/**
	 * Reads the HTTP header and returns the requested path.
	 * @param input the input stream to read from
	 * @return the path, or null if header not readable
	 * @throws IOException on read error
	 */
	static String readHeader(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
		
		// path first line ("COMMAND <path> HTTP/1.x") quick&dirty 
		String l = reader.readLine();
		if (l == null)
			return null;
		String path = l.replaceFirst("^\\w+\\s+", "").replaceFirst("\\s+\\S+$", "");
		
		// read rest of the header
		while (((l = reader.readLine()) != null) && (l.length() > 0))
			;
		return path;
	}

	/**
	 * Writes the specified error to the given OutputStream.
	 * @param code the HTTP status code
	 * @param message the message for the body
	 * @param output the stream to write to
	 * @throws IOException if writing failed
	 */
	static void writeError(int code, String message, OutputStream output) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
		bw.write(String.format("HTTP/1.0 %d no-phrase\r\n", code));
		bw.write("Content-Type: text/plain\r\n\r\n");
		bw.write(message);
		bw.write('\n');
		bw.flush();
	}

	/**
	 * Writes the specified content as HTTP response to the output stream.
	 * @param contentType the HTTP content type for the header
	 * @param body the response body as text
	 * @param output the stream to write to
	 * @throws IOException if writing failed
	 */
	static void writeContent(String contentType, String body, OutputStream output) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII")));
		bw.write("HTTP/1.0 200 OK\r\n");
		bw.write(String.format("Content-Type: %s\r\n\r\n", contentType));
		bw.write(body);
		bw.write('\n');
		bw.flush();
	}

}
