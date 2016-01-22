package hr.fer.zemris.sm.evolution.termination;


import hr.fer.zemris.sm.evolution.EvolutionaryProcess;

public class ConstantTimeTermination implements ITerminationOperator {

	private static final long serialVersionUID = -3263293812447383271L;

	private long finish;
	
	private long durnation;
	
	private boolean hasStarted;
	
	public ConstantTimeTermination(int seconds) {
		durnation = seconds * 1000;
		hasStarted = false;
	}

	@Override
	public boolean isFinished(EvolutionaryProcess process) {
		if(!hasStarted) {
			long start = System.currentTimeMillis();
			finish = start + durnation;
			hasStarted = true;
		}
		
		return finish < System.currentTimeMillis();
	}
}
