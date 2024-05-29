package com.example.bloodbank.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.exception.ValidationException;
import com.example.bloodbank.response.FetchUnitsAvailableResponse;
import com.example.bloodbank.service.DonationService;

import jakarta.validation.Valid;

@RestController
public class DonationController {

	@Autowired
	private DonationService donationService;

	@PostMapping("/addDonation")
	public ResponseEntity<Donation> addDonation(@Valid @RequestBody Donation donation) {
		Donation donationSaved = donationService.lastDonationMade(donation);
		return new ResponseEntity<>(donationSaved, HttpStatus.OK);
	}

	@GetMapping("/getUnitsAvailable")
	public List<FetchUnitsAvailableResponse> getUnitsAvailable() {
		return donationService.getUnitsAvailable();
	}

	@GetMapping("/getBloodDonated/{id}")
	public List<Donation> getBloodDonated(@PathVariable(value = "id") String donorId) {
		return donationService.getBloodDonated(donorId);
	}

	@PutMapping("/modifyDonation/{id}")
	public ResponseEntity<Donation> modifyDonationDetails(@PathVariable(value = "id") UUID donationId,
			@Valid @RequestBody Donation donation) {
		Donation donationSaved = donationService.updateDonation(donationId,donation);

		return ResponseEntity.ok(donationSaved);
	}

	@DeleteMapping("deleteDonation/{id}")
	public ResponseEntity<String> deleteDonation(@PathVariable("id") UUID donationId) {
		donationService.deleteDonation(donationId);
		return new ResponseEntity<>("Donation deleted successfully.", HttpStatus.OK);
	}

	@GetMapping("getDonationForLastThreeMonths")
	List<Donation> getDonationForLastThreeMonths() {
		return donationService.getDonationForLastThreeMonths();
	}

	
	@PatchMapping("partialModificationDonation/{id}")
	public ResponseEntity<Donation> partialModificationDonation(
	@PathVariable(value="id") UUID donationId, @RequestBody Donation donation) { 
	  Donation donationSaved = donationService.checkUnitsDonated(donationId,donation);
	  return ResponseEntity.ok(donationSaved); 
	  }
	 
}