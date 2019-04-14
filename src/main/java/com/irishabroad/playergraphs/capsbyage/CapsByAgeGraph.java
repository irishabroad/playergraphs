package com.irishabroad.playergraphs.capsbyage;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import com.irishabroad.playergraphs.Giffer;
import com.irishabroad.playergraphs.model.PlayerStats;

public class CapsByAgeGraph {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandle.class.getName());

	public CapsByAgeGraph() throws IOException {
		
		List<PlayerStats> playerGoalsByMonth = new ArrayList<>();
		URL url = Resources.getResource("playerCapsByAge.txt");
		String fileContents = Resources.toString(url, StandardCharsets.UTF_8);
		String[] lines = fileContents.split("\n");
		Arrays.stream(lines).forEach(l -> playerGoalsByMonth.add(new PlayerStats(l)));
		int years = 17;
		int months = 6;
		List<String> imageFilenames = new ArrayList<>();
		
		for (int counter = 0; counter < 273; counter++) {
		
			List<Integer> goals = new ArrayList<>();
			List<String> names = new ArrayList<>();
			for (PlayerStats goalsByMonth: playerGoalsByMonth) {
				
				goals.add(goalsByMonth.getStats().get(counter));
				names.add(goalsByMonth.getName());
				
			}
			PlayerCapsByAgeImage playerGoalsImage = new PlayerCapsByAgeImage(names, goals, years, months);
			playerGoalsImage.generateImage(counter);
			imageFilenames.add(playerGoalsImage.getImageFile().getAbsolutePath());
			if (++months == 12) {
				
				months = 0;
				years++;
				
			}
			
		}
		Giffer.generateFromFiles(imageFilenames.toArray(new String[imageFilenames.size()]), "capsByAgeGif.gif", 10, false);
		
	}
	
	public static void main(String...args) {
		
		try {
			
			new CapsByAgeGraph();
			
		} catch (IOException e) {
			
			LOGGER.debug("Error writing goals graph", e);
			
		}
		
	}
	
}
