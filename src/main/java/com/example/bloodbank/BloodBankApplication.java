package com.example.bloodbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
Design and implement blood bank management system. Consider the following requirements:
I want to have build this in Java application which can be plugged directly to frontend. Blood donation will be considered in units.Anytime that a donation is made , store this information at a donor level. I also want to see at a time, how much blood in units do I have in my system at any point across different blood groups. If a donor is not present in system, register the donor first , essential details for the donor would be first name,last name, city, blood group, registration date. For a given donor I want to understand how many times he has donated blood in the past. If mistakenly wrong entry was made for Donation or while registering the donor, please add functionalities to ensure that I can also update these accordingly. I also want to search all the donors based on a particular blood group type.Provide a delete functionality as well in case I want to remove a donor or donation entry. Use persistent datastore for this.
In case of any queries reach out to me. Before starting on the coding part be sure that the database design is able to incorporate all of the above efficiently.
Once you are done with the above, think of the enhancements to be made as mentioned below:Enhancements:1. Change the donation report w.r.t a donor to reflect how may times a donation was done in past 3 months and not beyond2. Add a functionality to mark the blood as not reusable 1 month after the donation was made3. Add unit test cases for every scenario4. Add receiver side details as well, register a receiver and when blood is received do the needful5. Throw error when donation is added > 3 units 6. Throw error if last donation by a donor is made in last 7 days and again a donation is being added against the donor.
 */
@SpringBootApplication
//@EnableSchedulings
public class BloodBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloodBankApplication.class, args);
	}
}
