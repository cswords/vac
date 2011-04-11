package edu.monash.it.student.vac.test;

import java.net.InetSocketAddress;

import edu.monash.it.student.vac.*;
import edu.monash.it.student.vac.AccessControlRule.Operation;
import edu.monash.it.student.vac.Identity.IdentityType;
import edu.monash.it.student.vac.NetworkService.Protocol;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AccessControlRule rule = new AccessControlRule();
		rule.setOperation(Operation.DROP);
		rule.setSource(Identity.Instance(IdentityType.User, "xyqin1"));
		NetworkService target = new NetworkService();
		target.setBaseProtocol(Protocol.TCP);
		target.setDescription("Test Web");
		target.setSocketAddress(InetSocketAddress.createUnresolved("testhost",
				80));
		target.setId(0);
		rule.setTarget(target);
		System.out.println(rule);
	}

}
