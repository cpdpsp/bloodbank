package com.example.bloodbank.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.exception.InvalidDataException;
import com.example.bloodbank.exception.ValidationException;
import com.example.bloodbank.repository.DonationRepository;
import com.example.bloodbank.response.FetchUnitsAvailableResponse;

@Service
public class DonationService {

	@Autowired
	private DonationRepository donationRepository; 
	
	public Donation lastDonationMade(Donation donation) {
		donation.setReusable(true);
		donation.setDonatedOn(System.currentTimeMillis());
		
		if(donationRepository.donationInLastSevenDays(donation.getDonorId().toString(),System.currentTimeMillis())>0) {
			throw new ValidationException("Donation has been made in last 7 days.");
		}
		
		return saveDonationDetails(donation);
	}
	public Donation saveDonationDetails(Donation donation) {
		
		return donationRepository.save(donation);
	}
	
	public List<FetchUnitsAvailableResponse> getUnitsAvailable() {
		return donationRepository.getUnitsAvailable();
	}
	
	public List<Donation> getBloodDonated(String donorId){
		return donationRepository.findAllByDonorId(donorId);
	}
	
	public Optional<Donation> findById(UUID id) {
		return donationRepository.findById(id);
	}
	
	public void deleteDonation(UUID donationId) {
		findById(donationId).orElseThrow(()->new InvalidDataException(("Enter a valid donation id.")));
		donationRepository.deleteById(donationId);
	}
	
	public List<Donation> getDonationForLastThreeMonths(){
		return donationRepository.getDonationForLastThreeMonths(System.currentTimeMillis());
	}
	
	public void updateBloodReusability() {
		donationRepository.updateBloodReusability(System.currentTimeMillis());
	}
	public Donation checkUnitsDonated(UUID donationId,Donation donation) {
		Donation oldDonation = findById(donationId)
				  .orElseThrow(()->new InvalidDataException("Invalid donation id: " +donationId));
				 
		 if(donation.getDonatedOn()!=null)
			  oldDonation.setDonatedOn(donation.getDonatedOn());
		  if(donation.getDonorId()!=null)
			  oldDonation.setDonorId(donation.getDonorId());
		  if(donation.getUnitsDonated()!=null)
			  oldDonation.setUnitsDonated(donation.getUnitsDonated());
		  if(donation.isReusable()!=null)
			  oldDonation.setReusable(donation.isReusable());
		if(oldDonation.getUnitsDonated()<1)
			throw new ValidationException("Blood donated should not be less than 1 unit.");
		else if(oldDonation.getUnitsDonated()>3)
			throw new ValidationException("Blood donated should not be more than 3 units.");
		
		return saveDonationDetails(oldDonation);
	}
	
	public Donation updateDonation(UUID donationId ,Donation donation) {
		Donation oldDonation = findById(donationId)
				.orElseThrow(() -> new InvalidDataException("Invalid donation id: " + donationId));
		
		oldDonation.setDonatedOn(donation.getDonatedOn());
		oldDonation.setDonorId(donation.getDonorId());
		oldDonation.setUnitsDonated(donation.getUnitsDonated());
		oldDonation.setReusable(donation.isReusable());
		return saveDonationDetails(oldDonation);
	}
}
