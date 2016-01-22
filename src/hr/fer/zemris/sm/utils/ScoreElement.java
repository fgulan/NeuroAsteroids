package hr.fer.zemris.sm.utils;

import static hr.fer.zemris.sm.game.Constants.*;

import java.util.Comparator;

public class ScoreElement {

	public static final int HUMAN = 0;
	public static final int AI    = 1;

	private static final String delimiter = "#";

	public static final Comparator<ScoreElement> BY_SCORE = (o1,o2) -> (int) Math.signum(o2.getScore() - o1.getScore());

	private int  type;
	private String name;
	private double  score;

	public ScoreElement(int type, String name, double score) {
		super();
		this.type = type;
		this.name = name;
		this.score = score;
	}

	public String getType() {
		return type == AI ? SCORE_ELEMENT_TYPE_AI : SCORE_ELEMENT_TYPE_HUMANE;
	}

	public String getName() {
		return name;
	}

	public double getScore() {
		return score;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getType())
		.append(delimiter)
		.append(name)
		.append(delimiter)
		.append(score);

		return sb.toString();
	}

	public static ScoreElement parse(String row) {
		String[] slitted = row.split(delimiter);
		return new ScoreElement(slitted[0].equals(SCORE_ELEMENT_TYPE_HUMANE) ? HUMAN : AI,
								slitted[1],
								Double.parseDouble(slitted[2]));
	}

}
