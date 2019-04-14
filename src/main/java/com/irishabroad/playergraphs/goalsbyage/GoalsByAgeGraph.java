package com.irishabroad.playergraphs.goalsbyage;

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

public class GoalsByAgeGraph {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandle.class.getName());

	public GoalsByAgeGraph() throws IOException {
		
		List<PlayerStats> playerGoalsByMonth = new ArrayList<>();
		URL url = Resources.getResource("playerGoalsByAge.txt");
		String fileContents = Resources.toString(url, StandardCharsets.UTF_8);
		String[] lines = fileContents.split("\n");
		Arrays.stream(lines).forEach(l -> playerGoalsByMonth.add(new PlayerStats(l)));
		int years = 18;
		int months = 3;
		List<String> imageFilenames = new ArrayList<>();
		
		for (int counter = 0; counter < 227; counter++) {
		
			List<Integer> goals = new ArrayList<>();
			List<String> names = new ArrayList<>();
			for (PlayerStats goalsByMonth: playerGoalsByMonth) {
				goals.add(goalsByMonth.getStats().get(counter));
				names.add(goalsByMonth.getName());
			}
			PlayerGoalsByAgeImage playerGoalsImage = new PlayerGoalsByAgeImage(names, goals, years, months);
			try {
				
				playerGoalsImage.generateImage(counter);
				imageFilenames.add(playerGoalsImage.getImage().getAbsolutePath());
				
			} catch (IOException e) {
				
				LOGGER.debug("Error writing goals graph", e);
				
			}
			if (++months == 12) {
				
				months = 0;
				years++;
				
			}
			
		}
		Giffer.generateFromFiles(imageFilenames.toArray(new String[imageFilenames.size()]), "goalsByAgeGif.gif", 10, false);
		
	}
	
	public static void main(String...args) {
		
		try {
			
			new GoalsByAgeGraph();
			
		} catch (IOException e) {
			
			LOGGER.debug("Error writing goals by age graph", e);
			
		}
		
	}
	
}
