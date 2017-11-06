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
import java.net.*;

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
                BufferedImage image = ImageIO.read(new URL(url));

                ImageIO.write(image,"png",new File("/opt/get.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public static void main(String[] args){
        IOUtils.readNetimage("http://wsdj.baic.gov.cn/system/getVerifyCode.do");
        String imagestr = "http://wsdj.baic.gov.cn/system/getVerifyCode.do";
        try {
            String tesseract = Tesseract.getInstance().doOCR(ImageIO.read(new URL(imagestr)));
            System.out.println(tesseract);
        } catch (TesseractException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
