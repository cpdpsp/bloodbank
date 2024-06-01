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
import com.example.bloodbank.response.UnitsAvailableByBloodGroup;
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
	public ResponseEntity<List<UnitsAvailableByBloodGroup>> getUnitsAvailable() {
		List<UnitsAvailableByBloodGroup> unitsAvailableList = donationService.getUnitsAvailable();
		return new ResponseEntity<>(unitsAvailableList, HttpStatus.OK);
	}

	@GetMapping("/getBloodDonated/{id}")
	public ResponseEntity<List<Donation>> getBloodDonated(@PathVariable(value = "id") String donorId) {
		List<Donation> donationList = donationService.getBloodDonated(donorId);
		return new ResponseEntity<>(donationList, HttpStatus.OK);
	}

	@PutMapping("/modifyDonation/{id}")
	public ResponseEntity<Donation> modifyDonationDetails(@PathVariable(value = "id") UUID donationId,
			@Valid @RequestBody Donation donation) {
		Donation donationSaved = donationService.updateDonation(donationId, donation);
		return new ResponseEntity<>(donationSaved, HttpStatus.OK);
	}

	@DeleteMapping("/deleteDonation/{id}")
	public ResponseEntity<String> deleteDonation(@PathVariable("id") UUID donationId) {
		donationService.deleteDonation(donationId);
		return new ResponseEntity<>("Donation deleted successfully.", HttpStatus.OK);
	}

	@GetMapping("/getDonationForLastThreeMonths")
	public ResponseEntity<List<Donation>> getDonationForLastThreeMonths() {
		List<Donation> donationList = donationService.getDonationForLastThreeMonths();
		return new ResponseEntity<>(donationList, HttpStatus.OK);
	}

	@PatchMapping("/modifyDonation/{id}")
	public ResponseEntity<Donation> patchDonation(@PathVariable(value = "id") UUID donationId,
			@RequestBody Donation donation) {
		Donation donationSaved = donationService.validateAndSaveDonation(donationId, donation);
		return new ResponseEntity<>(donationSaved, HttpStatus.OK);
	}
}
