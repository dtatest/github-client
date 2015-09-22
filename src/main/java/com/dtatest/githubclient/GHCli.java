package com.dtatest.githubclient;
/*
 * Simple CLI to invoke GitHub API methods
 */
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.dtatest.githubclient.controller.UserEmailController;
import com.dtatest.githubclient.model.UserEmail;
import com.dtatest.githubclient.model.UserEmails;

public class GHCli {
	private static final Logger log = Logger.getLogger(GHCli.class.getName());
	private String[] args = null;
	private Options options = new Options();

	public GHCli(String[] args) {

		this.args = args;

		//Define CLI options
		options.addOption("h", "help", false, "show help.");
		options.addOption("e", "email", false,
				"Request to work with user emails.");
		options.addOption("g", "get", false, "Get specified parameter.");
		options.addOption("a", "add", true, "Specify parameter value to add.");
		options.addOption("d", "delete", true,
				"Specify parameter value to delete.");
		options.addOption("u", "username", true, "Specify username.");
		options.addOption("p", "password", true, "Specify password.");

	}

	public void parse() {
		CommandLineParser parser = new BasicParser();

		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h"))
				help();
			//Commands related to GitHub User Email
			if (cmd.hasOption("e")) {
				//Commands require authentication with login and password 
				if (cmd.hasOption("u") && cmd.hasOption("p")) {
					UserEmailController userEmailController = new UserEmailController();
					//Command to get current User Email
					if (cmd.hasOption("g")) {
						UserEmails userEmails = userEmailController
								.getUserEmails(cmd.getOptionValue("u"),
										cmd.getOptionValue("p"));
						if (null != userEmails) {
							for (UserEmail userEmail : userEmails) {
								System.out.println(userEmail.getEmail());
							}
						} else {
							log.log(Level.INFO,
									"No emails configured for user "
											+ cmd.getOptionValue("u"));
						}
					}
					//Command to add email addresses - as a comma separated list
					if (cmd.hasOption("a")) {
						List<String> emailAddresses = Arrays.asList(cmd.getOptionValue("a").split(","));
						if (userEmailController.addUserEmails(
								cmd.getOptionValue("u"),
								cmd.getOptionValue("p"),
								emailAddresses)) {
							System.out.println("OK");
						} else {
							System.out.println("Failed");
						}
					}
					//Command to delete email addresses - as a comma separated list
					if (cmd.hasOption("d")) {
						List<String> emailAddresses = Arrays.asList(cmd.getOptionValue("d").split(","));
						if (userEmailController.deleteUserEmails(
								cmd.getOptionValue("u"),
								cmd.getOptionValue("p"),
								emailAddresses)) {
							System.out.println("OK");
						} else {
							System.out.println("Failed");
						}
					}
				} else {
					log.log(Level.SEVERE, "Username and password are required");
				}
			} else {
				log.log(Level.SEVERE, "Currently only email API is supported");
				help();
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to parse comand line arguments", e);
			help();
		}
	}

	private void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("GHCli", options);
		System.exit(0);
	}
}