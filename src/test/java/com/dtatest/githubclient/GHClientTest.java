package com.dtatest.githubclient;

import java.util.Arrays;
import java.util.List;

import com.dtatest.githubclient.controller.UserEmailController;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class GHClientTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GHClientTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GHClientTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testUserEmailController()
    {
    	UserEmailController userEmailController = new UserEmailController();
    	String username = System.getProperty("username"); 	
    	String password = System.getProperty("password");
    	List<String> emailAddresses = Arrays.asList("dtatest@gmail.com");
    	
    	//Get number of current emails
    	int origSize = userEmailController.getUserEmails(username, password).size();
    	//Add one more email
    	assertTrue(userEmailController.addUserEmails(username, password, emailAddresses));
    	//Get number of emails after adding one 
    	int plusOneSize = userEmailController.getUserEmails(username, password).size();
    	//Verify that only one was added
        assertEquals( plusOneSize, origSize + 1);
        //Remove one email
        assertTrue(userEmailController.deleteUserEmails(username, password, emailAddresses));
    	int minusOneSize = userEmailController.getUserEmails(username, password).size();
        //Verify only one email was removed
       assertEquals( minusOneSize, origSize);       
    }
}
