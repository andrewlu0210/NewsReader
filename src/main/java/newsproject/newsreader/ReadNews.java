package newsproject.newsreader;

import newsproject.newsreader.utils.HtmlCleanerUtils;
import org.htmlcleaner.HtmlCleaner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadNews {
    static String account = "news";
    static String passwd = "news";
    static String dbHost = "10.231.8.177";
    static String dbName = "newsDB";

    public static void main(String[] args){
        System.out.println("Getting UDN News...");
        NewsItemRepo repo = new NewsItemRepo(dbHost, dbName, account, passwd);
        try{
            repo.init();
            HtmlCleaner cleaner = HtmlCleanerUtils.getHtmlCleanerByUTF8();
            UDNParser parser =new UDNParser();
            List<NewsItem> items = parser.readList(cleaner);
            List<NewsItem> toSave = new ArrayList<>();
            for(NewsItem item:items){
                NewsItem check = repo.getNews(item.getLink());
                if(check==null){
                    Thread.sleep(500);
                    parser.getNewsContent(cleaner, item);
                    toSave.add(item);
                }
            }
            String nowStr=UDNParser.timeDF.format(new Date());
            System.out.print(nowStr+">>");
            if(toSave.size()>0){
                repo.saveNews(toSave);
                System.out.println(toSave.size()+" news Saved!");
            }else{
                System.out.println("No need to add news!");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            repo.close();
        }
    }
}
