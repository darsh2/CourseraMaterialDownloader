package coursera.coursematerial.downloader;

public class Resource {
	private int id;
	private String link;
	
	public Resource(int id, String link) {
		this.id = id;
		this.link = link;
	}
	
	public int getId() {
		return id;
	}
	
	public String getLink() {
		return link;
	}
}
