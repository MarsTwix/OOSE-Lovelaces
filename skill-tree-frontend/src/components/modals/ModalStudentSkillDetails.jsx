import React, {
  useState, Fragment, useEffect, useCallback,
} from "react";
import { Tab } from "@headlessui/react";
import toastr from "toastr";
import AssessmentRepository from "../../repositories/AssessmentRepository";
import UserRepository from "../../repositories/UserRepository";
import { Modal } from "..";

function ModalStudentSkillDetails({
  node,
  show,
  onClose,
  onGiveAssessment,
  studentId,
}) {
  const [skillName, setSkillName] = useState("");
  const [skillDescription, setSkillDescription] = useState("");
  const [skillAssessmentCriteria, setSkillAssessmentCriteria] = useState("");
  const [skillGrade, setSkillGrade] = useState(null);
  const [assessmentGrade, setAssessmentGrade] = useState(5.5);
  const [assessmentDescription, setAssessmentDescription] = useState("");
  const [assessmentSubmitDisabled, setAssessmentSubmitDisabled] = useState(false);
  const [feedbackDescription, setFeedbackDescription] = useState("");
  const [feedbackSubmitDisabled, setFeedbackSubmitDisabled] = useState(false);

  const postAssessment = useCallback(async () => AssessmentRepository.postAssessment({
    skillId: parseInt(node.id, 10), userId: parseInt(studentId, 10), grade: assessmentGrade, description: assessmentDescription,
  }), [assessmentGrade, node.id, assessmentDescription, studentId]);

  const postFeedback = useCallback(async () => UserRepository.postFeedback({
    skillId: parseInt(node.id, 10), forUserId: parseInt(studentId, 10), description: feedbackDescription,
  }), [feedbackDescription, node.id, studentId]);

  const tabClasses = "w-full rounded-lg py-2.5 text-sm font-medium leading-5 ring-white ring-opacity-60 ring-offset-2"
      + "ring-offset-pink-400 focus:outline-none focus:ring-2";

  const min = 0;
  const max = 10;

  const handleGradeInputChange = (event) => {
    const value = Math.max(min, Math.min(max, Number(event.target.value)));
    setAssessmentGrade(value);
  };

  const handleAssessmentSubmit = () => {
    setAssessmentSubmitDisabled(true);

    postAssessment()
      .then(() => {
        onGiveAssessment({ ...node, data: { ...node.data, grade: assessmentGrade } });
        toastr.success("Beoordeling aangemaakt");
        setAssessmentGrade(5.5);
        onClose();
      })
      .catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      })
      .finally(() => setAssessmentSubmitDisabled(false));
  };

  const handleFeedbackSubmit = () => {
    setFeedbackSubmitDisabled(true);

    postFeedback()
      .then(() => {
        toastr.success("Feedback aangemaakt");
        setFeedbackDescription("");
        onClose();
      }).catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      }).finally(() => setFeedbackSubmitDisabled(false));
  };

  useEffect(() => {
    if (!node && !node?.data) {
      return;
    }

    setSkillName(node?.data.label);
    setSkillDescription(node?.data.description);
    setSkillAssessmentCriteria(node?.data.assessmentCriteria);
    setSkillGrade(node?.data.grade);
  }, [node]);

  useEffect(() => {
    if (assessmentGrade <= 0 || assessmentGrade > 10 || assessmentDescription.length === 0) {
      setAssessmentSubmitDisabled(true);
    } else {
      setAssessmentSubmitDisabled(false);
    }
  }, [assessmentGrade, assessmentDescription]);

  useEffect(() => {
    setFeedbackSubmitDisabled(feedbackDescription.length === 0);
  }, [feedbackDescription]);

  return (
    <Modal
      title="Skill details"
      show={show}
      onClose={onClose}
    >

      <Tab.Group>
        <Tab.List className="flex space-x-1 rounded-xl bg-pink-500 p-1 mb-4">
          <Tab as={Fragment}>
            {({ selected }) => (
              <button
                className={
                  selected ? `${tabClasses} bg-white text-pink-500` : `${tabClasses} text-white bg-pink-500`
                }
                type="button"
              >
                Details
              </button>
            )}
          </Tab>
          <Tab as={Fragment}>
            {({ selected }) => (
              <button
                className={
                  selected ? `${tabClasses} bg-white text-pink-500` : `${tabClasses} text-white bg-pink-500`
                }
                type="button"
              >
                Beoordeling
              </button>
            )}
          </Tab>
          <Tab as={Fragment}>
            {({ selected }) => (
              <button
                className={
                  selected ? `${tabClasses} bg-white text-pink-500` : `${tabClasses} text-white bg-pink-500`
                }
                type="button"
              >
                Feedback
              </button>
            )}
          </Tab>
        </Tab.List>
        <Tab.Panels>
          <Tab.Panel>
            <div className="mt-2 flex flex-col">
              <strong>Naam: </strong>
              {skillName}
              <strong>Beschrijving: </strong>
              {skillDescription}
              <strong>Beoordelingscriteria: </strong>
              {skillAssessmentCriteria || "Geen criteria"}
              <strong>Cijfer: </strong>
              {skillGrade || "Niet vastgelegd"}
            </div>
          </Tab.Panel>
          <Tab.Panel>
            <label htmlFor="grade" className="font-medium text-gray-700">
              Cijfer
            </label>
            <input
              id="grade"
              type="range"
              min="1"
              max="10"
              step="0.1"
              className="w-full h-2 bg-pink-200 rounded-lg appearance-none cursor-pointer dark:bg-pink-700"
              onChange={(e) => setAssessmentGrade(parseFloat(e.target.value))}
              value={assessmentGrade}
            />
            <input
              id="grade"
              type="number"
              max="10"
              min="1"
              step="0.1"
              onChange={(e) => handleGradeInputChange(e)}
              value={assessmentGrade}
              className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md px-3 py-3
              placeholder-gray-7 focus:outline-none focus:shadow-outline-blue focus:ring-pink-500 focus:border-pink-500
              focus:z-10 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
            />
            <label htmlFor="description" className="font-medium text-gray-700">
              Toelichting
            </label>
            <textarea
              id="description"
              maxLength="250"
              onChange={(e) => setAssessmentDescription(e.target.value)}
              value={assessmentDescription}
              placeholder="Vul een toelichting in."
              className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md px-3 py-3
              placeholder-gray-7 focus:outline-none focus:shadow-outline-blue focus:ring-pink-500 focus:border-pink-500
              focus:z-10 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
            >
              {assessmentDescription}
            </textarea>
            <div className="flex mt-1 justify-end">
              <button
                id="save"
                className="bg-pink-500 text-white active:bg-pink-600 disabled:bg-pink-50 font-bold uppercase text-sm
                px-6 py-3 rounded shadow disabled:transform-none disabled:transition-none disabled:cursor-not-allowed
                hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                type="button"
                disabled={assessmentSubmitDisabled}
                onClick={() => handleAssessmentSubmit()}
              >
                Beoordelen
              </button>
            </div>
          </Tab.Panel>
          <Tab.Panel>
            <label htmlFor="feedbackDescription" className="font-medium text-gray-700">
              Feedback
            </label>
            <textarea
              id="feedbackDescription"
              onChange={(e) => setFeedbackDescription(e.target.value)}
              value={feedbackDescription}
              placeholder="Vul uw feedback in."
              className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md px-3 py-3
              placeholder-gray-7 focus:outline-none focus:shadow-outline-blue focus:ring-pink-500 focus:border-pink-500
              focus:z-10 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
            >
              {feedbackDescription}
            </textarea>
            <div className="flex mt-1 justify-end">
              <button
                id="save"
                className="bg-pink-500 text-white active:bg-pink-600 disabled:bg-pink-50 font-bold uppercase text-sm
                px-6 py-3 rounded shadow disabled:transform-none disabled:transition-none disabled:cursor-not-allowed
                hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                type="button"
                disabled={feedbackSubmitDisabled}
                onClick={() => handleFeedbackSubmit()}
              >
                Feedback geven
              </button>
            </div>
          </Tab.Panel>
        </Tab.Panels>
      </Tab.Group>
    </Modal>
  );
}

export default ModalStudentSkillDetails;
