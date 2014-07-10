package coursera.coursematerial.downloader;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SignIn {
	private final String link = "https://accounts.coursera.org/api/v1/login";
	private String email, password, formData, cAUTHCookie;
	private List<String> cookies;
	
	public SignIn(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public int validator() {
		try {
			URL url = new URL(link);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("X-CSRF2-Token", "csrf2Token");
			connection.setRequestProperty("X-CSRF2-Cookie", "csrf2Cookie");
			connection.setRequestProperty("X-CSRFToken", "csrfToken");
			connection.setRequestProperty("Cookie", "csrftoken=csrfToken; csrf2Cookie=csrf2Token");
			
			connection.setDoOutput(true);
			
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			email = URLEncoder.encode(email, "UTF-8");
			password = URLEncoder.encode(password, "UTF-8");
			formData = String.format("email=%s&password=%s&webrequest=true", email, password);
			out.write(formData);
			out.flush();
			out.close();
			
			connection.connect();
			
			int statusCode = connection.getResponseCode();
			cookies = connection.getHeaderFields().get("Set-Cookie");
			if (cookies != null) {
				for (String cookie : cookies) {
					if (cookie.contains("CAUTH"))
						cAUTHCookie = cookie.substring(0, cookie.indexOf(';'));
				}
				
				if (statusCode == 200 && cAUTHCookie != null)
					return 0;
				else
					return 1;
			}
			else
				return 1;
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}
	
	public String getCookie() {
		return cAUTHCookie;
	}
}
