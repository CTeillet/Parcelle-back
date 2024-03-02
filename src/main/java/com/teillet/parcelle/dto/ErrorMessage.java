package com.teillet.parcelle.dto;

import lombok.Value;

@Value
public class ErrorMessage {

	String message;

	public static ErrorMessage from(final String message) {
		return new ErrorMessage(message);
	}
}
