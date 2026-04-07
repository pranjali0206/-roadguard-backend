package com.roadguard.backend.service;

import com.roadguard.backend.dto.WorkshopDTO;
import com.roadguard.backend.model.Workshop;
import com.roadguard.backend.repository.WorkshopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkshopService {

    @Autowired
    private WorkshopRepository workshopRepository;

    private static final double EARTH_RADIUS_KM = 6371.0;

    // Haversine Formula — calculates real-world distance between two GPS coords
    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    // Get workshops within radiusKm from user location, sorted by distance
    public List<WorkshopDTO> getNearbyWorkshops(double userLat, double userLng, double radiusKm) {
        // Bounding box pre-filter (1 degree lat ≈ 111 km)
        double latDelta = radiusKm / 111.0;
        double lngDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(userLat)));

        List<Workshop> candidates = workshopRepository.findWithinBoundingBox(
            userLat - latDelta, userLat + latDelta,
            userLng - lngDelta, userLng + lngDelta
        );

        return candidates.stream()
            .map(w -> {
                double dist = haversine(userLat, userLng, w.getLatitude(), w.getLongitude());
                return new WorkshopDTO(
                    w.getId(), w.getName(), w.getOwnerName(), w.getPhone(),
                    w.getAddress(), w.getCity(), w.getState(),
                    w.getLatitude(), w.getLongitude(), w.getServices(),
                    w.getRating(), w.getTotalRatings(), w.getIsOpen(),
                    w.getImageUrl(), Math.round(dist * 10.0) / 10.0
                );
            })
            .filter(dto -> dto.getDistanceKm() <= radiusKm)  // exact Haversine filter
            .sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
            .collect(Collectors.toList());
    }

    // Save a new workshop
    public Workshop saveWorkshop(Workshop workshop) {
        return workshopRepository.save(workshop);
    }

    // Get all workshops (admin)
    public List<Workshop> getAllWorkshops() {
        return workshopRepository.findAll();
    }

    // Get by ID
    public Workshop getById(Long id) {
        return workshopRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Workshop not found"));
    }

    // Toggle open/close
    public Workshop toggleStatus(Long id) {
        Workshop w = getById(id);
        w.setIsOpen(!w.getIsOpen());
        return workshopRepository.save(w);
    }
}