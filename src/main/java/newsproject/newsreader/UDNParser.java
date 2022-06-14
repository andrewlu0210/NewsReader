package newsproject.newsreader;

import newsproject.newsreader.utils.HtmlCleanerUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UDNParser {
    public static final String NewsRootLink = "https://udn.com/news/breaknews/1/11#breaknews";
//    public static final SimpleDateFormat dayDF = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat timeDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public List<NewsItem> readList(HtmlCleaner cleaner) throws Exception{
        TagNode rootNode = HtmlCleanerUtils.getRootNode(cleaner, NewsRootLink);
        return parseByRootNode(rootNode);
    }

    private List<NewsItem> parseByRootNode(TagNode rootNode) throws Exception{
        //TagNode rootNode = HtmlCleanerUtils.getRootNode(cleaner, rootLink);
        TagNode section = HtmlCleanerUtils.getFirst(rootNode,"//section[@id='breaknews']");
        Object[] list = section.evaluateXPath("//div[@class='story-list__news']");
        List<NewsItem> infos = new ArrayList<>();
        for(Object obj : list){
            if(obj instanceof TagNode){
                TagNode node = (TagNode)obj;
                TagNode itemNode = HtmlCleanerUtils.getFirst(node,"//div[@class='story-list__text']");
                TagNode titleNode = itemNode.findElementByName("h2", false)
                        .findElementByName("a",false);
                TagNode briefNode = itemNode.findElementByName("p", false);
                TagNode timeNode = itemNode.findElementByName("time", true);
                String title = titleNode.getText().toString().trim();
                String link = "https://udn.com"+titleNode.getAttributeByName("href");
                String brief = briefNode.getText().toString().trim();
                String dateTime = timeNode.getText().toString().trim();
                Date time = timeDF.parse(dateTime);

                NewsItem info = new NewsItem();
                info.setTitle(title);
                info.setBrief(brief);
                info.setDateTime(dateTime);
                info.setTimestamp(time.getTime());
                info.setLink(link);
                infos.add(info);
            }
        }

        return infos;
    }

    public void getNewsContent(HtmlCleaner cleaner, NewsItem item) throws Exception{
        System.out.println("Getting "+item.getTitle()+"["+item.getDateTime()+"]");
        TagNode rootNode = HtmlCleanerUtils.getRootNode(cleaner, item.getLink());
        TagNode article = HtmlCleanerUtils.getFirst(rootNode, "//section[@class='article-content__editor']");
        String content = "";
        if(article!=null){
            // Set content to empty if xpath selection is failed
            TagNode[] ps = article.getChildTags();
            for(TagNode node:ps){
                if(node.getName().equalsIgnoreCase("p")){
                    String text = node.getText().toString().trim();
                    if(!text.equals("")){
                        content = content+"\n"+text;
                    }
                }
            }
            content = content.trim();
        }
        item.setContent(content);
        //System.out.println(content);
    }

}
