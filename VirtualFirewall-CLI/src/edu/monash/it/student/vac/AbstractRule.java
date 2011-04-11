package edu.monash.it.student.vac;

public abstract class AbstractRule {
	
	public abstract String toIPTablesRule();

	public String toString() {
		return this.toIPTablesRule();
	}
}
