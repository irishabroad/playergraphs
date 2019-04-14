package com.irishabroad.playergraphs.goalsbycaps;

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

public class GoalsByCapsGraph {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandle.class.getName());

	public GoalsByCapsGraph() throws IOException {
		
		List<PlayerStats> playerGoalsByMonth = new ArrayList<>();
		URL url = Resources.getResource("playerGoalsByCap.txt");
		String fileContents = Resources.toString(url, StandardCharsets.UTF_8);
		String[] lines = fileContents.split("\n");
		Arrays.stream(lines).forEach(l -> playerGoalsByMonth.add(new PlayerStats(l)));
		int caps = 1;
		List<String> filenames = new ArrayList<>();
		for (int counter = 0; counter < 146; counter++) {
		
			List<Integer> goals = new ArrayList<>();
			List<String> names = new ArrayList<>();
			for (PlayerStats goalsByMonth: playerGoalsByMonth) {
				goals.add(goalsByMonth.getStats().get(counter));
				names.add(goalsByMonth.getName());
			}
			PlayerGoalsByCapsImage playerGoalsImage = new PlayerGoalsByCapsImage(names, goals, caps++);
			try {
				
				playerGoalsImage.generateImage(counter);
				filenames.add(playerGoalsImage.getImage().getAbsolutePath());
				
			} catch (IOException e) {
				
				LOGGER.debug("Error writing goals graph", e);
				
			}
			
		}
		Giffer.generateFromFiles(filenames.toArray(new String[filenames.size()]), "goalsByCapsGif.gif", 10, false);
		
	}
	
	public static void main(String...args) {
		
		try {
			
			new GoalsByCapsGraph();
			
		} catch (IOException e) {
			
			LOGGER.debug("Error creating goals by caps graph", e);
			
		}
		
	}
	
}
