package com.example.bloodbank.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.exception.InvalidDataException;
import com.example.bloodbank.exception.ValidationException;
import com.example.bloodbank.repository.DonationRepository;
import com.example.bloodbank.response.UnitsAvailableByBloodGroup;

@Service
public class DonationService {

	@Autowired
	private DonationRepository donationRepository;

	// Save donation details if the donor has not made a donation in the last 7
	// days.
	public Donation lastDonationMade(Donation donation) {
		donation.setCanUse(true);
		donation.setDonatedOn(System.currentTimeMillis());

		if (donationRepository.getNumberOfDonationsMadeInLastSevenDays(donation.getDonorId().toString(),
				System.currentTimeMillis()) > 0) {
			throw new ValidationException(
					"Donation has been made in last 7 days. Please donate 7 days after the donation was made");
		}

		return saveDonationDetails(donation);
	}

	public Donation saveDonationDetails(Donation donation) {
		return donationRepository.save(donation);
	}

	public List<UnitsAvailableByBloodGroup> getUnitsAvailable() {
		return donationRepository.getUnitsAvailable();
	}

	public List<Donation> getBloodDonated(String donorId) {
		return donationRepository.findAllByDonorId(donorId);
	}

	public Optional<Donation> findById(UUID id) {
		return donationRepository.findById(id);
	}

	public void deleteDonation(UUID donationId) {
		findById(donationId).orElseThrow(() -> new InvalidDataException(("Enter a valid donation id.")));
		donationRepository.deleteById(donationId);
	}

	// Get all the donations made within last three months.
	public List<Donation> getDonationForLastThreeMonths() {
		return donationRepository.getDonationForLastThreeMonths(System.currentTimeMillis());
	}

	// Update the reusable status if the blood was donated more than one month ago.
	public void updateBloodReusability() {
		donationRepository.updateBloodReusability(System.currentTimeMillis());
	}

	// Check if the donation has valid donation ID and if the units donated are
	// valid before updating the details. In case someone wants to update only a few
	// donation details, rest will
	// still be preserved.
	public Donation validateAndSaveDonation(UUID donationId, Donation donation) {
		Donation oldDonation = findById(donationId)
				.orElseThrow(() -> new InvalidDataException("Invalid donation id: " + donationId));

		if (donation.getDonatedOn() != null)
			oldDonation.setDonatedOn(donation.getDonatedOn());
		if (donation.getDonorId() != null)
			oldDonation.setDonorId(donation.getDonorId());
		if (donation.getUnitsDonated() != null)
			oldDonation.setUnitsDonated(donation.getUnitsDonated());
		if (donation.getCanUse() != null)
			oldDonation.setCanUse(donation.getCanUse());
		if (oldDonation.getUnitsDonated() < 1)
			throw new ValidationException("Blood donated should not be less than 1 unit.");
		else if (oldDonation.getUnitsDonated() > 3)
			throw new ValidationException("Blood donated should not be more than 3 units.");

		return saveDonationDetails(oldDonation);
	}

	// Check if the donation has valid donation ID before updating the details.
	// All the fields will be updated based on the data received.
	public Donation updateDonation(UUID donationId, Donation donation) {
		Donation oldDonation = findById(donationId)
				.orElseThrow(() -> new InvalidDataException("Invalid donation id: " + donationId));
		if (donation.getCanUse() == null || donation.getDonatedOn() == null)
			throw new ValidationException(
					"Please provide all the details necessary to successfully update the donation.");

		oldDonation.setDonatedOn(donation.getDonatedOn());
		oldDonation.setDonorId(donation.getDonorId());
		oldDonation.setUnitsDonated(donation.getUnitsDonated());
		oldDonation.setCanUse(donation.getCanUse());
		return saveDonationDetails(oldDonation);
	}
}
