package com.jx.jebe.bumble.tools;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import sun.net.www.http.HttpClient;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xiaowei on 17/11/2.
 */
public class IOUtils {
    public static File getFileFromHttp(String url){
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File file = new File(uri);

        return file;
    }
    public static void readNetimage(String url){
        try {
            URL imageurl = new URL(url);

            HttpGet getm = new HttpGet(url);
            getm.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n");
            CloseableHttpClient hc = HttpClients.createDefault();
            HttpContext httpContext = new BasicHttpContext();
            CloseableHttpResponse response = hc.execute(getm,httpContext);
            InputStream input = response.getEntity().getContent();

//            InputStream input =connection.getInputStream();
//            BufferedImage image = ImageIO.read(input);
//            image.
//            image.flush();
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
//            writer.
            writer.write(ImageIO.read(input));



//            ImageIO.write(image, "jpg", new BufferedOutputStream(new FileOutputStream("/opt/test.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        IOUtils.readNetimage("http://www.lvzheng.com/picvalidate/be69024d-8d67-4694-a358-c04c9a4f7e53");
//        ITesseract tesseract = Tesseract.getInstance();
//        try {
//            BufferedImage image = ImageIO.read(new File(new URI("http://www.lvzheng.com/picvalidate/be69024d-8d67-4694-a358-c04c9a4f7e53")));
//            try {
//                String ret = tesseract.doOCR(image);
//                System.out.println(ret);
//            } catch (TesseractException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

    }
}
