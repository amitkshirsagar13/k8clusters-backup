package io.k8clusters.qa.service;

import io.k8clusters.qa.dto.Choice;
import io.k8clusters.qa.dto.QA;
import io.k8clusters.qa.dto.QuestionType;
import io.k8clusters.qa.repo.QaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QaListService {

    private final QaRepository qaRepository;

    public QaListService(QaRepository qaRepository) {
        this.qaRepository = qaRepository;
    }

    private static final String HINT = "%s is %s!!!";
    private static final String MATHQUESTION = "What is %s of %s and %s? i.e. [ %s %s %s ]";
    private static final String MULTIPLICATION = "multiplication";
    private static final String RIGHT = "Right";
    private static final String WRONG = "Wrong";

    public List<QA> getQaListMocked(int qaCount) {
        List<QA> qaList = new ArrayList();
        for (int i=0;i<qaCount;i++) {
            qaList.add(getMathMultiplicationNumber());
        }

        Collections.shuffle(qaList);
        int index = 1;
        for (QA qa : qaList) {
            qa.setId(""+index++);
        }

        return qaList;
    }

    private QA getMathMultiplicationNumber() {
        QA qa = new QA();
        double a = 1000.0;
        double b = 100.0;
        int qaNumberA = (int)( a * Math.random());
        int qaNumberB = (int)(b * Math.random());
        String question = String.format(MATHQUESTION, MULTIPLICATION, qaNumberA, qaNumberB,qaNumberA, "X", qaNumberB);
        qa.setQuestion(question);
        qa.setChoices(new ArrayList<Choice>());

        qa.setMaxSelection(1);
        qa.setqType(QuestionType.MULTTYPE);

        Choice choice = new Choice();
        choice.setValue("" + qaNumberA * qaNumberB);
        choice.setCorrect(true);
        choice.setHint(String.format(HINT, choice.getValue(), RIGHT));
        qa.getChoices().add(choice);

        for(int i = 0; i < 3; i++){
            choice = new Choice();
            int aChoice = (int)(a * Math.random());
            int bChoice = (int)(b * Math.random());
            choice.setValue("" + aChoice * bChoice);
            choice.setHint(String.format(HINT, choice.getValue(), WRONG));
            qa.getChoices().add(choice);
        }

        Collections.shuffle(qa.getChoices());
        int index = 1;
        for (Choice qaChoice : qa.getChoices()) {
            qaChoice.setIndex(index++);
        }
        return qa;
    }
}
