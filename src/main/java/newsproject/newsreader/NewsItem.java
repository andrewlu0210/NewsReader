package newsproject.newsreader;

public class NewsItem {
    private String title;
    private String brief;
    private String dateTime;
    private long timestamp;
    private String link;
    private String content="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsItem newsItem = (NewsItem) o;
        return title.equals(newsItem.title) &&
                brief.equals(newsItem.brief) &&
                dateTime.equals(newsItem.dateTime) &&
                link.equals(newsItem.link) &&
                content.equals(newsItem.content);
    }

    public String toString(){
        return "link:["+link+"]\ntitle:["+title+"]\ndateTime:"+dateTime+
                "\nbrief:["+brief+"]";
    }

}
