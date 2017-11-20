package com.jx.jebe.bumble.tools;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by xiaowei on 17/11/18.
 */
public class AliossTool {

    private static final String end_point = "http://oss-cn-beijing.aliyuncs.com";
    private static final String host_url = "xw-cabinet.oss-cn-beijing.aliyuncs.com";
    private static final String accesskey_id = "i3xR4RSOyYNBPZSb";
    private static final String accesskey_secreat = "kU1Wd69jUH56ACMk0uqJi2lxA34R3g";
    private static final String bucket_name = "xw-cabinet";

    public void downloadImage()throws Exception{
        OSSClient client = new OSSClient(end_point,accesskey_id,accesskey_secreat);
        client.getObject(new GetObjectRequest(bucket_name,"AppSignComfirmation_20171109163347_60f41b69-3edc-4793-a179-85aa515126a7.jpg"),new File("/opt/tady.jpg"));
    }

    public InputStream getImageStream(String fiilename)throws Exception{

        OSSClient client = new OSSClient(end_point,accesskey_id,accesskey_secreat);
            OSSObject obj = client.getObject(bucket_name,"AppSignComfirmation_20171109163347_60f41b69-3edc-4793-a179-85aa515126a7.jpg");
        InputStream inputStream = obj.getObjectContent();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int numberbytesread = 0;
        while ((numberbytesread = inputStream.read(buf)) != -1){
            outputStream.write(buf,0,numberbytesread);
        }

        byte[] data = outputStream.toByteArray();
        outputStream.close();
        inputStream.close();

        FileOutputStream outputStream1 = new FileOutputStream("/opt/ttt.jpg");
        outputStream1.write(data);


        client.shutdown();
        return inputStream;
    }
    public static void main(String[] args){
        InputStream inputStream = null;
        try {
            new AliossTool().getImageStream("");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
