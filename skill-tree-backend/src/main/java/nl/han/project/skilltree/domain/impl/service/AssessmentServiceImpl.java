package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.assessment.data.Assessment;
import nl.han.project.skilltree.domain.assessment.data.AssessmentDao;
import nl.han.project.skilltree.domain.assessment.service.AssessmentService;
import nl.han.project.skilltree.domain.exceptions.InvalidRequestException;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class AssessmentServiceImpl implements AssessmentService {
    @Inject
    AssessmentDao dao;

    @Override
    public Assessment createAssessment(Assessment assessment) {
        if(assessment.getGrade() < 1 || assessment.getGrade() > 10
                || assessment.getDescription() == null
                || assessment.getDescription().isEmpty()
                || assessment.getSkillId() < 1
                || assessment.getUserId() < 1
        ){
            throw new InvalidRequestException("Invalid arguments provided");
        }
        int insertedId = dao.createAssessment(assessment);
        return new Assessment(insertedId, assessment.getGrade(), assessment.getDescription(), assessment.getSkillId(), assessment.getUserId());
    }
}
