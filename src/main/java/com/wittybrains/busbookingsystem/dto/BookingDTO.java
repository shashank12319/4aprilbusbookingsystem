package com.wittybrains.busbookingsystem.dto;

import com.wittybrains.busbookingsystem.model.Booking;


import com.wittybrains.busbookingsystem.model.Booking;

public class BookingDTO {
	private Long bookingId;

	private TravelScheduleDTO schedule;

	private UserDTO user;

	public BookingDTO() {
		super();
	}

	public UserDTO getUser() {
		return user;
	}

	public BookingDTO(Booking booking) {
		this.bookingId = booking.getBookingId();
		this.schedule = new TravelScheduleDTO(booking.getSchedule());

		this.user = new UserDTO(booking.getUser());

	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public TravelScheduleDTO getSchedule() {
		return schedule;
	}

	public void setSchedule(TravelScheduleDTO schedule) {
		this.schedule = schedule;
	}

	public void setUser(UserDTO userDTO) {
		this.user = userDTO;
	}

}