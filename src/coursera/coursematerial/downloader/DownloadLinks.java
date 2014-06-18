package coursera.coursematerial.downloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadLinks {
	private ArrayList<String> titles, lectures;

	private Elements links;
	private Document doc;
	
	public DownloadLinks(String URL, String cookie) {
		try {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Cookie", cookie);
			connection.connect();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line = br.readLine(), crawler = "";
			while (line != null) {
				crawler += line;
				line = br.readLine();
			}
			
			doc = Jsoup.parse(crawler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getLectureTitles() {
		links = doc.select("a[href]");
		titles = new ArrayList<String>();
		lectures = new ArrayList<String>();
		for (Element link : links) {
			System.out.println(link);
			String URL = link.attr("href");
			if (URL.contains("download.mp4")) {
				System.out.println(link.text() + " " + URL);
				titles.add(link.text());
				lectures.add(URL);
			}
		}
		return titles;
	}

	public ArrayList<String> getLectures() {
		return lectures;
	}
}