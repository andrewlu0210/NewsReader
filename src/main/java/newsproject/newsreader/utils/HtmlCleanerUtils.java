package newsproject.newsreader.utils;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlCleanerUtils {
    public static HtmlCleaner getHtmlCleaner(String charset) {
        HtmlCleaner cleaner = new HtmlCleaner();
        initProps(cleaner, charset);
        return cleaner;
    }

    public static HtmlCleaner getHtmlCleanerByUTF8() {
        HtmlCleaner cleaner = new HtmlCleaner();
        initProps(cleaner, "utf-8");
        return cleaner;
    }

    public static TagNode getRootNode(HtmlCleaner cleaner, String link) throws Exception{
        byte[] content = HtmlCleanerUtils.getHttpData(link);
        return cleaner.clean(new String(content));
    }
    public static TagNode getFirst(TagNode node, String xpath) throws Exception{
        Object[] objs=node.evaluateXPath(xpath);
        if(objs.length==0){return null;}
        else{
            Object obj=objs[0];
            if(obj instanceof TagNode){
                return (TagNode)obj;
            }else{
                return null;
            }
        }
    }

    public static byte[] getHttpData(String link) throws Exception{
        URL url=null;
        try{
            url=new URL(link);
            BufferedInputStream in=null;
            HttpURLConnection con=null;

            con=(HttpURLConnection)url.openConnection();
            if(con instanceof HttpsURLConnection) {
                HttpsURLConnection conn = (HttpsURLConnection)con;
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
            }


            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
            con.setInstanceFollowRedirects(true);
            con.setConnectTimeout(10000);
            con.setUseCaches(false);
            con.setRequestMethod("GET");
            con.connect();
            in=new BufferedInputStream(con.getInputStream());


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n=0;
            while (-1!=(n=in.read(buf))){
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            con.disconnect();
            buf=null;
            return out.toByteArray();
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    static void initProps(HtmlCleaner cleaner, String charset){
        CleanerProperties props = cleaner.getProperties();
        props.setCharset(charset);
        props.setAdvancedXmlEscape(true);
        props.setTranslateSpecialEntities(true);
        props.setRecognizeUnicodeChars(true);
        props.setUseCdataForScriptAndStyle(true);
        props.setOmitDoctypeDeclaration(true);
        props.setUseEmptyElementTags(true);
        props.setAllowMultiWordAttributes(true);
        props.setIgnoreQuestAndExclam(true);
    }

}
