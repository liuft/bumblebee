package com.jx.jebe.bumble.tools;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class TestTess4j {
    public static void main(String[] args){


        String imagestr = "http://wsdj.baic.gov.cn/system/getVerifyCode.do";
        System.out.println(System.getProperty("java.library.path"));
        try {
            ITesseract stance = new Tesseract();

            System.out.println(stance.doOCR(ImageIO.read(new URL(imagestr))));
        } catch (TesseractException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
