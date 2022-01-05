package port.web.http;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.net.*;
import com.sun.net.httpserver.*;

public class StaticLiveRouter implements HttpHandler {
	private int prefixLength;
	private String rootDirectory;
	private Map<String,String> mimes = new HashMap<String,String>();

	public StaticLiveRouter(String prefix, String root) {
		this.prefixLength = prefix.length();
		this.rootDirectory = root;
		declareMimes();
	}
	
	protected void declareMimes() {
		mimes.put("css", "text/css");
		mimes.put("js", "text/javascript");
		mimes.put("json", "application/json");
	}

	protected String combine(String path1, String path2) {
		File file1 = new File(path1);
		File file2 = new File(file1, path2);
		return file2.getPath();
	}

	protected String getFileExtension(String name) {
		int position = name.lastIndexOf(".");
		if (position == -1)
			return "";
    	return name.substring(position + 1);
	}

	protected String getMimeType(String name) {
		String mime = URLConnection.guessContentTypeFromName(name);
		if (mime == null)
			mime = mimes.get(getFileExtension(name));
		return mime;
	}
	
	protected String getPath(HttpExchange request) {
		String completePath = request.getRequestURI().getPath();
		String localPath = completePath.substring(prefixLength);
		return ("".equals(localPath))
			? "index.html" // default file
			: localPath;
	}

	protected void sendFile(HttpExchange request, InMemoryFile file, int code) {
		Headers h = request.getResponseHeaders();
		h.add("Content-type", file.mime);
		h.add("Connection", "keep-alive");
		try {
			request.sendResponseHeaders(code, file.content.length);
			OutputStream body = request.getResponseBody();
			body.write(file.content);
			body.close();
		} catch (IOException e) { e.printStackTrace(); }
	}

	public void handle(HttpExchange t) throws IOException {
		String localPath = getPath(t);
		String absolutePath = combine(rootDirectory, localPath);
		InMemoryFile reply;
		int code = 200;

		try {
			File file = new File(absolutePath);
			if (!file.exists() || !file.isFile())
				throw new FileNotFoundException();

			String mime = getMimeType(file.getName());

			FileInputStream fileStream = new FileInputStream(file);
			byte rawFile[] = fileStream.readAllBytes();
			fileStream.close();

			reply = new InMemoryFile(mime, rawFile);
			code = 200;
		} catch (FileNotFoundException e) {
			byte[] message = "404 Not Found".getBytes();
			reply = new InMemoryFile("text/plain", message);
			code = 404;
		}

		sendFile(t, reply, code);
	}
}
