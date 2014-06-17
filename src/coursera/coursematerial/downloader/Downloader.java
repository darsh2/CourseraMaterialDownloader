package coursera.coursematerial.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.net.ssl.HttpsURLConnection;

public class Downloader implements Runnable {
	private String URL, filePath;
	private FileOutputStream out;
	
	public Downloader(String URL, String fileName, String filePath, String fileType) {
		this.URL = URL;
		this.filePath = filePath.concat(fileName).concat(fileType);
	}
	
	@Override
	public void run() {
		ReadableByteChannel rbc = null;
		try {
			File download = new File(filePath);
			URL url = new URL(URL);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestProperty("Range", "bytes=0-");
			connection.connect();
			
			int fileSize = connection.getContentLength();
			rbc = Channels.newChannel(connection.getInputStream());
			out = new FileOutputStream(download);
			out.getChannel().transferFrom(rbc, 0, fileSize);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				out.close();
				if (rbc != null)
					rbc.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
