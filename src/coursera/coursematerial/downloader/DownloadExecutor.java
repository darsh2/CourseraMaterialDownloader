package coursera.coursematerial.downloader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadExecutor {
	public int executeDownload(ArrayList<String> toDownload, ArrayList<String> titles, 
			String filePath, String fileType, String cookie) {
		int l = toDownload.size();
		
		try {
			ExecutorService executor = Executors.newFixedThreadPool(l < 4 ? l : 4);
			for (int i = 0; i < l; i++)
				executor.submit(new Downloader(toDownload.get(i), removeInvalidCharacters(titles.get(i)), filePath, fileType, cookie));
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
