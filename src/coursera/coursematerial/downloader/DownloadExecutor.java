package coursera.coursematerial.downloader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadExecutor {
	public int executeDownload(ArrayList<String> toDownload, ArrayList<String> titles, 
			String filePath, String fileType) {
		int l = toDownload.size();
		
		try {
			//System.out.println(filePath);
			ExecutorService executor = Executors.newFixedThreadPool(l < 4 ? l : 4);
			for (int i = 0; i < l; i++) {
				//System.out.println(toDownload.get(i) + " " + titles.get(i));
				executor.submit(new Downloader(toDownload.get(i), removeInvalidCharacters(titles.get(i)), filePath, fileType));
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
		char[] buffer = new char[1000];
		int p = 0;
		for (int i = 0, l = fileName.length(); i < l; i++) {
			char e = fileName.charAt(i);
			if ((e >= 65 && e <= 90) || (e >= 97 && e <= 122) || e == ' ' || e == '\t')
				buffer[p++] = e;
		}
		char[] file = new char[p];
		for (int i = 0; i < p; i++)
			file[i] = buffer[i];
		return new String(file);
	}
}
