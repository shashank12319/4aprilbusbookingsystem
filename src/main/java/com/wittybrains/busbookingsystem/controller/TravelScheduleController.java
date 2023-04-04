package com.wittybrains.busbookingsystem.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.wittybrains.busbookingsystem.dto.TravelScheduleDTO;
import com.wittybrains.busbookingsystem.model.Bus;
import com.wittybrains.busbookingsystem.model.Driver;
import com.wittybrains.busbookingsystem.model.Stop;
import com.wittybrains.busbookingsystem.model.TravelSchedule;
import com.wittybrains.busbookingsystem.repository.TravelScheduleRepository;
import com.wittybrains.busbookingsystem.service.TravelScheduleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/schedules")
public class TravelScheduleController {

	private static final Logger logger = LoggerFactory.getLogger(TravelScheduleController.class);

	private final TravelScheduleService travelScheduleService;
	private final TravelScheduleRepository scheduleRepository;

	public TravelScheduleController(TravelScheduleService travelScheduleService,
			TravelScheduleRepository scheduleRepository) {
		this.travelScheduleService = travelScheduleService;
		this.scheduleRepository = scheduleRepository;
	}

	@PostMapping
	public ResponseEntity<?> createTravelSchedule(@RequestBody TravelScheduleDTO travelScheduleDTO)
			throws ParseException {
		logger.info("Creating travel schedule with DTO={}", travelScheduleDTO);
		ResponseEntity<?> travelSchedule = travelScheduleService.createSchedule(travelScheduleDTO);

		if (travelSchedule != null) {
			logger.info("Successfully created travel schedule");
			return ResponseEntity.ok(travelSchedule);
		} else {
			logger.error("Failed to create travel schedule");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create travel schedule");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateSchedule(@PathVariable Long id, @RequestBody TravelScheduleDTO updatedSchedule)
			throws ParseException {
		try {
			logger.info("Updating travel schedule with id={}, DTO={}", id, updatedSchedule);
			ResponseEntity<?> savedSchedule = travelScheduleService.updateSchedule(id, updatedSchedule);
			if (savedSchedule != null) {
				logger.info("Successfully updated travel schedule");
				return ResponseEntity.ok(savedSchedule);
			} else {
				logger.warn("Travel schedule not found with id={}", id);
				return ResponseEntity.notFound().build();
			}
		} catch (EntityNotFoundException ex) {
			logger.error("Failed to update travel schedule with id={}", id, ex);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTravelSchedule(@PathVariable Long id) {
		try {
			logger.info("Fetching travel schedule with id={}", id);
			TravelSchedule travelSchedule = travelScheduleService.getScheduleById(id);
			if (travelSchedule != null) {
				logger.info("Successfully fetched travel schedule with id={}", id);
				return ResponseEntity.ok(travelSchedule);
			} else {
				logger.warn("Travel schedule not found with id={}", id);
				return ResponseEntity.notFound().build();
			}
		} catch (EntityNotFoundException ex) {
			logger.error("Failed to fetch travel schedule with id={}", id, ex);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
		Optional<TravelSchedule> existingScheduleOptional = scheduleRepository.findById(id);
		if (existingScheduleOptional.isPresent()) {
			logger.info("Deleting travel schedule with id={}", id);
			scheduleRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			logger.warn("Travel schedule not found with id={}", id);
			return ResponseEntity.notFound().build();
		}
	}

	
	@GetMapping("/travel-schedules")
	public ResponseEntity<List<TravelScheduleDTO>> getTravelSchedules(
	        @RequestParam(required = false) String source,
	        @RequestParam(required = false) String destination,
	        @RequestParam(required = false) Long busId,
	        @RequestParam(required = false) Long driverId,
	        @RequestParam(required = false) String estimatedArrivalTimeStart,
	        @RequestParam(required = false) String estimatedArrivalTimeEnd,
	        @RequestParam(required = false) String estimatedDepartureTimeStart,
	        @RequestParam(required = false) String estimatedDepartureTimeEnd
	) {
	    Specification<TravelSchedule> spec = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (source != null) {
	            predicates.add(cb.equal(root.get("source"), source));
	        }

	        if (destination != null) {
	            predicates.add(cb.equal(root.get("destination"), destination));
	        }

	        if (busId != null) {
	            Join<TravelSchedule, Bus> busJoin = root.join("bus");
	            predicates.add(cb.equal(busJoin.get("id"), busId));
	        }

	        if (driverId != null) {
	            Join<TravelSchedule, Driver> driverJoin = root.join("driver");
	            predicates.add(cb.equal(driverJoin.get("driverId"), driverId));
	        }

	        if (estimatedArrivalTimeStart != null && estimatedArrivalTimeEnd != null) {
	            predicates.add(cb.between(root.get("estimatedArrivalTime"), estimatedArrivalTimeStart, estimatedArrivalTimeEnd));
	        } else if (estimatedArrivalTimeStart != null) {
	            predicates.add(cb.greaterThanOrEqualTo(root.get("estimatedArrivalTime"), estimatedArrivalTimeStart));
	        } else if (estimatedArrivalTimeEnd != null) {
	            predicates.add(cb.lessThanOrEqualTo(root.get("estimatedArrivalTime"), estimatedArrivalTimeEnd));
	        }

	        if (estimatedDepartureTimeStart != null && estimatedDepartureTimeEnd != null) {
	            predicates.add(cb.between(root.get("estimatedDepartureTime"), estimatedDepartureTimeStart, estimatedDepartureTimeEnd));
	        } else if (estimatedDepartureTimeStart != null) {
	            predicates.add(cb.greaterThanOrEqualTo(root.get("estimatedDepartureTime"), estimatedDepartureTimeStart));
	        } else if (estimatedDepartureTimeEnd != null) {
	            predicates.add(cb.lessThanOrEqualTo(root.get("estimatedDepartureTime"), estimatedDepartureTimeEnd));
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	    List<TravelSchedule> travelSchedules = scheduleRepository.findAll(spec);

	    List<TravelScheduleDTO> travelScheduleDTOs = travelSchedules.stream().map(TravelScheduleDTO::new)
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(travelScheduleDTOs);
	}


	@GetMapping("travel")
	public List<TravelSchedule> findTravelSchedules(@RequestParam("source") String source,
	                                                 @RequestParam("destination") String destination) {
	    Specification<TravelSchedule> spec = (root, query, cb) -> {
	        // Join the TravelSchedule table with the Stop table to check for intermediate stops
	        Join<TravelSchedule, Stop> join = root.join("stops");
	        Predicate stopsPredicate = cb.notEqual(root.get("scheduleId"), join.get("travelSchedule").get("scheduleId"));

	        // Create predicates for the join
	        Predicate sourcePredicate = cb.equal(root.get("source"), source);
	        Predicate destinationPredicate = cb.equal(root.get("destination"), destination);
	        
	        // Combine the predicates
	        Predicate predicate = cb.and(sourcePredicate, destinationPredicate, stopsPredicate);
	        // Create the query
	        query.distinct(true);
	        query.where(predicate);
	        return null;
	    };
	    return scheduleRepository.findAll(spec);
	}




	@GetMapping
	public ResponseEntity<List<TravelScheduleDTO>> getAllSchedules() {
		logger.info("Getting all travel schedules");
		List<TravelScheduleDTO> travelScheduleDTOList = travelScheduleService.getAllSchedules();
		return ResponseEntity.ok().body(travelScheduleDTOList);
	}
}
