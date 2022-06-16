package newsproject.newsreader;

import newsproject.newsreader.utils.HtmlCleanerUtils;
import org.htmlcleaner.HtmlCleaner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadNews {

    public static void main(String[] args){
        if(args.length!=4){
            System.out.println("USAGE: java newsproject.newsreader.ReadNews dbHost dbName account password");
            return;
        }
        String dbHost = args[0].trim();
        String dbName = args[1].trim();
        String account = args[2].trim();
        String passwd = args[3].trim();
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
