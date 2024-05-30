package com.example.bloodbank;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.bloodbank.entity.Donor;
import com.example.bloodbank.exception.InvalidBloodGroupException;
import com.example.bloodbank.repository.DonorRepository;
import com.example.bloodbank.service.DonorService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DonorServiceTest {

	@InjectMocks
	private DonorService donorService;
	
	@Mock
	private DonorRepository donorRepository;
	
	@BeforeEach
	private void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void whenSaveDonorAndValidDonor_thenReturnDonor() {
		Donor donor = new Donor("sunidhi","pandey","Jamnagar","AB-",System.currentTimeMillis(),"213qaz@qa.voqw");
		
		Mockito.when(donorRepository.save(donor)).thenReturn(donor);
		
		Donor donorSaved = donorService.saveDonorDetails(donor);
		
		Assertions.assertThat(donorSaved).isNotNull();
	}
	
	@Test
	public void whenSaveDonorAndInvalidBloodGroup_thenThrowException() {
	
		Donor donor = new Donor("sunidhi","pandey","Jamnagar","F-",System.currentTimeMillis(),"213qaz@qa.voqw");		
		Mockito.when(donorRepository.save(donor)).thenReturn(donor);
		
		org.junit.jupiter.api.Assertions.assertThrows(InvalidBloodGroupException.class, 
				()->{
					donorService.saveDonorDetails(donor);
				});
		
		Mockito.verify(donorRepository,Mockito.never()).save(Mockito.any());
	}

	@Test
	public void whenFindById_thenReturnDonor() {
		UUID donorId = UUID.randomUUID();
		Donor donor = new Donor("sunidhi","pandey","Jamnagar","AB-",System.currentTimeMillis(),"213qaz@qa.voqw");
		donor.setDonorId(donorId);
		
		Mockito.when(donorRepository.findById(donorId)).thenReturn(Optional.of(donor));
		
		Donor donorSaved = donorService.findById(donorId).get();
		
		Assertions.assertThat(donorSaved).isNotNull();
	}
}
