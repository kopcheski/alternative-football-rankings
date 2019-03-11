package com.kopcheski.alternative.football.rankings;

public class Position {

	private int position;

	private String team;

	private int points;

	public Position(int position, String team, int points) {
		this.position = position;
		this.team = team;
		this.points = points;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "Position{" +
				"position=" + position +
				", team='" + team + '\'' +
				", points=" + points +
				'}';
	}

}
