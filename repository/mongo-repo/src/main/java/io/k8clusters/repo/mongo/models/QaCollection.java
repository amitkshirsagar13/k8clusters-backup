package io.k8clusters.repo.mongo.models;
import io.k8clusters.qa.dto.QuestionType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class QaCollection extends BaseModel {

    private String question;
    private List<Choice> answerList = new ArrayList<>();

    private int maxSelection;
    private int point = 0;

    private QuestionType qType = QuestionType.MULTTYPE;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Choice> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Choice> answerList) {
        this.answerList = answerList;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public QuestionType getqType() {
        return qType;
    }

    public void setqType(QuestionType qType) {
        this.qType = qType;
    }
}
