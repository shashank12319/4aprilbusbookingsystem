package com.wittybrains.busbookingsystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.wittybrains.busbookingsystem.dto.BusDTO;
import com.wittybrains.busbookingsystem.service.BusService;




@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;
    private final Logger logger = LoggerFactory.getLogger(BusController.class);

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PostMapping("/create")
    public ResponseEntity<List<BusDTO>> createBuses(@RequestBody List<BusDTO> busList) {
        logger.info("Creating buses...");
        List<BusDTO> createdBuses = busService.createBuses(busList);
        logger.info("Buses created successfully");
        return new ResponseEntity<>(createdBuses, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
	public ResponseEntity<BusDTO> getBusById(@PathVariable Long id) {
        logger.info("Getting bus with ID {}", id);
		BusDTO bus = busService.getBusById(id);
		return ResponseEntity.ok().body(bus);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BusDTO> updateBus(@PathVariable Long id, @RequestBody BusDTO busDTO) {
        logger.info("Updating bus with ID {}", id);
		BusDTO updatedBus = busService.updateBus(id, busDTO);
        logger.info("Bus with ID {} updated successfully", id);
		return ResponseEntity.ok().body(updatedBus);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBus(@PathVariable Long id) {
        logger.info("Deleting bus with ID {}", id);
		busService.deleteBus(id);
        logger.info("Bus with ID {} deleted successfully", id);
		return ResponseEntity.ok().body("Bus with id " + id + " deleted successfully");
	}
	@GetMapping
	public ResponseEntity<List<BusDTO>> getAllBuses() {
	    logger.info("Getting all buses");
	    List<BusDTO> buses = busService.getAllBuses();
	    return ResponseEntity.ok().body(buses);
	}

}
