package com.kopcheski.alternative.football.rankings;

import com.kopcheski.alternative.football.rankings.model.matches.MatchesData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;

public class AtpStyle {

	private BiFunction<String, Integer, Integer> computeDraw = (key, oldValue) -> oldValue == null ? 1 : oldValue + 1;

	private BiFunction<String, Integer, Integer> computeVictory = (key, oldValue) -> oldValue == null ? 3 : oldValue + 3;

	private final MatchesData matchesData;

	private int position;

	public AtpStyle(MatchesData matchesData) {
		this.matchesData = matchesData;
		this.position = 1;
	}

	public List<Position> classification() {
		Map<String, Integer> teamPoints = new HashMap<>();
		matchesData.getMatches().forEach(match -> {
			String away = match.getAwayTeam().getName();
			String home = match.getHomeTeam().getName();
			String winner = match.getScore().getWinner();
			if ("DRAW".equals(winner)) {
				teamPoints.compute(away, computeDraw);
				teamPoints.compute(home, computeDraw);
			} else if ("HOME_TEAM".equals(winner)) {
				teamPoints.compute(home, computeVictory);
			} else {
				teamPoints.compute(away, computeVictory);
			}
		});

		return teamPoints.entrySet()
				.stream()
				.sorted(reverseOrder(comparingByValue()))
				.map(this::getPosition)
				.collect(Collectors.toList());
	}

	private Position getPosition(Map.Entry<String, Integer> element) {
		return new Position(position++, element.getKey(), element.getValue());
	}

}
