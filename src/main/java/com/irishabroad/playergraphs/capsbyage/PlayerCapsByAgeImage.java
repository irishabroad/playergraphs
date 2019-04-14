package com.irishabroad.playergraphs.capsbyage;

import static com.irishabroad.playergraphs.Constants.BACKGROUND_COLOUR;
import static com.irishabroad.playergraphs.Constants.MONTHS_FONT;
import static com.irishabroad.playergraphs.Constants.NUMBER_INDEX_FONT;
import static com.irishabroad.playergraphs.Constants.PLAYER_COLOURS;
import static com.irishabroad.playergraphs.Constants.PLAYER_NAME_FONT;
import static com.irishabroad.playergraphs.Constants.YEARS_FONT;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import com.google.common.base.Preconditions;

public class PlayerCapsByAgeImage {

	private BufferedImage playerGoalsImage;

	private File image;
	
	private List<String> names;
	private List<Integer> goals;

	private int years;
	private int months;
	
	public PlayerCapsByAgeImage(@Nonnull List<String> names, @Nonnull List<Integer> goals, int years, int months) {
		
		Preconditions.checkArgument(names.size() == goals.size(), "Inequal number of names and caps");
		this.playerGoalsImage = new BufferedImage(1700, 360, ColorSpace.TYPE_RGB);
		this.names = names;
		this.goals = goals;
		this.years = years;
		this.months = months;
		
	}
	
	@Nonnull
	public File getImageFile() {
		return image;
	}

	public void generateImage(int counter) throws IOException {
		
		Graphics2D graphics = playerGoalsImage.createGraphics();
		graphics.setColor(BACKGROUND_COLOUR);
		graphics.fillRect(0, 0, playerGoalsImage.getWidth(), playerGoalsImage.getHeight());
		graphics.setFont(PLAYER_NAME_FONT);
	    FontMetrics fontMetrics = graphics.getFontMetrics();
	    int stringHeight = fontMetrics.getAscent();
	    graphics.setPaint(Color.black);
	    int nameY = 40;
    	graphics.drawString("Player", 10, nameY);
    	nameY += stringHeight + 15;
	    for (String name: names) {
	    	
	    	graphics.drawString(name, 10, nameY);
	    	nameY += stringHeight + 10;
	    	
	    }
	    graphics.drawLine(10, 50, 1680, 50);
	    graphics.drawLine(180, 10, 180, 350);
	    int index = 10;
		graphics.setFont(NUMBER_INDEX_FONT);
		fontMetrics = graphics.getFontMetrics();
	    for (int yearX = 280; yearX < 1700; yearX += 100) {
	    
	    	graphics.drawLine(yearX, 47, yearX, 53);
	    	String indexString = String.valueOf(index);
	    	graphics.drawString(indexString, yearX - (fontMetrics.stringWidth(indexString) / 2), 45);
	    	index += 10;
		    
	    }
		graphics.setFont(YEARS_FONT);
		fontMetrics = graphics.getFontMetrics();
		String yearString = years + " Years";
		graphics.drawString(yearString, 1680 - fontMetrics.stringWidth(yearString), 300);
		graphics.setFont(MONTHS_FONT);
		fontMetrics = graphics.getFontMetrics();
		String monthString = months + " Months";
		graphics.drawString(monthString, 1680 - fontMetrics.stringWidth(monthString), 320);
	    
	    int goalsY = stringHeight + 37;
	    int playerColourIndex = 0;
	    for (Integer goal: goals) {
	    	
		    graphics.setPaint(PLAYER_COLOURS[playerColourIndex++]);
	    	graphics.fillRect(181, goalsY, (goal * 10), stringHeight);
	    	graphics.drawString(String.valueOf(goal), (185 + (goal * 10)), goalsY + stringHeight - 4);
	    	goalsY += stringHeight + 10;
	    	
	    	
	    }
		image = new File("output/capsbyage/playerGoals" + String.format("%03d", counter) + ".png");
		if(image.exists()) {
			
			Files.delete(Paths.get(image.toURI()));
			
		}
        ImageIO.write(playerGoalsImage, "png", image);

	}

}
