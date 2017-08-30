public class InfoDate {
	int type;
	String atDate;
	String beginDate;
	String endDate;
	String link;
	String titleTimeline;
	String titlePage;
	String dateFormat;

	public InfoDate(){
	}
	
	public String getbeginDate() {
		return beginDate;
	}

	public void setbeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getendDate() {
		return endDate;
	}

	public void setendDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getatDate() {
		return atDate;
	}

	public void setatDate(String atDate) {
		this.atDate = atDate;
	}
	
	public String gettitleTimeline() {
		return titleTimeline;
	}

	public void settitleTimeline(String titleTimeline) {
		this.titleTimeline = titleTimeline;
	}
	
	public String gettitlePage() {
		return titlePage;
	}

	public void settitlePage(String titlePage) {
		this.titlePage = titlePage;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public String toString() {
		switch (type){
		case 1 : return "InfoDate [titlePage=" + titlePage + ", type=" + type + ", beginDate=" + beginDate + ", endDate=" + endDate + ", link=" + link + ", dateFormat=" + dateFormat + "]";
		case 2 : return "InfoDate [titlePage=" + titlePage + ", type=" + type + ", atDate=" + atDate + ", link=" + link + ", dateFormat=" + dateFormat + "]";
		case 3 : return "InfoDate [titlePage=" + titlePage + ", type=" + type + ", beginDate=" + beginDate + ", endDate=" + endDate + ", link=" + link + ", titleTimeline=" + titleTimeline + ", dateFormat=" + dateFormat + "]";
		case 4 : return "InfoDate [titlePage=" + titlePage + ", type=" + type + ", atDate=" + atDate + ", link=" + link + ", titleTimeline=" + titleTimeline + ", dateFormat=" + dateFormat + "]";
		default : return "Infodate type is not correct";
		}
	}
}