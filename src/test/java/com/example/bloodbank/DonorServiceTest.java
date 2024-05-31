package com.example.bloodbank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
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
import com.example.bloodbank.exception.InvalidDataException;
import com.example.bloodbank.repository.DonorRepository;
import com.example.bloodbank.service.DonorService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DonorServiceTest {

	@InjectMocks
	private DonorService donorService;
	
	@Mock
	private DonorRepository donorRepository;
	
	private Donor donor;
	
	@BeforeEach
	private void setup() {
		MockitoAnnotations.initMocks(this);
		donor = new Donor("sunidhi","pandey","Jamnagar","AB-",System.currentTimeMillis(),"213qaz@qa.voqw");
		donor.setDonorId(UUID.randomUUID());
	}
	
	@Test
	public void whenSaveDonorAndValidDonor_thenReturnDonor() {
		Mockito.when(donorRepository.save(donor)).thenReturn(donor);
		
		Donor donorSaved = donorService.saveDonorDetails(donor);
		
		Assertions.assertNotNull(donorSaved);
		Assertions.assertEquals(donor.getFirstName(), donorSaved.getFirstName());
	}
	
	@Test
	public void whenSaveDonorAndInvalidBloodGroup_thenThrowException() {
		donor.setBloodGroup("F-");
		
		Assertions.assertThrows(InvalidBloodGroupException.class, 
				()->{
					donorService.saveDonorDetails(donor);
				});
		
		Mockito.verify(donorRepository,Mockito.never()).save(Mockito.any());
	}

	@Test
	public void whenFindById_thenReturnDonor() {
		UUID donorId = donor.getDonorId();
		
		Mockito.when(donorRepository.findById(donorId)).thenReturn(Optional.of(donor));
		
		Donor donorSaved = donorService.findById(donorId).get();
		
		Assertions.assertEquals(donor,donorSaved);
	}

	@Test
	public void whenGetDonorWithBloodGroup_thenReturnDonorList() {
		Donor donor2 = new Donor("sunny","pandey","Ahmedabad","AB-",System.currentTimeMillis(),"sun@qa.voqw");
		List<Donor> donorList = new ArrayList<>();
		donorList.add(donor);
		donorList.add(donor2);
		
		Mockito.when(donorRepository.findByBloodGroup("AB-")).thenReturn(donorList);
		List<Donor> donorListReceived = donorService.getDonorWithBloodGroup("AB-");
		
		Assertions.assertEquals(donor.getBloodGroup(), donorListReceived.get(0).getBloodGroup());
		Assertions.assertEquals(donor2.getBloodGroup(), donorListReceived.get(1).getBloodGroup());
	}
	
	
	  @Test public void whenDeleteDonor_thenNothing() {
		  Mockito.when(donorRepository.findById(Mockito.any())).thenReturn(Optional.of(donor));
		  Mockito.doNothing().when(donorRepository).deleteById(Mockito.any());
		  donorService.deleteDonor(donor.getDonorId());
		  Mockito.verify(donorRepository,
				  Mockito.times(1)).deleteById(donor.getDonorId()); 
		  }
	
	
	  @Test 
	  public void whenDeleteDonor_thenThrowException() {	  
		  Mockito.when(donorRepository.findById(Mockito.any())).thenThrow(new InvalidDataException("Enter a valid donor id."));
		  Assertions.assertThrows(InvalidDataException.class, () ->
		  		donorService.deleteDonor(donor.getDonorId()));
	  
			Mockito.verify(donorRepository,Mockito.never()).deleteById(donor.getDonorId());
	  }
	 
	 
	
	@Test
	public void whenPatchDonorAndInvalidID_thenThrowException() {
		Mockito.when(donorRepository.save(Mockito.any())).
		thenThrow(new InvalidDataException("Invalid donor id: "+Mockito.any()));
	
		Assertions.assertThrows(InvalidDataException.class, ()->{
			donorService.patchDonor(donor.getDonorId(),donor);			
		});
		
		Mockito.verify(donorRepository,Mockito.never()).save(Mockito.any(Donor.class));
	}
	
	
	  @Test
	  public void whenPatchDonorAndValidDonor_thenGetUpdatedDonor() { 
		  Donor updatedDonor = new Donor("sunny","pandey","Rajkot","AB-",System.currentTimeMillis(),
	  "sun@qa.voqw"); updatedDonor.setDonorId(donor.getDonorId());
	  
	  Mockito.when(donorRepository.findById(Mockito.any())).thenReturn(Optional.of(donor));
	 
	  Mockito.when(donorRepository.save(Mockito.any())).thenReturn(updatedDonor);
	  Donor donorReceived =donorService.patchDonor(donor.getDonorId(),updatedDonor);
	  
	  Assertions.assertEquals(updatedDonor.getRegistrationDate(),donorReceived.getRegistrationDate());
	  Assertions.assertEquals(updatedDonor.getBloodGroup(),donorReceived.getBloodGroup());
	  Assertions.assertEquals(updatedDonor.getEmail(), donorReceived.getEmail());
	  Assertions.assertEquals(updatedDonor.getCity(), donorReceived.getCity());
	  Assertions.assertEquals(updatedDonor.getFirstName(),donorReceived.getFirstName());
	  Assertions.assertEquals(updatedDonor.getLastName(),donorReceived.getLastName());
	  Assertions.assertEquals(updatedDonor.getDonorId(),donorReceived.getDonorId());
	  }
	  
	  @Test
	  public void whenUpdateDonorAndValidDonor_thenReturnDonor() {
		  Donor updatedDonor = new Donor("sunny","pandey","Rajkot","AB-",System.currentTimeMillis(),
				  "sun@qa.voqw"); updatedDonor.setDonorId(donor.getDonorId());
				  
		  Mockito.when(donorRepository.findById(Mockito.any())).thenReturn(Optional.of(donor));
		  Mockito.when(donorRepository.save(Mockito.any())).thenReturn(updatedDonor);
		  
		  Donor donorReceived = donorService.updateDonor(donor.getDonorId(),updatedDonor);
		  
		  Assertions.assertEquals(updatedDonor.getRegistrationDate(),donorReceived.getRegistrationDate());
		  Assertions.assertEquals(updatedDonor.getBloodGroup(),donorReceived.getBloodGroup());
		  Assertions.assertEquals(updatedDonor.getEmail(), donorReceived.getEmail());
		  Assertions.assertEquals(updatedDonor.getCity(), donorReceived.getCity());
		  Assertions.assertEquals(updatedDonor.getFirstName(),donorReceived.getFirstName());
		  Assertions.assertEquals(updatedDonor.getLastName(),donorReceived.getLastName());
		  Assertions.assertEquals(updatedDonor.getDonorId(),donorReceived.getDonorId());				 
	  }
	 
}
