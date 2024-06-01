package com.example.bloodbank.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bloodbank.service.ScheduledTaskService;

@Component
public class ScheduledTask {

	@Autowired
	private ScheduledTaskService scheduledService;

	@Scheduled(cron = "0 * * * * *") // Cron expression for running every minute
	public void execute() {
		scheduledService.updateReusability();
	}
}
