package com.example.bloodbank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.bloodbank.controller.DonationController;
import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.exception.GlobalExceptionHandler;
import com.example.bloodbank.exception.InvalidDataException;
import com.example.bloodbank.exception.ValidationException;
import com.example.bloodbank.response.FetchUnitsAvailableResponse;
import com.example.bloodbank.service.DonationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class DonationControllerTest {

	@Mock
	private DonationService donationService;
	
	@InjectMocks
	private DonationController donationController;
	
	private MockMvc mockMvc;

    @Autowired
	ObjectMapper objectMapper;
    
    @BeforeEach
	private void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc= MockMvcBuilders.standaloneSetup(donationController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}
    
    @Test
    public void whenPostRequestToDonationAndValidDonation_thenCorrectResponse() throws Exception{
    	Donation donation = new Donation(1f,UUID.randomUUID(),true);
    	String donationRequest = objectMapper.writeValueAsString(donation);
    	donation.setDonatedOn(System.currentTimeMillis());
    	
    	Mockito.when(donationService.lastDonationMade(Mockito.any())).thenReturn(donation);
    	
    	mockMvc.perform(MockMvcRequestBuilders.post("/addDonation").contentType(MediaType.APPLICATION_JSON).content(donationRequest))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(jsonPath("$", aMapWithSize(5)))
    	.andExpect(jsonPath("$.donatedOn",is(donation.getDonatedOn())));
    }

    @Test
    public void whenPostRequestToDonationAndInvalidDonation_thenThrowException() throws Exception{
    	Donation donation = new Donation(2f,UUID.randomUUID(),false,System.currentTimeMillis());
    	String donationRequest = objectMapper.writeValueAsString(donation);
    	
    	Mockito.when(donationService.lastDonationMade(Mockito.any())).thenThrow(new ValidationException("Donation has been made in last 7 days."));
    	mockMvc.perform(MockMvcRequestBuilders.post("/addDonation").contentType(MediaType.APPLICATION_JSON).content(donationRequest))
    	.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    
    @Test
    public void whenGetUnitsAvailable_thenCorrectResponse() throws Exception{

    	List<FetchUnitsAvailableResponse> unitsList = new ArrayList<>();
    	FetchUnitsAvailableResponse unitsAvailable1 = new FetchUnitsAvailableResponse() {
			
			@Override
			public int getUnitsDonated() {
				// TODO Auto-generated method stub
				return 1;
			}
			
			@Override
			public String getBloodGroup() {
				// TODO Auto-generated method stub
				return "A+";
			}
		};
		
		FetchUnitsAvailableResponse unitsAvailable2 = new FetchUnitsAvailableResponse() {
			
			@Override
			public int getUnitsDonated() {
				// TODO Auto-generated method stub
				return 4;
			}
			
			@Override
			public String getBloodGroup() {
				// TODO Auto-generated method stub
				return "O+";
			}
		};
		unitsList.add(unitsAvailable1);
		unitsList.add(unitsAvailable2);
    	
    	Mockito.when(donationService.getUnitsAvailable()).thenReturn(unitsList);
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/getUnitsAvailable"))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andExpect(jsonPath("$[0].bloodGroup",is(unitsAvailable1.getBloodGroup())))
    	.andExpect(jsonPath("$[1].bloodGroup",is(unitsAvailable2.getBloodGroup())))
    	.andExpect(jsonPath("$[0].unitsDonated",is(unitsAvailable1.getUnitsDonated())))
    	.andExpect(jsonPath("$[1].unitsDonated",is(unitsAvailable2.getUnitsDonated())));
    }
    
    @Test
    public void whenGetBloodDonatedAndValidId_thenCorrectResponse() throws Exception{
    	UUID donorId = UUID.randomUUID();

    	Donation donation1 = new Donation(1f,donorId,true);
    	Donation donation2 = new Donation(2f,donorId,true);
    	List<Donation> donationList = new ArrayList<>();
    	donationList.add(donation1);
    	donationList.add(donation2);

    	Mockito.when(donationService.getBloodDonated(Mockito.any())).thenReturn(donationList);

    	mockMvc.perform(MockMvcRequestBuilders.get("/getBloodDonated/{id}",donorId))
    	    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andExpect(jsonPath("$[0].donorId",equalTo(donation1.getDonorId().toString())))
    	.andExpect(jsonPath("$[1].donorId",equalTo(donation2.getDonorId().toString())))
    	.andExpect(jsonPath("$[0].unitsDonated").value(donation1.getUnitsDonated()))
    	.andExpect(jsonPath("$[1].unitsDonated").value(donation2.getUnitsDonated()));
       	
    }
    
    @Test
    public void whenPutDonationAndValidDonation_thenCorrectResponse() throws Exception{
    	UUID donationId = UUID.randomUUID();
    	Donation donation = new Donation(1f,UUID.randomUUID(),true,System.currentTimeMillis());
    	donation.setDonationId(donationId);
    	Donation modifiedDonation = new Donation(2f,UUID.randomUUID(),false,System.currentTimeMillis());
    	modifiedDonation.setDonationId(donationId);
    	

    	Mockito.when(donationService.updateDonation(Mockito.any(),Mockito.any())).thenReturn(modifiedDonation);
    	
    	
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyDonation/{id}",donationId).
				contentType(MediaType.APPLICATION_JSON).
				content(objectMapper.writeValueAsString(donation)))
		.andExpect(jsonPath("$",aMapWithSize(5)))
		.andExpect(jsonPath("$.unitsDonated").value(modifiedDonation.getUnitsDonated()))
		.andExpect(jsonPath("$.donorId",equalTo(modifiedDonation.getDonorId().toString())))
		.andExpect(jsonPath("$.donatedOn",is(modifiedDonation.getDonatedOn())))
		.andExpect(jsonPath("$.reusable",is(modifiedDonation.isReusable())));
    }
    
    @Test
    public void whenPutDonationAndInvalidDonationId_thenThrowException() throws Exception{
    	UUID donationId = UUID.randomUUID();
    	Donation donation = new Donation(1f,UUID.randomUUID(),true,System.currentTimeMillis());
    	donation.setDonationId(donationId);
    	Donation modifiedDonation = new Donation(2f,UUID.randomUUID(),false,System.currentTimeMillis());
    	modifiedDonation.setDonationId(donationId);
    	
    	
    	Mockito.when(donationService.updateDonation(Mockito.any(),Mockito.any())).
    	thenThrow(new InvalidDataException("Invalid donation id: " + donationId));
    
    	mockMvc.perform(MockMvcRequestBuilders.put("/modifyDonation/{id}",donationId))
    	.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    
    @Test
    public void whenDeleteDonationAndValid_thenCorrectResponse() throws Exception{

		Mockito.doNothing().when(donationService).deleteDonation(Mockito.any());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteDonation/{id}",UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void whenGetDonationForLastThreeMonths_thenCorrectResponse() throws Exception{
    	Donation donation1 = new Donation(1f,UUID.randomUUID(),true,System.currentTimeMillis());
    	Donation donation2 = new Donation(2f,UUID.randomUUID(),true,System.currentTimeMillis());
    	
    	List<Donation> donationList = new ArrayList<>();
    	donationList.add(donation1);
    	donationList.add(donation2);
    	
    	Mockito.when(donationService.getDonationForLastThreeMonths()).thenReturn(donationList);

    	mockMvc.perform(MockMvcRequestBuilders.get("/getDonationForLastThreeMonths"))
    	.andExpect(jsonPath("$",hasSize(2)))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andExpect(jsonPath("$[0].donatedOn",is(donation1.getDonatedOn())))
    	.andExpect(jsonPath("$[1].donatedOn",is(donation2.getDonatedOn())));
    }
    
    @Test
    public void whenPatchDonationAndValidDonation_thenCorrectResponse() throws Exception{
    	Donation donation = new Donation(1f,UUID.randomUUID(),true,System.currentTimeMillis());
    	Donation modifiedDonation = new Donation(2f,UUID.randomUUID(),false,System.currentTimeMillis());
    
    	Mockito.when(donationService.checkUnitsDonated(Mockito.any(),Mockito.any())).thenReturn(modifiedDonation);
    	
    	mockMvc.perform(MockMvcRequestBuilders.patch("/partialModificationDonation/{id}",UUID.randomUUID())
    			.content(objectMapper.writeValueAsString(donation)).contentType(MediaType.APPLICATION_JSON))
    	.andExpect(jsonPath("$",aMapWithSize(5)))
		.andExpect(jsonPath("$.unitsDonated").value(modifiedDonation.getUnitsDonated()))
		.andExpect(jsonPath("$.donorId",equalTo(modifiedDonation.getDonorId().toString())))
		.andExpect(jsonPath("$.donatedOn",is(modifiedDonation.getDonatedOn())))
		.andExpect(jsonPath("$.reusable",is(modifiedDonation.isReusable())));
    }
}
