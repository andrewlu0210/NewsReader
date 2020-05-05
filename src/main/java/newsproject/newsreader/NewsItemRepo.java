package newsproject.newsreader;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class NewsItemRepo {
    private String dbHost, dbName, account, passwd;
    private boolean inited=false;
    private MongoDatabase db;
    private MongoClient mongo;
    static String NEWS_ITEM = "news_item";

    public NewsItemRepo(String dbHost, String dbName, String account, String passwd){
        this.dbHost = dbHost;
        this.dbName = dbName;
        this.account = account;
        this.passwd = passwd;
    }
    public NewsItem getNews(String link) {
        MongoCollection<Document> col = db.getCollection(NEWS_ITEM);
        Document q = new Document("link", link);
        Document ret = col.find(q).limit(1).first();
        if(ret!=null){
            return parseNewsInfo(ret);
        }
        return null;
    }

    public void saveNews(List<NewsItem> infos){
        if(infos ==null || infos.size()==0){return;}
        MongoCollection<Document> col = db.getCollection(NEWS_ITEM);
        List<Document> docs = new ArrayList<>();
        for(NewsItem info:infos){
            Document doc = new Document("link", info.getLink())
                    .append("title", info.getTitle())
                    .append("brief",info.getBrief())
                    .append("dateTime", info.getDateTime())
                    .append("timestamp", info.getTimestamp())
                    .append("content", info.getContent());
            docs.add(doc);
        }
        col.insertMany(docs);
    }

    private NewsItem parseNewsInfo(Document ret){
        NewsItem info = new NewsItem();
        info.setLink(ret.getString("link"));
        info.setTitle(ret.getString("title"));
        info.setBrief(ret.getString("brief"));
        info.setDateTime(ret.getString("dateTime"));
        info.setTimestamp(ret.getLong("timestamp"));
        info.setContent(ret.getString("content"));
        //System.out.println(info.getLink()+","+info.getTitle()+","+info.getDateTime()+","+info.getBrief());
        return info;
    }

    public void init(){
        if(inited){return;}
        mongo = new MongoClient(
                new MongoClientURI( "mongodb://"+account+
                        ":"+passwd+"@"+
                        dbHost+"/"+dbName )
        );
        db=mongo.getDatabase(dbName);
        inited=true;
    }
    public void close(){
        if(mongo!=null){
            mongo.close();
            mongo=null;
            db = null;
            inited=false;
        }
    }
}
