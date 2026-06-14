package dto;
import java.io.Serializable;
import java.time.LocalDate;

public class Info implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long info_id;
	private String title;
	private String content;
	private LocalDate post_date;

	public Info() {
	}

	public Info(Long info_id, String title, String content, LocalDate post_date) {
		super();
		this.info_id = info_id;
		this.title = title;
		this.content = content;
		this.post_date = post_date;
	}

	public Long getInfo_id() {
		return info_id;
	}

	public void setInfo_id(Long info_id) {
		this.info_id = info_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDate getPost_date() {
		return post_date;
	}

	public void setPost_date(LocalDate post_date) {
		this.post_date = post_date;
	}

}
