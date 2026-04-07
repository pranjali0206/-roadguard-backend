package com.roadguard.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roadguard.backend.model.Workshop;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    // Fetch all workshops within a bounding box (fast pre-filter before Haversine)
    @Query("SELECT w FROM Workshop w WHERE " +
           "w.latitude BETWEEN :minLat AND :maxLat AND " +
           "w.longitude BETWEEN :minLng AND :maxLng")
    List<Workshop> findWithinBoundingBox(
        @Param("minLat") double minLat,
        @Param("maxLat") double maxLat,
        @Param("minLng") double minLng,
        @Param("maxLng") double maxLng
    );

    List<Workshop> findByIsOpenTrue();
    List<Workshop> findByCity(String city);
}