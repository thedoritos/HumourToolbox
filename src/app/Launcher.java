package app;

import java.awt.Rectangle;

import model.Dotter;
import controlP5.*;

import processing.core.PApplet;
import processing.core.PImage;

public class Launcher extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	private Rectangle screenBounds = new Rectangle(480, 480);
	
	private ControlP5 gui;
	private int bgColor = 0xFF000000;
	
	public static void main(String[] args) {
		PApplet.main(new String[]{Launcher.class.getName()});
	}
	
	public void setup() {
		size(screenBounds.width, screenBounds.height);
		background(0);
		
		this.frame.setTitle("Dotter : ver 0.0.0");
		
		gui = new ControlP5(this);
		
		setupFilePathSelector(gui, 10, 10);
		setupSizeSelector(gui, 10, 60);
		setupFormatSelector(gui, 10, 110);
		setupExecuteButton(gui, 10, 160);
		setupMessageDialog(gui, 10, height - 100);
		
		setupBGSelector(gui, width - 60, 10);
	}
	
	public void setupBGSelector(ControlP5 gui, float x, float y) {
//		gui.addTextlabel("lbl_bw")
//		   .setText("UI Color : black or white.")
//		   .setPosition(x, y);
		gui.addRadioButton("bgButton")
		   .setPosition(x, y)
//		   .setItemsPerRow(4)
		   .setSpacingColumn(40)
		   .addItem("black", 0)
		   .addItem("white", 1)
		   .activate(0);
	}
	
	public void setupFilePathSelector(ControlP5 gui, float x, float y) {
		gui.addTextlabel("lbl_input")
		   .setText("Base Image : type filename and 'enter'.")
		   .setPosition(x, y);
//		gui.addButton("load")
//		   .setPosition(220, y + 20);
		gui.addTextfield("input")
		   .setAutoClear(false)
		   .setPosition(x, y + 20)
		   .setCaptionLabel("");
	}
	
	public void setupSizeSelector(ControlP5 gui, float x, float y) {
		gui.addTextlabel("resolutionLabel")
		   .setText("Dot Size : select size to generate.")
		   .setPosition(x, y);
		gui.addRadioButton("sizeButton")
		   .setPosition(x, y + 20)
		   .setItemsPerRow(4)
		   .setSpacingColumn(40)
		   .addItem("1x", 1)
		   .addItem("2x", 2)
		   .addItem("4x", 4)
		   .addItem("8x", 8)
		   .activate(0);
	}
	
	public void setupFormatSelector(ControlP5 gui, float x, float y) {
		gui.addTextlabel("lbl_fmt")
		   .setText("Format : select fmt as '.png' or original.")
		   .setPosition(x, y);
		gui.addRadioButton("fmtButton")
		   .setPosition(x, y + 20)
		   .setItemsPerRow(4)
		   .setSpacingColumn(40)
		   .addItem("png", 0)
		   .addItem("original", 1)
		   .activate(0);
	}
	
	public void setupExecuteButton(ControlP5 gui, float x, float y) {
		gui.addTextlabel("lbl_gen")
		   .setText("Execute :")
		   .setPosition(x, y);
		gui.addButton("generate")
		   .setPosition(x, y + 20);
	}
	
	public void setupMessageDialog(ControlP5 gui, float x, float y) {
		gui.addTextlabel("lbl_err")
		   .setText("Message : ")
		   .setPosition(x, y);
		gui.addTextarea("area_msg")
		   .setText("Hello. This is dot image generator.")
		   .setSize(400, 20)
		   .setPosition(x, y + 20);
	}
	
	private PImage img = null;
	private PImage imgGen = null;
	private String imgName = null;
	
	public void draw() {
		
		background(bgColor);
		
		if (imgGen != null) {
			
			float scaleX = (float) imgGen.width / width;
			float scaleY = (float) imgGen.height / height;
			float scale = (scaleX > scaleY) ? scaleX : scaleY;
			
			if (scale > 1.0f) {
				pushMatrix();
				{
					scale(1.0f / scale);
					imageMode(CENTER);
					image(imgGen, width * scale * 0.5f, height * scale * 0.5f);
				}
				popMatrix();
			} else {
				imageMode(CENTER);
				image(imgGen, width * 0.5f, height * 0.5f);
			}
			
		}
	}
	
	public void controlEvent(ControlEvent e) {
		if (e.isAssignableFrom(Launcher.class)) {
			println("Event Name:" + e.getName() + " Value:" + e.getStringValue() + " .");
		}
	}
	
	public void printMsg(String msg) {
		Textarea area = (Textarea) gui.get("area_msg");
		if (area != null) {
//			area.append("\n" + msg);
			area.setText(msg);
		}
	}
	
	public void input(String text) {
		
		printMsg("Loading image...");
		
		// Clear image
		img = null;
		imgGen = null;
		imgName = null;
		
		// Check input type.
		if (!Dotter.isImageFile(text)) {
			printMsg("Irregal input type. Not an image.");
			return;
		}
		
		// Reload image
		imgName = new String(text);
		img = loadImage(text);
		if (img == null) {
			printMsg("Failed to load image. It exists?");
			return;
		}
		
		// Generate image
		RadioButton radio = (RadioButton) gui.get("sizeButton");
		if (radio != null) {
			float scale = radio.getValue();
			imgGen = Dotter.createDotImage(this, img, (int) scale);
		}
		
		printMsg("Image successfully loaded.");
	}
	
	public void sizeButton(int value) {
		if (img != null) {
			imgGen = Dotter.createDotImage(this, img, value);
			printMsg("Image dotted as " + value + " x " + value + ".");
		}
	}
	
	public void fmtButton(int value) {
		
	}
	
	public void bgButton(int value) {
		// 0 : black
		// 1 : white
		switch (value) {
		case 0:
		default:
			gui.setColorCaptionLabel(0xFFFFFFFF);
			gui.setColorValueLabel(0xFFFFFFFF);
			gui.setColorBackground(0xFF003652);
			bgColor = 0xFF000000;
			break;
		case 1:
			gui.setColorCaptionLabel(0xFF303030);
			gui.setColorValueLabel(0xFF303030);
			gui.setColorBackground(0x44003652);
			bgColor = 0xFFFFFFFF;
			break;
		}
		
		// TextArea is always transparent.
		Textarea area = (Textarea) gui.get("area_msg");
		if (area != null) {
			area.setColorBackground(0x00FFFFFF);
		}
	}
	
	public void generate() {
		printMsg("Exporting image.");
		
		// Check image.
		if (imgGen == null || imgName == null) {
			printMsg("Image to export not found.");
			return;
		}
		
		// Get size.
		RadioButton radio = (RadioButton) gui.get("sizeButton");
		if (radio == null) {
			printMsg("Image size to export not found.");
			return;
		}
		
		// Get format & name.
		String name = null;
		RadioButton fmt = (RadioButton) gui.get("fmtButton");
		if (fmt != null) {
			float format = fmt.getValue();
			if (format == 0) {
				// as PNG
				name = Dotter.createDotImageName(imgName, (int) radio.getValue(), "png");
			} else {
				// same as original.
				name = Dotter.createDotImageName(imgName, (int) radio.getValue());
			}
		}
		
		// Save image.
		if (name == null) {
			printMsg("Image format to export not found.");
			return;
		}
		
		imgGen.save(name);
		printMsg("Image successfully exported.");
	}
	
}
