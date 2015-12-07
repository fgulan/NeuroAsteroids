package hr.fer.zemris.game.Utils;

public class EvolutionElement {

	private String name;
	private double fitness;
	private String comment;
	
	public EvolutionElement(String name, double fitness, String comment) {
		super();
		this.name = name;
		this.fitness = fitness;
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name)
		.append(" ")
		.append(fitness)
		.append(" ")
		.append(comment);
		
		return sb.toString();	
	}
	
	public static EvolutionElement parse(String row){
		String[] splitted = row.split(" ");
		return new EvolutionElement(splitted[0],
									Double.parseDouble(splitted[1]),
									splitted[2]);
	}

	public String getName() {
		return name;
	}

	public double getFitness() {
		return fitness;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EvolutionElement that = (EvolutionElement) o;
		return !(name != null ? !name.equals(that.name) : that.name != null);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
