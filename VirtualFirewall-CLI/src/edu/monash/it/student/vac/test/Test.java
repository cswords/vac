package edu.monash.it.student.vac.test;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.monash.it.student.vac.*;
import edu.monash.it.student.vac.AccessControlRule.Operation;
import edu.monash.it.student.vac.Identity.IdentityType;
import edu.monash.it.student.vac.NetworkService.Protocol;
import edu.monash.it.student.vac.configuration.Context;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		RulePool pool = Context.getCurrent().getRulePool();

		AccessControlRule rule = new AccessControlRule();

		// set operation
		rule.setOperation(Operation.DROP);
		// set source
		rule.setSource(Identity.Instance(IdentityType.User, "xyqin1"));
		// set target
		NetworkService target = new NetworkService();
		target.setBaseProtocol(Protocol.TCP);
		target.setDescription("Test Web");
		// notice: see the method, 'testhost' will never work because iptables will seek the ip first.
		target.setSocketAddress(InetSocketAddress.createUnresolved("testhost",
				80));
		target.setId(0);
		rule.setTarget(target);
		

		NetworkService target2 = new NetworkService();
		target2.setBaseProtocol(Protocol.TCP);
		target2.setDescription("Test Web");
		target2.setSocketAddress(InetSocketAddress.createUnresolved("testhost",
				81));
		target2.setId(0);
		//rule.setTarget(target2);

		pool.addService(target);
		pool.addService(target2);

		System.out.println(pool.toIPTablesRule());
		System.out.println("////////////////////////////");
		System.out.println(pool.toString());
		
		Context.getCurrent().saveConfiguration();
	}

}
