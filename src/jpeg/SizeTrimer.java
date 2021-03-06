package jpeg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SizeTrimer {
	
	public Image resizeImage(Image rgbImg, int samplingRatio) { 
		int w = rgbImg.getWidth(null);
		int h = rgbImg.getHeight(null); 
		switch (samplingRatio) { 
	         case Sampler.YUV_444:  return scaleYUV4(rgbImg); 
	         case Sampler.YUV_420:  return scaleYUV16(rgbImg);
	         case Sampler.YUV_422:  return scaleYUV16(rgbImg); 
	         default:      throw new IllegalArgumentException("Invalid Sampling Ratio "); 
		 }
	} 
	private Image scaleYUV4(Image rgbImg) {
		
		int w = rgbImg.getWidth(null);
		int h = rgbImg.getHeight(null); 		
		if (w%8 == 0 && h%8 == 0) {
			return rgbImg;
		}	
        int newW = ((int) Math.ceil(w/8.0)) * 8;
        int newH = ((int) Math.ceil(h/8.0)) * 8;
		return newImage(rgbImg, h, w, newH, newW);
	}
	private Image scaleYUV16(Image rgbImg) {
		
		int w = rgbImg.getWidth(null);
		int h = rgbImg.getHeight(null); 
		if (w%16 == 0 && h%16 == 0) {
			return rgbImg;
		}
		
        int newW = ((int) Math.ceil(w/16.0)) * 16;
        int newH = ((int) Math.ceil(h/16.0)) * 16;
        Image ii = newImage(rgbImg, h, w, newH, newW);
		return ii;
		
	}
	
	
	private Image newImage(Image rgbImg, int h, int w, int newH, int newW) {
		
		// create new image with new dimensions 
        try { 
            
              BufferedImage newImg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB); 
             int[] imgData = new int[w * h]; 
             PixelGrabber grabber = new PixelGrabber(rgbImg,0,0,w,h,imgData,0,w); 
             if (grabber.grabPixels()) { 
            	 newImg.setRGB(0,0,w,h,imgData,0,w); 
                // set remaining pixel columns to right-most pixel column of the source image 
                for (int y = 0;y < h;y++) { 
                    int lastColumnValue = imgData[(y+1)*w-1]; 
                    for (int x=w;x < newW; x++) { 
                    	newImg.setRGB(x,y,lastColumnValue); 
                    } 
                } 
                // set remaining pixel rows to bottom-most pixel row of the source image              
                for (int x=0; x < w; x++) { 
                    int lastRowValue = imgData[(h-1)*w+x]; 
                    for (int y=h;y < newH;y++) { 
                    	newImg.setRGB(x,y,lastRowValue); 
                    } 
                } 
                // fill the last bottom-right square with the last pixel value of the source image 
                int widthDiff = newW - w; 
                int heightDiff = newH - h; 
                for (int i = 0;i < heightDiff;i++) { 
                    for (int j = 0; j < widthDiff; j++) { 
                    	newImg.setRGB(w+j,h+i,imgData[imgData.length-1]); 
                    } 
                }
                return newImg; 
            } 
        } 
        catch (Exception e) { 
            System.out.print("\nException occurred: " + e.getMessage()); 
            e.printStackTrace(); 
        } 
        return null; 
    } 
  
	public static void main(String[] args) {
		
		File f = null;
	    //read image
	    try{
	      f = new File("/Users/apple/Desktop/hihi.png"); //image file path
	      Image image = ImageIO.read(f);
	      BufferedImage buffered = (BufferedImage) image;
	      ImageIO.write(buffered, "jpg", new File("myImage.jpg"));
	      
	      System.out.println("Reading complete.");
	      SizeTrimer st = new SizeTrimer();
	      Image m = st.resizeImage(image, 1);
	     // BufferedImage buffered = (BufferedImage) m;
	     // ImageIO.write(buffered, "jpg", new File("myImage.jpg"));
	      
	    }
	    catch(IOException e){
	      System.out.println("Error: "+e);
	    }
	}
	

}
