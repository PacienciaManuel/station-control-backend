package com.stationcontrol.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseController<T> {
	
	public ResponseEntity<T> ok(T body) {
		return ResponseEntity.ok(body);
	}
	
	public ResponseEntity<List<T>> ok(List<T> body) {
		return ResponseEntity.ok(body);
	}
	
	public ResponseEntity<Page<T>> ok(Page<T> body) {
		return ResponseEntity.ok(body);
	}
	
	public ResponseEntity<T> created(T body) {
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
	
	public ResponseEntity<List<T>> created(List<T> body) {
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
	
	public ResponseEntity<Void> noContent() {
		return ResponseEntity.noContent().build();
	}
}
