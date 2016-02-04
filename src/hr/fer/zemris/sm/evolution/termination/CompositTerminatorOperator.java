package hr.fer.zemris.sm.evolution.termination;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;

/**
 * Class that enables you to have multiple termination operators.
 *
 * It works with OR logic meaning that if one of the termination operators
 * have returned true than {@link #isFinished}  will also return true.
 *
 * Possible improvement is to add support for other kind of logic to this class.
 *
 */
public class CompositTerminatorOperator implements ITerminationOperator{

	private static final long serialVersionUID = -3836285638984245562L;
	private List<ITerminationOperator> operators;

	public CompositTerminatorOperator() {
		super();
		operators = new ArrayList<>();
	}
	
	public void addOperator(ITerminationOperator op) {
		operators.add(op);
	}
	
	public void removeOperator(ITerminationOperator op) {
		operators.remove(op);
	}

	@Override
	public boolean isFinished(EvolutionaryProcess process) {
		for(ITerminationOperator o : operators) {
			if(o.isFinished(process)) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
