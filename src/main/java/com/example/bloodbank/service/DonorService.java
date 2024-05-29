package com.example.bloodbank.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bloodbank.entity.Donor;
import com.example.bloodbank.exception.InvalidBloodGroupException;
import com.example.bloodbank.exception.InvalidDataException;
import com.example.bloodbank.repository.DonorRepository;
import com.example.bloodbank.utils.BloodGroup;

@Service
public class DonorService {

	@Autowired
	private DonorRepository donorRepository;
	
	public Donor saveDonorDetails(Donor donor) {
		donor.setRegistrationDate(System.currentTimeMillis());
		validateBloodGroup(donor);
		return donorRepository.save(donor);
	}
	
	public Optional<Donor> findById(UUID donorId) {
		return donorRepository.findById(donorId);
	}
	
	public List<Donor> getDonorWithBloodGroup(String bloodGroup){
		return donorRepository.findByBloodGroup(bloodGroup);
	}
	
	public void deleteDonor(UUID donorId) {
		findById(donorId).orElseThrow(()-> new InvalidDataException("Enter a valid donor id."));
		donorRepository.deleteById(donorId);
	}
	
	public Donor patchDonor(UUID donorId,Donor donor) {
		
		Donor oldDonor = findById(donorId)
				.orElseThrow(()->new InvalidDataException("Invalid donor id: "+donorId));
		
		if(donor.getFirstName()!=null)
			oldDonor.setFirstName(donor.getFirstName());
		if(donor.getLastName()!=null)
			oldDonor.setLastName(donor.getLastName());
		if(donor.getEmail()!=null)
			oldDonor.setEmail(donor.getEmail());
		if(donor.getBloodGroup()!=null)
			oldDonor.setBloodGroup(donor.getBloodGroup());
		if(donor.getRegistrationDate()!=null)
			oldDonor.setRegistrationDate(donor.getRegistrationDate());
		if(donor.getCity()!=null)
			oldDonor.setCity(donor.getCity());
		validateBloodGroup(donor);
		return donorRepository.save(oldDonor);
	}
	
	private void validateBloodGroup(Donor donor) {
		Optional<BloodGroup> bloodGroup = Stream.of(BloodGroup.values())
				.filter(c->c.toString().equals(donor.getBloodGroup()))
				.findFirst();
				if(bloodGroup.isEmpty()) {
					throw new InvalidBloodGroupException();
				}
	}
	
	public Donor updateDonor(UUID donorId, Donor donor) {
		Donor oldDonor = findById(donorId)
                .orElseThrow(() -> new InvalidDataException("Invalid donor id: " + donorId));
		
		oldDonor.setBloodGroup(donor.getBloodGroup());
		oldDonor.setCity(donor.getCity());
		oldDonor.setFirstName(donor.getFirstName());
		oldDonor.setLastName(donor.getLastName());
		oldDonor.setEmail(donor.getEmail());
		oldDonor.setRegistrationDate(donor.getRegistrationDate());
		
		validateBloodGroup(oldDonor);
		return donorRepository.save(oldDonor);
		
	}
}
