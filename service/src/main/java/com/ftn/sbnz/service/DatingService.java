package com.ftn.sbnz.service;

import com.ftn.sbnz.model.dto.AnswerRequest;
import com.ftn.sbnz.model.dto.DatingResponse;
import com.ftn.sbnz.model.events.AnswerProvidedEvent;
import com.ftn.sbnz.model.models.*;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class DatingService {
	private static final Logger log = LoggerFactory.getLogger(DatingService.class);
	private static final String ROOT_QUESTION_ID = "soviet_union";

	private final Map<String, PossiblePeriods> activeSessions = new HashMap<>();
	private final Map<String, QuestionNode> questionRepository;

	private final KieContainer kieContainer;

	@Autowired
	public DatingService(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
		this.questionRepository = createQuestionRepository();
	}

	public DatingResponse startDating() {
		String sessionId = UUID.randomUUID().toString();
		PossiblePeriods periods = new PossiblePeriods();
		activeSessions.put(sessionId, periods);
		log.info("New session started: {}", sessionId);

		QuestionNode rootQuestion = questionRepository.get(ROOT_QUESTION_ID);
		return new DatingResponse(sessionId, rootQuestion.getText(), periods.getPeriods(), ROOT_QUESTION_ID);
	}

	public DatingResponse processAnswer(AnswerRequest answerRequest) {
		PossiblePeriods currentPeriods = activeSessions.get(answerRequest.getSessionId());
		if (currentPeriods == null) {
			throw new IllegalArgumentException("Invalid session ID: " + answerRequest.getSessionId());
		}

		KieSession kieSession = kieContainer.newKieSession("datingSession");

		String nextQuestionId = null;

		try {
			kieSession.insert(currentPeriods);
			kieSession.insert(new AnswerProvidedEvent(answerRequest.getQuestionId(), answerRequest.getAnswer()));
			kieSession.fireAllRules();

			for (Object object : kieSession.getObjects(o -> o instanceof NextQuestion)) {
				nextQuestionId = ((NextQuestion) object).getQuestionId();
				System.out.println("Question Id: "+nextQuestionId);
				break;
			}

		} finally {
			kieSession.dispose();
		}

		log.info("Session {} updated. New Periods: {}", answerRequest.getSessionId(), currentPeriods.getPeriods());

		if (currentPeriods.getFinalPeriod() != null) {
			return new DatingResponse(
					answerRequest.getSessionId(),
					"Final period determined.",
					currentPeriods.getPeriods(),
					currentPeriods.getFinalPeriod()
			);
		} else if (nextQuestionId != null) {
			QuestionNode nextQuestion = questionRepository.get(nextQuestionId);
			return new DatingResponse(
					answerRequest.getSessionId(),
					nextQuestion.getText(),
					currentPeriods.getPeriods(),
					nextQuestionId
			);
		} else {
			return new DatingResponse(
					answerRequest.getSessionId(),
					"Period narrowed. No next question determined by rules.",
					currentPeriods.getPeriods()
			);
		}
	}

	public List<String> getHistoricalPathForYear(int year) {
		KieSession kieSession = kieContainer.newKieSession("bwKsession");

		try {
			questionRepository.values().forEach(kieSession::insert);
			kieSession.insert(new FindPathForYear(year));
			kieSession.fireAllRules();

			QueryResults results = kieSession.getQueryResults("Get Final Path");

			if (results.size() > 0) {
				FinalPath finalPath = (FinalPath) results.iterator().next().get("$fp");
				return finalPath.getPath();
			}

			return new ArrayList<>();

		} finally {
			kieSession.dispose();
		}
	}

	private Map<String, QuestionNode> createQuestionRepository() {
		Map<String, QuestionNode> repo = new HashMap<>();
		String dataPath = "/data/data.csv";
		InputStream dataStream = getClass().getResourceAsStream(dataPath);
		if (dataStream == null) {
			throw new RuntimeException("Could not find data.csv on classpath. Path: " + dataPath);
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream))) {
			// Skip header
			reader.readLine();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split("\\|");
				// CSV columns: QuestionId,Name,StartDate,EndDate,AnswerYes,AnswerNo

				String name = values[0];
				String questionId = values[1];
				String answerYes = values[4];
				String answerNo = values[5];
				String questionText = "Does the " + name + " exist?";
				repo.put(questionId, new QuestionNode(questionId, questionText, answerYes, answerNo));
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read or parse data.csv", e);
		}
		log.info("Successfully loaded {} questions into the repository.", repo.size());
		return repo;
	}
}

