package com.roadguard.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roadguard.backend.dto.WorkshopDTO;
import com.roadguard.backend.model.Workshop;
import com.roadguard.backend.service.WorkshopService;

@RestController
@RequestMapping("/api/workshops")
@CrossOrigin(origins = "*") // allow React frontend
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    // POST /api/workshops — add workshop (admin/testing)
    @PostMapping
    public ResponseEntity<Workshop> addWorkshop(@RequestBody Workshop workshop) {
        return ResponseEntity.ok(workshopService.saveWorkshop(workshop));
    }

    // GET /api/workshops — get all (admin)
    @GetMapping
    public ResponseEntity<List<Workshop>> getAll() {
        return ResponseEntity.ok(workshopService.getAllWorkshops());
    }

    // GET /api/workshops/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Workshop> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workshopService.getById(id));
    }

    // GET /api/workshops/nearby?lat=22.7&lng=75.8&radius=10
    @GetMapping("/nearby")
    public ResponseEntity<List<WorkshopDTO>> getNearby(
        @RequestParam double lat,
        @RequestParam double lng,
        @RequestParam(defaultValue = "10") double radius
    ) {
        return ResponseEntity.ok(workshopService.getNearbyWorkshops(lat, lng, radius));
    }

    // PATCH /api/workshops/{id}/toggle
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Workshop> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(workshopService.toggleStatus(id));
    }
}