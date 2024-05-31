package com.example.bloodbank.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloodbank.entity.Donation;
import com.example.bloodbank.response.FetchUnitsAvailableResponse;

@Repository
public interface DonationRepository extends JpaRepository<Donation,UUID>{

	@Query(value="Select blood_group as bloodGroup,sum(units_donated) as unitsDonated from donation JOIN donor"
			+ " on donor.donor_id = donation.donor_id group by donor.blood_group;",nativeQuery=true)
	List<FetchUnitsAvailableResponse> getUnitsAvailable();
	
	@Query(value="Select * from donation where donor_id=:donorId",nativeQuery = true)
	List<Donation> findAllByDonorId(String donorId);
	
	@Query(value="select * from donation where donated_on between UNIX_TIMESTAMP(DATE_SUB(CURDATE(),INTERVAL 3 MONTH))*1000 and (:currentTime);",nativeQuery=true)
	List<Donation> getDonationForLastThreeMonths(Long currentTime);
	
	@Query(value="select count(*) from donation where donated_on between UNIX_TIMESTAMP(DATE_SUB(CURDATE(),INTERVAL 7 DAY))*1000 and (:currentTime) and donor_id=:donorId",nativeQuery=true)
	int numberOfDonationsMadeInLastSevenDays(String donorId,Long currentTime);
	
	@Transactional
	@Modifying
	@Query(value="update donation set reusable=false where "
			+ "donated_on not between UNIX_TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) * 1000 and (:currentTime)",nativeQuery=true)
	void updateBloodReusability(Long currentTime);
}
