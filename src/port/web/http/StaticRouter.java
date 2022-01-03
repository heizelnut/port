package port.web.http;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.net.*;
import com.sun.net.httpserver.*;

public class StaticRouter implements HttpHandler {
	private ArrayList<Resource> resources = new ArrayList<Resource>();
	private int prefixLength;
	private String rootDirectory;
	private Map<String,String> mimes = new HashMap<String,String>();

	public StaticRouter(String prefix, String root) {
		this.prefixLength = prefix.length();
		this.rootDirectory = root;

		mimes.put("css", "text/css");
		mimes.put("js", "text/javascript");
		mimes.put("json", "application/json");
	}

	private String combine(String path1, String path2) {
		File file1 = new File(path1);
		File file2 = new File(file1, path2);
		return file2.getPath();
	}

	private String fileExtension(File file) {
		String name = file.getName();
		int position = name.lastIndexOf(".");
		if (position == -1)
			return "";
    	return name.substring(position + 1);
	}

	private String findMimeType(File file) {
		String mime= URLConnection.guessContentTypeFromName(file.getName());
		if (mime == null)
			mime = mimes.get(fileExtension(file));
		return mime;
	}

	public void handle(HttpExchange t) throws IOException {
		String localPath = t.getRequestURI().getPath();
		localPath = localPath.substring(prefixLength);
		localPath = ("".equals(localPath) || "/".equals(localPath)) ? "index.html" : localPath;
		String absolutePath = combine(rootDirectory, localPath);
		String mimeType = "";
		File resource;
		long size = 0;
		try {
			resource = new File(absolutePath);
			if (!resource.isFile() || !resource.exists())
				throw new FileNotFoundException();
    		mimeType = findMimeType(resource);
			size = resource.length();

			Headers h = t.getResponseHeaders();
			h.add("Content-type", mimeType);
			h.add("Connection", "keep-alive");
			t.sendResponseHeaders(200, size);
			FileInputStream fileStream = new FileInputStream(resource);
			OutputStream body = t.getResponseBody();
			fileStream.transferTo(body);
			fileStream.close();
			body.close();
		} catch (FileNotFoundException e) {
			String message = "404 Not Found";
			t.getResponseHeaders().add("Content-Type", "text/plain");
			t.sendResponseHeaders(404, message.length());
			OutputStream body = t.getResponseBody();
			body.write(message.getBytes());
			body.close();
		}
	}
}
