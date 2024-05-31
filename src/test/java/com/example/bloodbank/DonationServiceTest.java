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

import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.exception.InvalidDataException;
import com.example.bloodbank.exception.ValidationException;
import com.example.bloodbank.repository.DonationRepository;
import com.example.bloodbank.response.FetchUnitsAvailableResponse;
import com.example.bloodbank.service.DonationService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DonationServiceTest {

	@InjectMocks
	private DonationService donationService;
	
	@Mock
	private DonationRepository donationRepository;
	
	private Donation donation;
	
	@BeforeEach
	private void setup() {
		MockitoAnnotations.initMocks(this);
		donation = new Donation(3f,UUID.randomUUID(),true,System.currentTimeMillis());
		donation.setDonationId(UUID.randomUUID());
	}
	
	@Test
	public void whenSaveDonationAndValidDonation_thenCorretResponse() {
		Mockito.when(donationRepository.numberOfDonationsMadeInLastSevenDays(Mockito.any(),Mockito.any())).thenReturn(0);
		
		Mockito.when(donationRepository.save(Mockito.any())).thenReturn(donation);
		
		Donation donationReceived = donationService.lastDonationMade(donation);
		
		Assertions.assertEquals(donation.getDonatedOn(), donationReceived.getDonatedOn());
		Assertions.assertEquals(donation.getUnitsDonated(), donationReceived.getUnitsDonated());
		Assertions.assertEquals(donation.getDonationId(), donationReceived.getDonationId());
		Assertions.assertEquals(donation.getDonorId(), donationReceived.getDonorId());
		Assertions.assertEquals(donation.isReusable(), donationReceived.isReusable());
	}
	
	@Test
	public void whenSaveDonationAndInvalidDonation_thenThrowException() {
		Mockito.when(donationRepository.numberOfDonationsMadeInLastSevenDays(Mockito.any(),Mockito.any())).thenReturn(1);
		Assertions.assertThrows(ValidationException.class, ()->donationService.lastDonationMade(donation));

		Mockito.verify(donationRepository,Mockito.never()).save(Mockito.any());
	}
	
	@Test
	public void whenGetUnitsAvailable_thenCorrectResponse() {
		List<FetchUnitsAvailableResponse> unitsAvailableList = new ArrayList<>();
		FetchUnitsAvailableResponse unitsAvailable=new FetchUnitsAvailableResponse() {
			
			@Override
			public int getUnitsDonated() {
				// TODO Auto-generated method stub
				return 1;
			}
			
			@Override
			public String getBloodGroup() {
				// TODO Auto-generated method stub
				return "O+";
			}
		};
		
		unitsAvailableList.add(unitsAvailable);
		
		Mockito.when(donationRepository.getUnitsAvailable()).thenReturn(unitsAvailableList);
		
		List<FetchUnitsAvailableResponse> unitsAvailableReceived = donationService.getUnitsAvailable();
		
		Assertions.assertEquals(unitsAvailableList.size(), unitsAvailableReceived.size());
		Assertions.assertEquals(unitsAvailableList.get(0).getUnitsDonated(), unitsAvailableReceived.get(0).getUnitsDonated());
		Assertions.assertEquals(unitsAvailableList.get(0).getBloodGroup(), unitsAvailableReceived.get(0).getBloodGroup());
	}
	
	@Test
	public void whenGetBloodDonated_thenCorrectResponse() {

		Donation donation2 = new Donation(1f,donation.getDonorId(),true,System.currentTimeMillis());
		List<Donation> donationList = new ArrayList<>();
		donationList.add(donation);
		donationList.add(donation2);
		
		Mockito.when(donationRepository.findAllByDonorId(Mockito.any())).thenReturn(donationList);
		
		List<Donation> donationListReceived = donationService.getBloodDonated(donation.getDonorId().toString());
		
		Assertions.assertEquals(donationList.size(), donationListReceived.size());
		Assertions.assertEquals(donationList.get(0).getDonorId(), donationListReceived.get(0).getDonorId());
		Assertions.assertEquals(donationList.get(1).getDonorId(), donationListReceived.get(1).getDonorId());
	}
	
	@Test
	public void whenFindById_thenCorrectResponse() {
		Mockito.when(donationRepository.findById(Mockito.any())).thenReturn(Optional.of(donation));
		Optional<Donation> donationReceived = donationService.findById(donation.getDonationId());
		Assertions.assertNotNull(donationReceived);
		Assertions.assertEquals(donation.getDonatedOn(), donationReceived.get().getDonatedOn());
		Assertions.assertEquals(donation.getDonationId(), donationReceived.get().getDonationId());
		Assertions.assertEquals(donation.getDonorId(), donationReceived.get().getDonorId());
		Assertions.assertEquals(donation.isReusable(), donationReceived.get().isReusable());
		Assertions.assertEquals(donation.getUnitsDonated(), donationReceived.get().getUnitsDonated());
	}
	
	@Test
	public void whenDeleteDonation_thenCorrectResponse() {
		Mockito.when(donationRepository.findById(Mockito.any())).thenReturn(Optional.of(donation));
		Mockito.doNothing().when(donationRepository).deleteById(Mockito.any());
		
		donationService.deleteDonation(donation.getDonationId());
		Mockito.verify(donationRepository,Mockito.times(1)).deleteById(donation.getDonationId()); 
	}
	
	@Test
	public void whenDeleteDonationAndInvaldId_thenThrowException() {
		Mockito.when(donationRepository.findById(Mockito.any())).thenThrow(new InvalidDataException("Enter a valid donation id."));
		Assertions.assertThrows(InvalidDataException.class, ()->donationService.deleteDonation(donation.getDonationId()));

		Mockito.verify(donationRepository,Mockito.never()).deleteById(donation.getDonationId());
	}
	
	@Test
	public void whenGetDonationForThreeMonths_thenCorrectResponse() {
		Donation donation2 = new Donation(1f,donation.getDonorId(),true,System.currentTimeMillis());
		List<Donation> donationList = new ArrayList<>();
		donationList.add(donation);
		donationList.add(donation2);
		
		Mockito.when(donationRepository.getDonationForLastThreeMonths(Mockito.any())).thenReturn(donationList);
		List<Donation> donationListReceived = donationService.getDonationForLastThreeMonths();


		Assertions.assertEquals(donationList.size(), donationListReceived.size());
		Assertions.assertEquals(donationList.get(0).getDonorId(), donationListReceived.get(0).getDonorId());
		Assertions.assertEquals(donationList.get(0).getDonationId(), donationListReceived.get(0).getDonationId());
		Assertions.assertEquals(donationList.get(1).getDonatedOn(), donationListReceived.get(1).getDonatedOn());
		Assertions.assertEquals(donationList.get(1).getUnitsDonated(), donationListReceived.get(1).getUnitsDonated());
		
	}
	
	@Test
	public void whenUpdateBloodReusability_thenReturnNothing() {
		Mockito.doNothing().when(donationRepository).updateBloodReusability(Mockito.any());
		donationService.updateBloodReusability();
		Mockito.verify(donationRepository,Mockito.times(1)).updateBloodReusability(Mockito.any());
	}
	
	@Test
	public void whenPatchDonation_thenReturnDonation() {
		Donation updatedDonation = new Donation(1f,UUID.randomUUID(),true,System.currentTimeMillis());
		Mockito.when(donationRepository.findById(Mockito.any())).thenReturn(Optional.of(donation));
		Mockito.when(donationRepository.save(Mockito.any())).thenReturn(updatedDonation);
		
		Donation donationReceived = donationService.checkUnitsDonated(donation.getDonationId(),updatedDonation);
		Assertions.assertEquals(updatedDonation.getDonatedOn(), donationReceived.getDonatedOn());
		Assertions.assertEquals(updatedDonation.getDonationId(), donationReceived.getDonationId());
		Assertions.assertEquals(updatedDonation.getDonorId(), donationReceived.getDonorId());
		Assertions.assertEquals(updatedDonation.getUnitsDonated(), donationReceived.getUnitsDonated());
		Assertions.assertEquals(updatedDonation.isReusable(), donationReceived.isReusable());
	}
	
	@Test
	public void whenPatchDonationAndInvalidId_thenThrowException() {
		Mockito.when(donationRepository.findById(Mockito.any())).thenThrow(new InvalidDataException("Invalid donation id: " +donation.getDonationId()));
		
		Assertions.assertThrows(InvalidDataException.class, () ->
	  		donationService.checkUnitsDonated(donation.getDonationId(),donation));

		Mockito.verify(donationRepository,Mockito.never()).save(donation);
	}
	
	@Test
	public void whenPatchDonationAndInvalidUnits_thenThrowException() {
		Donation updatedDonation = new Donation(6f,UUID.randomUUID(),true,System.currentTimeMillis());
		Mockito.when(donationRepository.findById(Mockito.any())).thenReturn(Optional.of(donation));
		
		Assertions.assertThrows(ValidationException.class,
		  ()->donationService.checkUnitsDonated(Mockito.any(), updatedDonation));

		Mockito.verify(donationRepository,Mockito.never()).save(updatedDonation);	 
	}
	
	@Test
	public void whenUpdateDonation_thenReturnDonation() {
		Donation updatedDonation = new Donation(1f,UUID.randomUUID(),true,System.currentTimeMillis());
		Mockito.when(donationRepository.findById(Mockito.any())).thenReturn(Optional.of(donation));
		Mockito.when(donationRepository.save(Mockito.any())).thenReturn(updatedDonation);
		
		Donation donationReceived = donationService.updateDonation(donation.getDonationId(),updatedDonation);
		Assertions.assertEquals(updatedDonation.getDonatedOn(), donationReceived.getDonatedOn());
		Assertions.assertEquals(updatedDonation.getDonationId(), donationReceived.getDonationId());
		Assertions.assertEquals(updatedDonation.getDonorId(), donationReceived.getDonorId());
		Assertions.assertEquals(updatedDonation.getUnitsDonated(), donationReceived.getUnitsDonated());
		Assertions.assertEquals(updatedDonation.isReusable(), donationReceived.isReusable());
	}
}
