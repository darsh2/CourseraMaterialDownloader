package coursera.coursematerial.downloader;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadLinks {
	private ArrayList<String> titles, lectures;
	
	private Elements links;
	private Document doc;
	
	public DownloadLinks(String URL) {
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getLectureTitles() {
		links = doc.select("a[href]");
		titles = new ArrayList<String>();
		lectures = new ArrayList<String>();
		
		for (Element link : links) {
			String URL = link.attr("href");
			if (URL.contains("download.mp4")) {
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
