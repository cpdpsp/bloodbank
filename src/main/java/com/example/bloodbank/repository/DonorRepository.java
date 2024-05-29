package com.example.bloodbank.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bloodbank.entity.Donor;

@Repository
public interface DonorRepository extends JpaRepository<Donor,UUID>{

	@Query(value="Select * from donor where blood_group=:bloodGroup",nativeQuery=true)
	List<Donor> findByBloodGroup(String bloodGroup);
	
}
