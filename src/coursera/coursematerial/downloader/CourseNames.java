package coursera.coursematerial.downloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CourseNames {
	public Map<String, String> getCourses(String cookie) throws Exception {
		Map<String, String> courseDetails = new HashMap<String, String>();
		final String URL = "https://www.coursera.org/maestro/api/topic/list2_combined";
		
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
		
		/*
		 * The following code was taken from DhulkeeCoursera
		 * Used to get active course names
		 */
		JSONObject getJsonFile = (JSONObject) JSONValue.parse(crawler);
		JSONArray enrollments = (JSONArray) getJsonFile.get("enrollments");
		JSONObject list2 = (JSONObject) getJsonFile.get("list2");
		JSONArray courses = (JSONArray) list2.get("courses");
		JSONObject topics = (JSONObject) list2.get("topics");
		
		for (Object enrollmentObj : enrollments) {
			JSONObject enrollment = (JSONObject) enrollmentObj;
			Number course_id = (Number) enrollment.get("course_id");
			for (Object courseObj : courses) {
				JSONObject course = (JSONObject) courseObj;
				if (course.get("id").equals(course_id)) {
					boolean active = (boolean)((Object)course.get("active")); 
					if (active) {
						String id = String.valueOf((Number) course.get("topic_id"));
						courseDetails.put((String) ((JSONObject) topics.get(id)).get("name"), (String) course.get("home_link"));
					}
				}
			}
		}
		return courseDetails;
	}
}
