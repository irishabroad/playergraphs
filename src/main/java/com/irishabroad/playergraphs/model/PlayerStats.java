package com.irishabroad.playergraphs.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class PlayerStats {
	
	private String name;
	private List<Integer> stats;
	
	public PlayerStats(@Nonnull String inputLine) {
		
		String[] csvArray = inputLine.split("\t");
		this.name = csvArray[0];
		this.stats = new ArrayList<>();
		for (int counter = 1; counter < csvArray.length; counter ++) {
			
			stats.add(Integer.valueOf(csvArray[counter]));
			
		}
		
	}
	
	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public List<Integer> getStats() {
		return stats;
	}

	@Override
	public String toString() {
		
		StringBuilder retVal = new StringBuilder(name);
		stats.stream().forEach(gBM -> retVal.append("\t" + gBM));
		return retVal.toString();
		
	}
	
}
