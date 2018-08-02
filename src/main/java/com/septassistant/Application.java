package com.septassistant;

import java.util.Scanner;

import com.septassistant.model.Result;
import com.septassistant.model.SeptaRoute;
import com.septassistant.model.UberRoute;

public class Application {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter your starting address: ");
		String origin = sc.nextLine();
		
		System.out.println("Enter your destination address: ");
		String destination = sc.nextLine();
	
		sc.close();

		UberRoute uberRoute = Uber.getUberRoute(origin, destination);
		SeptaRoute septaRoute = Septa.getSeptaRoute(origin, destination);	
		
		new Result().uberRoute(uberRoute).septaRoute(septaRoute);
	}
}
