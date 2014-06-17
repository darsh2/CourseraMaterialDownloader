package coursera.coursematerial.downloader;

public class URLChecker {
	private String URL;
	
	public URLChecker(String URL) {
		this.URL = URL;
	}
	
	public String checker() {
		if (URL.startsWith("https://class.coursera.org/") && URL.contains("lecture"))
			return URL;
		return null;
	}
}
