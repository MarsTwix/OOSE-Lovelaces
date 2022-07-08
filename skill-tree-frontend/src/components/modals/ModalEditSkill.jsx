import React, {
  useState, useEffect, useCallback,
} from "react";
import { Switch } from "@headlessui/react";
import toastr from "toastr";
import { useParams } from "react-router-dom";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { Modal } from "..";

function ModalEditSkill({
  node,
  show,
  onClose,
  onArchive,
  onEdit,
}) {
  const { id } = useParams();
  const [skillName, setSkillName] = useState("");
  const [skillDescription, setSkillDescription] = useState("");
  const [skillAssessmentCriteria, setSkillAssessmentCriteria] = useState("");
  const [evidenceRequired, setEvidenceRequired] = useState(false);
  const [editDisabled, setEditDisabled] = useState(false);
  const [archiveDisabled, setArchiveDisabled] = useState(false);

  const onEditSkill = useCallback(() => SkillTreeRepository.editSkill(id, node.id, {
    name: skillName,
    description: skillDescription,
    assessmentCriteria: skillAssessmentCriteria,
    evidenceRequired,
  }), [id, skillName, skillDescription, evidenceRequired, skillAssessmentCriteria]);

  const handleSubmit = useCallback(() => {
    setEditDisabled(true);
    onEditSkill()
      .then(() => {
        onEdit({
          id: node.id,
          name: skillName,
          description: skillDescription,
          assessmentCriteria: skillAssessmentCriteria,
          evidenceRequired,
        });
        toastr.success(`${skillName} aangepast`);
      }).catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      }).finally(() => {
        setEditDisabled(false);
      });
  }, [skillName, skillDescription, evidenceRequired, skillAssessmentCriteria]);

  const onArchiveSkill = () => {
    setArchiveDisabled(true);
    SkillTreeRepository.archiveSkill(id, node.id)
      .then(() => {
        onArchive({
          id: node.id,
          name: skillName,
        });
        toastr.success(`${node.data.label} gearchiveerd`);
      }).catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      }).finally(() => setArchiveDisabled(false));
  };

  useEffect(() => {
    if (!node && !node?.data) {
      return;
    }

    setSkillName(node?.data.label);
    setSkillDescription(node?.data.description);
    setEvidenceRequired(node?.data.evidenceRequired);
    setSkillAssessmentCriteria(node?.data.assessmentCriteria);
  }, [node]);

  return (
    <Modal
      title="Bijwerken skill"
      show={show}
      onClose={onClose}
    >
      <div className="mt-2 flex flex-col">
        <label htmlFor="base-input" className="block font-medium text-gray-7">Naam skill</label>
        <input
          onChange={(e) => setSkillName(e.target.value)}
          value={skillName}
          placeholder={node?.data.label}
          type="text"
          id="name-input"
          maxLength="255"
          className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
                   px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue
                   focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
        />
      </div>

      <div className="mt-2 flex flex-col">
        <label
          htmlFor="base-input"
          className="block font-medium text-gray-7"
        >
          Beschrijving
        </label>
        <textarea
          onChange={(e) => setSkillDescription(e.target.value)}
          value={skillDescription}
          placeholder={node?.data.description}
          id="description-input"
          rows="4"
          className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
                   px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue
                   focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
        />
      </div>

      <div className="mt-2 flex flex-col">
        <label
          htmlFor="assessment-criteria-input"
          className="block font-medium text-gray-7"
        >
          Beoordelingscriteria
        </label>
        <textarea
          onChange={(e) => setSkillAssessmentCriteria(e.target.value)}
          value={skillAssessmentCriteria}
          placeholder={node?.data.assessmentCriteria}
          id="assessment-criteria-input"
          rows="4"
          className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
          px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue focus:border-blue-300
          transition duration-150 ease-in-out sm:text-sm sm:leading-5"
        />
      </div>

      <div className="flex items-start mt-2">
        <div className="flex items-center h-5">
          <Switch
            checked={evidenceRequired}
            onChange={setEvidenceRequired}
            className={`${evidenceRequired ? "bg-pink-500" : "bg-pink-50"}
          relative inline-flex h-[1.5rem] w-[3rem] mt-2 shrink-0 cursor-pointer rounded-full
          border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none
          focus-visible:ring-2  focus-visible:ring-white focus-visible:ring-opacity-75`}
          >
            <span className="sr-only">Use setting</span>
            <span
              aria-hidden="true"
              className={`${evidenceRequired ? "translate-x-6" : "translate-x-0"}
            pointer-events-none inline-block h-[1.225rem] w-[1.225rem] transform rounded-full bg-white
            shadow-lg ring-0 transition duration-200 ease-in-out`}
            />
          </Switch>
        </div>
        <div className="ml-3 text-sm">
          <label htmlFor="comments" className="font-medium text-gray-700">
            Bewijs
            verplicht
          </label>
          <p className="text-gray-500">
            Studenten moeten bewijslast uploaden om de skill te
            kunnen behalen.
          </p>
        </div>
      </div>

      <div className="mt-4 flex justify-end">
        <button
          className="bg-black text-white font-bold uppercase px-6 py-3 text-sm outline-none
                  focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150 rounded disabled:bg-black/50"
          type="button"
          disabled={archiveDisabled}
          onClick={() => onArchiveSkill()}
        >
          Archiveren
        </button>
        <button
          id="save"
          className="bg-pink-500 text-white active:bg-pink-600 disabled:bg-pink-50
                  font-bold uppercase text-sm px-6 py-3 rounded shadow disabled:transform-none
                  disabled:transition-none disabled:cursor-not-allowed hover:shadow-lg outline-none
                  focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
          type="submit"
            /* eslint-disable no-mixed-operators */
          disabled={(editDisabled
              || skillName === ""
              || skillDescription === ""
              || skillName === node?.data.label
              && skillDescription === node?.data.description
              && evidenceRequired === node?.data.evidenceRequired
              && node?.data.assessmentCriteria === skillAssessmentCriteria)}
          onClick={() => handleSubmit()}
        >
          Opslaan
        </button>
      </div>
    </Modal>
  );
}

export default ModalEditSkill;
