package com.ftn.sbnz.service;

import com.ftn.sbnz.model.dto.AnswerRequest;
import com.ftn.sbnz.model.dto.DatingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dating")
public class DatingController {
	private static final Logger log = LoggerFactory.getLogger(DatingController.class);

	private final DatingService datingService;

	@Autowired
	public DatingController(DatingService datingService) {
		this.datingService = datingService;
	}

	/**
	 * Starts a new map dating session.
	 * @return The initial state, including the first question and the full time period.
	 */
	@GetMapping("/start")
	public ResponseEntity<DatingResponse> startDating() {
		log.info("Request received to start a new dating session.");
		DatingResponse response = datingService.startDating();
		return ResponseEntity.ok(response);
	}

	/**
	 * Processes a user's answer to a question.
	 * @param answerRequest The user's answer and the current state.
	 * @return The updated state, including the new possible periods and the next question.
	 */
	@PostMapping("/answer")
	public ResponseEntity<DatingResponse> processAnswer(@RequestBody AnswerRequest answerRequest) {
		log.info("Received answer: {}", answerRequest);
		DatingResponse response = datingService.processAnswer(answerRequest);
		return ResponseEntity.ok(response);
	}
}
