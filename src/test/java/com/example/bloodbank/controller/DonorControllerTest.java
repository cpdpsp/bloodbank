package com.example.bloodbank.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.aMapWithSize;
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

import com.example.bloodbank.entity.Donor;
import com.example.bloodbank.exception.GlobalExceptionHandler;
import com.example.bloodbank.exception.InvalidBloodGroupException;
import com.example.bloodbank.exception.InvalidDataException;
import com.example.bloodbank.service.DonorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class DonorControllerTest {

	@Mock
	private DonorService donorService;

	@InjectMocks
	private DonorController donorController;

	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@BeforeEach
	private void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(donorController).setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	public void addDonorTestSentDonorReceivedDonorWithRegistrationDateTest() throws Exception {

		MediaType applicationJson = new MediaType(MediaType.APPLICATION_JSON);

		Donor donorToBeSent = new Donor();
		donorToBeSent.setCity("Jamnagar");
		donorToBeSent.setBloodGroup("O-");
		donorToBeSent.setEmail("213qaz@qa.voqw");
		donorToBeSent.setFirstName("sunidhi");
		donorToBeSent.setLastName("pandey");

		String donor = objectMapper.writeValueAsString(donorToBeSent);

		Long registrationDate = System.currentTimeMillis();
		donorToBeSent.setRegistrationDate(registrationDate);
		Mockito.when(donorService.saveDonorDetails(Mockito.any())).thenReturn(donorToBeSent);

		mockMvc.perform(MockMvcRequestBuilders.post("/addDonor").contentType(MediaType.APPLICATION_JSON).content(donor))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(applicationJson))
				.andExpect(jsonPath("$", aMapWithSize(7)))
				.andExpect(jsonPath("$.registrationDate", is(registrationDate)));

	}

	@Test
	public void addDonorTestSentDonorReceivedDonorWithoutRegistrationDateTest() throws Exception {

		MediaType applicationJson = new MediaType(MediaType.APPLICATION_JSON);
		Donor donorToBeSent = new Donor();
		donorToBeSent.setCity("Jamnagar");
		donorToBeSent.setBloodGroup("O-");
		donorToBeSent.setEmail("213qaz@qa.voqw");
		donorToBeSent.setFirstName("sunidhi");
		donorToBeSent.setLastName("pandey");

		String donor = objectMapper.writeValueAsString(donorToBeSent);

		Long registrationDate = System.currentTimeMillis();
		Mockito.when(donorService.saveDonorDetails(Mockito.any())).thenReturn(donorToBeSent);

		mockMvc.perform(MockMvcRequestBuilders.post("/addDonor").contentType(MediaType.APPLICATION_JSON).content(donor))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(applicationJson))
				.andExpect(jsonPath("$", aMapWithSize(7))).andExpect(jsonPath("$.registrationDate").doesNotExist());
	}

	@Test
	public void whenPostDonorAndInvalidBloodGroup_thenThrowException() throws Exception {
		Donor donorToBeSent = new Donor("sunidhi", "pandey", "Jamnagar", "U-", System.currentTimeMillis(),
				"213qaz@qa.voqw");
		Mockito.when(donorService.saveDonorDetails(Mockito.any())).thenThrow(new InvalidBloodGroupException());

		mockMvc.perform(MockMvcRequestBuilders.post("/addDonor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(donorToBeSent)))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());

	}

	@Test
	public void whenPutRequestToDonorAndValidDonor_thenCorrectResponse() throws Exception {

		Donor donorToBeSent = new Donor("sunidhi", "pandey", "Jamnagar", "O-", System.currentTimeMillis(),
				"213qaz@qa.voqw");
		Donor updatedDonor = new Donor("sun", "pandey", "Jamnagar", "O+", System.currentTimeMillis(), "qaz@qa.voqw");

		Mockito.when(donorService.updateDonor(Mockito.any(), Mockito.any())).thenReturn(updatedDonor);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyDonor/{id}", UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(donorToBeSent)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", is(updatedDonor.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedDonor.getLastName())))
				.andExpect(jsonPath("$.city", is(updatedDonor.getCity())))
				.andExpect(jsonPath("$.bloodGroup", is(updatedDonor.getBloodGroup())))
				.andExpect(jsonPath("$.registrationDate", is(updatedDonor.getRegistrationDate())))
				.andExpect(jsonPath("$.email", is(updatedDonor.getEmail())));
	}

	@Test
	public void whenGetDonorsForBloodGroupAndValidBloodGroup_thenCorrectResponse() throws Exception {

		List<Donor> donorList = new ArrayList<>();
		Donor donor1 = new Donor("sunidhi", "pandey", "Jamnagar", "O-", System.currentTimeMillis(), "213qaz@qa.voqw");
		Donor donor2 = new Donor("sun", "pandey", "Jamnagar", "O-", System.currentTimeMillis(), "qaz@qa.voqw");
		donorList.add(donor1);
		donorList.add(donor2);

		Mockito.when(donorService.getDonorWithBloodGroup(Mockito.any())).thenReturn(donorList);

		mockMvc.perform(MockMvcRequestBuilders.get("/getDonors/{bloodGroup}", "O-"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].bloodGroup", is(donor1.getBloodGroup())))
				.andExpect(jsonPath("$[0].firstName", is(donor1.getFirstName())))
				.andExpect(jsonPath("$[1].bloodGroup", is(donor2.getBloodGroup())))
				.andExpect(jsonPath("$[1].firstName", is(donor2.getFirstName())));
	}

	@Test
	public void whenDeleteDonorAndValidDonor_thenCorrectResponse() throws Exception {

		Mockito.doNothing().when(donorService).deleteDonor(Mockito.any());
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteDonor/{id}", UUID.randomUUID()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void whenDeleteDonorAndInvaldDonorId_thenThrowException() throws Exception {
		Mockito.doThrow(new InvalidDataException("Enter a valid donor id.")).when(donorService)
				.deleteDonor(Mockito.any());

		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteDonor/{id}", UUID.randomUUID()))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	public void whenPatchRequestToDonorAndValidDonor_thenCorrectResponse() throws Exception {

		Donor donorToBeSent = new Donor("sunidhi", "pandey", "Jamnagar", "O-", System.currentTimeMillis(),
				"213qaz@qa.voqw");
		Donor updatedDonor = new Donor("sun", "pandey", "Jamnagar", "O+", System.currentTimeMillis(), "qaz@qa.voqw");

		Mockito.when(donorService.patchDonor(Mockito.any(), Mockito.any())).thenReturn(updatedDonor);

		mockMvc.perform(MockMvcRequestBuilders.patch("/modifyDonor/{id}", UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(donorToBeSent)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", is(updatedDonor.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedDonor.getLastName())))
				.andExpect(jsonPath("$.city", is(updatedDonor.getCity())))
				.andExpect(jsonPath("$.bloodGroup", is(updatedDonor.getBloodGroup())))
				.andExpect(jsonPath("$.registrationDate", is(updatedDonor.getRegistrationDate())))
				.andExpect(jsonPath("$.email", is(updatedDonor.getEmail())));
	}

}
