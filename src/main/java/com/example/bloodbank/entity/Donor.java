package com.example.bloodbank.entity;

import java.sql.Types;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Donor {

	@Id
	@Column(name = "donor_id")
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(Types.VARCHAR)
	private UUID donorId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "city")
	private String city;

	@Column(name = "blood_group")
	private String bloodGroup;

	@Column(name = "registration_date")
	private Long registrationDate;

	@Column(name = "email")
	private String email;

	public UUID getDonorId() {
		return donorId;
	}

	public void setDonorId(UUID donorId) {
		this.donorId = donorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Donor(String firstName, String lastName, String city, String bloodGroup, Long registrationDate,
			String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.bloodGroup = bloodGroup;
		this.registrationDate = registrationDate;
		this.email = email;
	}

	public Long getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Long registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Donor() {

	}

	@Override
	public int hashCode() {
		return Objects.hash(bloodGroup, city, donorId, email, firstName, lastName, registrationDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Donor other = (Donor) obj;
		return Objects.equals(bloodGroup, other.bloodGroup) && Objects.equals(city, other.city)
				&& Objects.equals(donorId, other.donorId) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(registrationDate, other.registrationDate);
	}
}
