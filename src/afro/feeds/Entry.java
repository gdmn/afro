package afro.feeds;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dmn
 */
public class Entry implements Comparable<Entry> {

	private String title;
	private String href;
	private Date updated;
	private String content;
	private String feedName;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getFeedName() {
		return feedName;
	}

	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

	@Override
	public int compareTo(Entry o) {
		// TODO test, cos nie tak
		int result = 0;
		if (updated != null && o.updated != null) {
			result = -updated.compareTo(o.updated);
		} else if (updated == null) {
			result = -1;
		}
		if (result == 0) {
			result = title.compareTo(o.title);
		}
		return result;
	}

	public static String dateToString(Date value) {
		return dateToString("yyyy-MM-dd HH:mm", value);
	}

	public static String dateToString(String format, Date value) {
		if (value == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(format, Locale.US);
		return formatter.format(value);
	}

	public static Date parseDate(String format, String value) {
		DateFormat formatter = new SimpleDateFormat(format, Locale.US);

		Date date = null;
		try {
			date = (Date) formatter.parse(value);
		} catch (ParseException ex) {
			Logger.getLogger(Entry.class.getName()).log(Level.SEVERE, ex.getMessage());
		}
		return date;
	}

	public static Date parseDate(String value) {
		String[] DATE_FORMATS = new String[]{"EEE, dd MMM yyyy HH:mm:ss Z",
			"yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd'T'HH:mm:ssZ",
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ",
			"yyyy-MM-dd",
			"dd.MM.yyyy, HH:mm",
			"MMM/dd/yyyy",

		};
		for (String format : DATE_FORMATS) {
			try {
				DateFormat formatter = new SimpleDateFormat(format, Locale.US);
				Date date = (Date) formatter.parse(value);
				return date;
			} catch (ParseException ex) {
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Entry{" + (feedName == null ? "" : "feedName=" + feedName + "; ") + (updated == null ? ""
				: "updated=" + dateToString(updated) + "; ") + "title=" + title
				+ "; href=" + href + "; "
				+ "content=" + content + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Entry other = (Entry) obj;
		if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
			return false;
		}
		if ((this.href == null) ? (other.href != null) : !this.href.equals(other.href)) {
			return false;
		}
		if (this.updated != other.updated && (this.updated == null || !this.updated.equals(other.updated))) {
			return false;
		}
		if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 71 * hash + (this.title != null ? this.title.hashCode() : 0);
		hash = 71 * hash + (this.href != null ? this.href.hashCode() : 0);
		hash = 71 * hash + (this.content != null ? this.content.hashCode() : 0);
		return hash;
	}
}
