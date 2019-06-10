package org.hamonsoft.pilot_cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;

public class Clients {

	public static void main(String[] args) {
		
		System.out.println("client start");
		//int portNumber = 10400;
		int portAdd = 0;
		if (args.length == 0) {

			portAdd = 0;
		} else {

			portAdd = Integer.parseInt(args[0]);
		}
		//portNumber = portNumber + portAdd;
		try {
			Socket dsock = null;
			Gson gson = new Gson();
			
			Header rHeader= new Header();
			Header header = new Header();
	
			String sessionKey = null;
			
			while(true) {
				//connect TCP
				//dsock = new Socket("192.168.252.133", 10399);
				
				dsock = new Socket("127.0.0.1", 10400);
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dsock.getOutputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(dsock.getInputStream()));
				
				if(sessionKey == null) {
					header.setCommand("login");
				} else {
					header.setSessionKey(sessionKey);
					header.setCommand("normal");
				}
				bw.write(gson.toJson(header)+"\n");
				bw.flush();
				
				rHeader = gson.fromJson(br.readLine(), Header.class);
				System.out.println(rHeader);
				//abnormal session
				if(rHeader.getCommand().equals("abnormal")) {
					sessionKey = null;
				//upload session
				} else if(rHeader.getCommand().equals("newSession")) {
					sessionKey = rHeader.getSessionKey();
				}
				//close sock
				dsock.close();
				//repleat for 5 second
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}


