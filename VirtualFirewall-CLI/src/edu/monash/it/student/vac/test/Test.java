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
	public static void main(String[] args) {
        String name = System.getProperty("user.name");
        System.out.println(name);
	}

}
