package edu.miu.reversi;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

public class TextMetric
{
	public TextMetric()	{
		return;
	}
	
	public static Dimension getMetric(String text, Font theFont)
	{
		GraphicsEnvironment mGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice mGD = mGE.getDefaultScreenDevice();
		GraphicsConfiguration mGC = mGD.getDefaultConfiguration();
		BufferedImage tempImage = mGC.createCompatibleImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)tempImage.getGraphics();
		Dimension result = new Dimension();
		FontMetrics metrics = g2d.getFontMetrics(theFont);
		result.height = metrics.getHeight();
		result.width = metrics.stringWidth(text);
		return result;
	}
}
