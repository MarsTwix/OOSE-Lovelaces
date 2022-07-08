package nl.han.project.skilltree.domain.impl.service;

import nl.han.project.skilltree.domain.assessment.data.Assessment;
import nl.han.project.skilltree.domain.exceptions.InvalidRequestException;
import nl.han.project.skilltree.domain.exceptions.ServerException;
import nl.han.project.skilltree.domain.impl.data.AssessmentDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @Mock
    AssessmentDaoImpl dao;

    @InjectMocks
    AssessmentServiceImpl sut;

    Assessment assessment;

    @BeforeEach
    void setUp() {
        assessment = new Assessment(1, 5, "test", 1, 1);
    }

    @Test
    void createAssessmentSuccessful() {
        when(dao.createAssessment(assessment)).thenReturn(50);
        Assessment result = sut.createAssessment(assessment);
        assertEquals(50, result.getId());
    }

    @Test
    void createAssessmentInvalidRequestArguments() {
        assessment.setDescription(null);
        assertThrows(InvalidRequestException.class, () -> sut.createAssessment(assessment));
        assessment.setDescription("");
        assertThrows(InvalidRequestException.class, () -> sut.createAssessment(assessment));
        assessment.setDescription("Test");
        assessment.setSkillId(0);
        assertThrows(InvalidRequestException.class, () -> sut.createAssessment(assessment));
        assessment.setUserId(0);
        assessment.setSkillId(1);
        assertThrows(InvalidRequestException.class, () -> sut.createAssessment(assessment));
        assessment.setUserId(1);
        assessment.setGrade(11);
        assertThrows(InvalidRequestException.class, () -> sut.createAssessment(assessment));
        assessment.setGrade(0);
        assertThrows(InvalidRequestException.class, () -> sut.createAssessment(assessment));
    }
}