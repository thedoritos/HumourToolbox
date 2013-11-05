package model;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class Dotter {
	
	public void saveDotImage(PApplet p5, String res, int scale) {
		
		PImage img = p5.loadImage(res);
		img.loadPixels();
		
		int[] colors = img.pixels;
		for (int i = 0; i < img.width / scale; i++) {
			for (int j = 0; j < img.height / scale; j++) {
				
				int col = i * scale;
				int row = j * scale;
				
				int color = colors[row * img.width + col];
				
				for (int c = 0; c < scale; c++) {
					for (int r = 0; r < scale; r++) {
						colors[(row + r) * img.width + (col + c)] = color;
					}
				}
			}
		}
		
		String outExt = PApplet.getExtension(res);
		String outPreExt = res.substring(0, res.length() - (outExt.length() + 1));
		
		String outName = outPreExt + "_" + scale + "x" + "." + outExt;
		img.save(outName);
		
		System.out.println("Image '" + outName + "' saved.");
	}
	
	public static String createDotImageName(String res, int scale, String fmt) {
		
		String outExt = PApplet.getExtension(res);
		String outPreExt = res.substring(0, res.length() - (outExt.length() + 1));
		
		return outPreExt + "_" + scale + "x" + "." + fmt;
	}
	
	public static String createDotImageName(String res, int scale) {
		
		String outExt = PApplet.getExtension(res);
		String outPreExt = res.substring(0, res.length() - (outExt.length() + 1));
		
		return outPreExt + "_" + scale + "x" + "." + outExt;
	}
	
	public static PImage createDotImage(PApplet p5, PImage image, int scale) {
		
		PImage img = image.get();
		
		// Check scale.
		// If scale equals to 1, return the source image directory.
		// This method does not support scales under 1. Return the source
		// if the scale is so.
		if (scale == 1 || scale < 1) {
			return img;
		}
		
		// Load colors to the inner array.
		img.loadPixels();
		
		// Change colors in the inner array.
		int[] colors = img.pixels;
		for (int i = 0; i < img.width / scale; i++) {
			for (int j = 0; j < img.height / scale; j++) {
				
				int col = i * scale;
				int row = j * scale;
				
				int color = colors[row * img.width + col];
				
				for (int c = 0; c < scale; c++) {
					for (int r = 0; r < scale; r++) {
						colors[(row + r) * img.width + (col + c)] = color;
					}
				}
			}
		}
		
		return img;
	}
	
	public static boolean isImageFile(String fileName) {
		String ext = PApplet.getExtension(fileName);
		List<String> fmts = Arrays.asList("png", "jpg", "jpeg", "bmp");
		for (String fmt : fmts) {
			if (ext.equals(fmt)) {
				return true;
			}
		}
		return false;
	}

}
