package com.amoraesdev.mobify;

import java.io.Console;
import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.amoraesdev.mobify.valueobjects.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

@SpringBootApplication
public class ClientApplication {

	@Autowired
	private ClientOAuth clientOAuth;
	
	@Value("${mobify-server-url}")
	private String mobifyServerUrl;
	
	private String accessToken;
	
	private Console console = System.console();
	private PrintStream out = System.out;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class, args);
		ClientApplication app = context.getBean(ClientApplication.class);
        app.start();
	}
	
	public void start(){
		//get an access token from the oauth server
		this.accessToken = clientOAuth.getAccessToken();
		//show some basic console interface to type the message
		out.println("Mobify Sample Client");
		out.println("Notification type:");
		String type = console.readLine();
		out.println("Notification text:");
		String message = console.readLine();
		out.println("Users (use comma to separate them):");
		String users = console.readLine();
		String[] usersArray = users.split(",");
		sendNotification(usersArray, type, message);
		out.println("---------------");
	}
	
	public void sendNotification(String[] users, String type, String message){
		String url = mobifyServerUrl.concat("/applications/%s/notifications");
		try {
			ObjectMapper mapper = new ObjectMapper();
			Notification notification = new Notification(users, type, message);
			HttpResponse<String> response = Unirest.post(String.format(url, clientOAuth.getClientId()))
				.header("Authorization", "Bearer ".concat(accessToken))
				.header("Content-Type", "application/json")
				.body(mapper.writeValueAsString(notification))
				.asString();
			if(response.getStatus() == 200){
				out.println("Notification successfully sent.");
			}else{
				throw new RuntimeException("Error sending notification. Error code: "+response.getStatus());	
			}
		} catch (Exception e) {
			throw new RuntimeException("Error sending notification", e);
		}
	}
}
