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
	private ArrayList<String> titles, lectures, textSubtitles, srtSubtitles, pdf, ppt;

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
		titles = new ArrayList<String>();
		lectures = new ArrayList<String>(); textSubtitles = new ArrayList<>(); srtSubtitles = new ArrayList<>();
		pdf = new ArrayList<>(); ppt = new ArrayList<>();

		String format = "%d - %d - ";
		int weekCount = 0;
		Elements weekHeaders = doc.select(".course-item-list-section-list");
		for (Element weekHeader : weekHeaders) {
			weekCount++;
			Elements weekLectures = weekHeader.children();
			int weeklyLectureCount = 0;
			for (Element lecture : weekLectures) {
				weeklyLectureCount++;
				titles.add(String.format(format, weekCount, weeklyLectureCount) + lecture.select(".lecture-link").text());
				Elements resources = lecture.select(".course-lecture-item-resource").select("a[href]");
				boolean flag1 = false, flag2 = false;
				for (Element resource : resources) { 
					String URL = resource.attr("href");
					if (URL.contains("download.mp4"))
						lectures.add(URL);
					else if (URL.contains("format=txt"))
						textSubtitles.add(URL);
					else if (URL.contains("format=srt"))
						srtSubtitles.add(URL);
					else if (URL.contains("pdf")) {
						pdf.add(URL);
						flag1 = true;
					} else if (URL.contains("pptx")) {
						ppt.add(URL);
						flag2 = true;
					}
				}
				if (!flag1)
					pdf.add(null);
				if (!flag2)
					ppt.add(null);
			}
		}
		return titles;
	}

	public ArrayList<String> getLectures() {
		return lectures;
	}
	
	public ArrayList<String> getTXT() {
		return textSubtitles;
	}
	
	public ArrayList<String> getSRT() {
		return srtSubtitles;
	}
	
	public ArrayList<String> getPDF() {
		return pdf;
	}
	
	public ArrayList<String> getPPT() {
		return ppt;
	}
}