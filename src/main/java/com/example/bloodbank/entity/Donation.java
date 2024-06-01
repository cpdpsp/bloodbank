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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "donation")
public class Donation {

	@Id
	@Column(name = "donation_id")
	@JdbcTypeCode(Types.VARCHAR)
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID donationId;

	@Column(name = "donated_on")
	private Long donatedOn;

	@Min(value = 1, message = "Blood donated should not be less than 1 unit.")
	@Max(value = 3, message = "Blood donated should not be more than 3 units.")
	@Column(name = "units_donated")
	private Float unitsDonated;

	@JdbcTypeCode(Types.VARCHAR)
	@Column(name = "donor_id")
	@NotNull(message = "Enter donor id.")
	private UUID donorId;

	// Blood is not usable after one month from the donation date.
	@Column(name = "reusable")
	private Boolean reusable;

	public Boolean isReusable() {
		return reusable;
	}

	public void setReusable(Boolean reusable) {
		this.reusable = reusable;
	}

	public UUID getDonationId() {
		return donationId;
	}

	public void setDonationId(UUID donationId) {
		this.donationId = donationId;
	}

	public Long getDonatedOn() {
		return donatedOn;
	}

	public void setDonatedOn(Long donatedOn) {
		this.donatedOn = donatedOn;
	}

	public Float getUnitsDonated() {
		return unitsDonated;
	}

	public void setUnitsDonated(Float unitsDonated) {
		this.unitsDonated = unitsDonated;
	}

	public Donation(Float unitsDonated, UUID donorId, boolean reusable) {
		super();
		this.unitsDonated = unitsDonated;
		this.donorId = donorId;
		this.reusable = reusable;
	}

	public UUID getDonorId() {
		return donorId;
	}

	public void setDonorId(UUID donorId) {
		this.donorId = donorId;
	}

	public Donation() {

	}

	public Donation(Float unitsDonated, UUID donorId, boolean reusable, Long donatedOn) {
		super();
		this.unitsDonated = unitsDonated;
		this.donorId = donorId;
		this.reusable = reusable;
		this.donatedOn = donatedOn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(donatedOn, donationId, donorId, reusable, unitsDonated);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Donation other = (Donation) obj;
		return Objects.equals(donatedOn, other.donatedOn) && Objects.equals(donationId, other.donationId)
				&& Objects.equals(donorId, other.donorId) && Objects.equals(reusable, other.reusable)
				&& Objects.equals(unitsDonated, other.unitsDonated);
	}
}
