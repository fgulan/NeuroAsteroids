package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.EPListener;
import hr.fer.zemris.sm.evolution.IEvolutionaryProcess;

public class PrintErrorListener implements EPListener{

	private static final long serialVersionUID = -708876249879816237L;

	@Override
	public void listen(IEvolutionaryProcess process) {
		//System.out.println("Error: " + (1 / process.getPopulation().getBest().getFitness()));
	}

	
	
}
