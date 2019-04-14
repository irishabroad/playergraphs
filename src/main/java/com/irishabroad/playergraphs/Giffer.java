package com.irishabroad.playergraphs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

/*
 * Giffer is a simple java class to make my life easier in creating gif images.
 * 
 * Usage :
 * There are two methods for creating gif images
 * To generate from files, just pass the array of filename Strings to this method
 * Giffer.generateFromFiles(String[] filenames, String output, int delay, boolean loop)
 * 
 * Or as an alternative you can use this method which accepts an array of BufferedImage
 * Giffer.generateFromBI(BufferedImage[] images, String output, int delay, boolean loop)
 * 
 * output is the name of the output file
 * delay is time between frames, accepts hundredth of a time. Yeah it's weird, blame Oracle
 * loop is the boolean for whether you want to make the image loopable.
 */

public abstract class Giffer {

	private Giffer() {
		// Private constructor
	}
	
	// Generate gif from an array of filenames
	// Make the gif loopable if loop is true
	// Set the delay for each frame according to the delay (ms)
	// Use the name given in String output for output file
	public static void generateFromFiles(String[] filenames, String output, int delay, boolean loop)
		throws IOException
	{
		int length = filenames.length;
		BufferedImage[] imageList = new BufferedImage[length];
		
		for (int i = 0; i < length; i++)
		{
			BufferedImage img = ImageIO.read(new File(filenames[i]));
			imageList[i] = img;
		}
		
		generateFromBI(imageList, output, delay, loop);
	}
	
	// Generate gif from BufferedImage array
	// Make the gif loopable if loop is true
	// Set the delay for each frame according to the delay, 100 = 1s
	// Use the name given in String output for output file
	public static void generateFromBI(BufferedImage[] images, String output, int delay, boolean loop)
			throws IOException
	{
		ImageWriter gifWriter = getWriter();
		ImageOutputStream ios = getImageOutputStream(output);
		IIOMetadata metadata = getMetadata(gifWriter, delay, loop);

		gifWriter.setOutput(ios);
		gifWriter.prepareWriteSequence(null);
		for (BufferedImage img: images)
		{
			IIOImage temp = new IIOImage(img, null, metadata);
			gifWriter.writeToSequence(temp, null);
		}
		gifWriter.endWriteSequence();
	}
	
	// Retrieve gif writer
	private static ImageWriter getWriter() throws IIOException
	{
		Iterator<ImageWriter> itr = ImageIO.getImageWritersByFormatName("gif");
		if(itr.hasNext())
			return itr.next();
		
		throw new IIOException("GIF writer doesn't exist on this JVM!");
	}
	
	// Retrieve output stream from the given file name
	private static ImageOutputStream getImageOutputStream(String output) throws IOException
	{
		File outfile = new File(output);
		return ImageIO.createImageOutputStream(outfile);
	}
	
	// Prepare metadata from the user input, add the delays and make it loopable
	// based on the method parameters
	private static IIOMetadata getMetadata(ImageWriter writer, int delay, boolean loop)
		throws IIOInvalidTreeException
	{
		// Get the whole metadata tree node, the name is javax_imageio_gif_image_1.0
		// Not sure why I need the ImageTypeSpecifier, but it doesn't work without it
		ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB);
		IIOMetadata metadata = writer.getDefaultImageMetadata(imageType, null);
		String nativeFormat = metadata.getNativeMetadataFormatName();
		IIOMetadataNode nodeTree = (IIOMetadataNode)metadata.getAsTree(nativeFormat);
		
		// Set the delay time you can see the format specification on this page
		// https://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/gif_metadata.html
		IIOMetadataNode graphicsNode = getNode("GraphicControlExtension", nodeTree);
		graphicsNode.setAttribute("delayTime", String.valueOf(delay));
		graphicsNode.setAttribute("disposalMethod", "none");
		graphicsNode.setAttribute("userInputFlag", "FALSE");
		
		if(loop)
			makeLoopy(nodeTree);
		
		metadata.setFromTree(nativeFormat, nodeTree);
		
		return metadata;
	}
	
	// Add an extra Application Extension node if the user wants it to be loopable
	// I am not sure about this part, got the code from StackOverflow
	private static void makeLoopy(IIOMetadataNode root)
	{
		IIOMetadataNode applicationExtensions = getNode("ApplicationExtensions", root);
		IIOMetadataNode applicationNode = getNode("ApplicationExtension", applicationExtensions);
		
		applicationNode.setAttribute("applicationID", "NETSCAPE");
		applicationNode.setAttribute("authenticationCode", "2.0");
		applicationNode.setUserObject(new byte[]{ 0x1, (byte) (0 & 0xFF), (byte) ((0 >> 8) & 0xFF)});
		
		applicationExtensions.appendChild(applicationNode);
		root.appendChild(applicationExtensions);
	}
	
	// Retrieve the node with the name from the parent root node
	// Append the node if the node with the given name doesn't exist
	private static IIOMetadataNode getNode(String nodeName, IIOMetadataNode root)
	{
		IIOMetadataNode node = null;
		
		for (int i = 0; i < root.getLength(); i++)
		{
			if(root.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0)
			{
				node = (IIOMetadataNode) root.item(i);
				return node;
			}
		}
		
		// Append the node with the given name if it doesn't exist
		node = new IIOMetadataNode(nodeName);
		root.appendChild(node);
		
		return node;
	}
	
}
