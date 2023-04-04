
package com.wittybrains.busbookingsystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wittybrains.busbookingsystem.dto.StationDTO;

import com.wittybrains.busbookingsystem.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {
    private static final Logger logger = LoggerFactory.getLogger(StationController.class);

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }
    
    @PostMapping
    public ResponseEntity<List<StationDTO>> createStations(@RequestBody List<StationDTO> stationList) {
        logger.info("Creating stations...");
        List<StationDTO> createdStations = stationService.createStations(stationList);
        logger.info("Stations created successfully.");
        return ResponseEntity.ok().body(createdStations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StationDTO> getStation(@PathVariable Long id) {
        logger.info("Getting station with id: {}", id);
        StationDTO stationDTO = stationService.getStationById(id);
        logger.info("Station retrieved successfully.");
        return ResponseEntity.ok().body(stationDTO);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StationDTO> updateStation(@PathVariable Long id, @RequestBody StationDTO stationDTO) {
        logger.info("Updating station with id: {}", id);
        StationDTO updatedStationDTO = stationService.updateStation(id, stationDTO);
        logger.info("Station updated successfully.");
        return ResponseEntity.ok().body(updatedStationDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        logger.info("Deleting station with id: {}", id);
        stationService.deleteStation(id);
        logger.info("Station deleted successfully.");
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<StationDTO>> getAllStations() {
        logger.info("Getting all stations");
        List<StationDTO> stationDTOList = stationService.getAllStations();
        return ResponseEntity.ok().body(stationDTOList);
    }

}
