package coursera.coursematerial.downloader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadExecutor {
	public int executeDownload(ArrayList<Resource> toDownload, ArrayList<String> titles, 
			String filePath, String cookie) {
		int l = toDownload.size();
		System.out.println(titles.size());
		try {
			ExecutorService executor = Executors.newFixedThreadPool(l < 4 ? l : 4);
			for (Resource resource : toDownload) {
				String URL = resource.getLink(), fileType;
				if (URL.contains("txt"))
					fileType = ".txt";
				else if (URL.contains("srt"))
					fileType = ".srt";
				else if (URL.contains("pdf"))
					fileType = ".pdf";
				else if (URL.contains("pptx"))
					fileType = ".pptx";
				else
					fileType = ".mp4";
				executor.submit(new Downloader(URL, removeInvalidCharacters(titles.get(resource.getId())), filePath, fileType, cookie));
			}
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);
			
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	private String removeInvalidCharacters(String fileName) {
		fileName = fileName.replaceAll("[\\\\/?*\"><|]", "");
		fileName = fileName.replaceAll(":", "-");
		return fileName;
	}
}
