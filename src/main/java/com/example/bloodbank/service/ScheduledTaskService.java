package com.example.bloodbank.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

	@Autowired
	private DonationService donationService;

	// Update the reusable status if the blood was donated more than one month ago.
	public void updateReusability() {
		System.out.println("Scheduled task executed at: " + new Date());
		donationService.updateBloodReusability();
	}
}
