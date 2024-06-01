package com.example.bloodbank.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloodbank.entity.Donor;
import com.example.bloodbank.service.DonorService;

@RestController
public class DonorController {

	@Autowired
	private DonorService donorService;

	@PostMapping(value = "/addDonor", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Donor> addDonor(@RequestBody Donor donor) {
		Donor donorSaved = donorService.saveDonorDetails(donor);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(donorSaved);
	}

	@PutMapping("/modifyDonor/{id}")
	public ResponseEntity<Donor> modifyDonorDetails(@PathVariable(value = "id") UUID donorId,
			@RequestBody Donor donor) {
		Donor donorSaved = donorService.updateDonor(donorId, donor);
		return ResponseEntity.ok(donorSaved);
	}

	@GetMapping("/getDonors/{bloodGroup}")
	public List<Donor> getDonors(@PathVariable("bloodGroup") String bloodGroup) {
		return donorService.getDonorWithBloodGroup(bloodGroup);
	}

	@DeleteMapping("/deleteDonor/{id}")
	public ResponseEntity<String> deleteDonor(@PathVariable("id") UUID donorId) {
		donorService.deleteDonor(donorId);
		return new ResponseEntity<>("Donor deleted successfully.", HttpStatus.OK);
	}

	@PatchMapping("/modifyDonor/{id}")
	public ResponseEntity<Donor> patchDonor(@PathVariable("id") UUID donorId,
			@RequestBody Donor donor) {
		Donor donorSaved = donorService.patchDonor(donorId, donor);
		return ResponseEntity.ok(donorSaved);
	}

}
