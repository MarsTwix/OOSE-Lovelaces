import React, { useState, useCallback } from "react";
import { Switch } from "@headlessui/react";
import { trim } from "lodash";
import { useParams } from "react-router-dom";

import toastr from "toastr";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { ReactFlowService } from "../../services";
import { Modal } from "..";

function ModalCreateSkill({
  show, onClose, addToReactFlow,
}) {
  const { id } = useParams();
  const [name, setName] = useState(null);
  const [description, setDescription] = useState(null);
  const [assessmentCriteria, setAssessmentCriteria] = useState(null);
  const [evidenceRequired, setEvidenceRequired] = useState(false);
  const [disabled, setDisabled] = useState(false);

  const postSkill = useCallback(() => SkillTreeRepository.postSkill(id, {
    name, description, assessmentCriteria, evidenceRequired, x: 0, y: 0,
  }), [id, name, description, evidenceRequired, assessmentCriteria]);

  const handleSubmit = useCallback(() => {
    setDisabled(true);
    postSkill()
      .then((res) => {
        addToReactFlow(ReactFlowService.mapToNode({
          id: res.data.id, name, description, assessmentCriteria, evidenceRequired, x: 0, y: 0,
        }));
        onClose();
        toastr.success(`${name} aangemaakt`);
      }).catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      }).finally(() => {
        setDisabled(false);
      });
  }, [postSkill]);

  return (
    <Modal
      title="Skill toevoegen"
      show={show}
      onClose={onClose}
    >
      <div>

        <div className="mt-2 flex flex-col gap-2">
          <label htmlFor="base-input" className="block font-medium text-gray-7">Naam skill</label>
          <input
            onChange={(e) => setName(e.target.value)}
            type="text"
            id="name-input"
            maxLength="255"
            className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md px-3 py-3
            placeholder-gray-7 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition
            duration-150 ease-in-out sm:text-sm sm:leading-5"
          />

          <label htmlFor="base-input" className="block font-medium text-gray-7">Beschrijving</label>
          <textarea
            onChange={(e) => setDescription(e.target.value)}
            id="description-input"
            rows="4"
            className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md px-3 py-3
            placeholder-gray-7 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition
            duration-150 ease-in-out sm:text-sm sm:leading-5"
          />

          <div className="mt-2 flex flex-col">
            <label
              htmlFor="assessment-criteria-input"
              className="block font-medium text-gray-7"
            >
              Beoordelingscriteria
            </label>
            <textarea
              onChange={(e) => setAssessmentCriteria(e.target.value)}
              id="assessment-criteria-input"
              rows="4"
              className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
          px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue focus:border-blue-300
          transition duration-150 ease-in-out sm:text-sm sm:leading-5"
            />
          </div>

          <div className="flex items-start">
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
              <label htmlFor="comments" className="font-medium text-gray-700">Bewijs verplicht</label>
              <p className="text-gray-500">Studenten moeten bewijslast uploaden om de skill te kunnen behalen.</p>
            </div>
          </div>
        </div>
      </div>

      <div className="mt-4 flex justify-end">
        <button
          className="bg-black text-white font-bold uppercase px-6 py-3 text-sm outline-none focus:outline-none
          mr-1 mb-1 ease-linear transition-all duration-150 rounded"
          type="button"
          onClick={() => onClose()}
        >
          Sluiten
        </button>
        <button
          id="save"
          className="bg-pink-500 text-white active:bg-pink-600 disabled:bg-pink-50 font-bold uppercase text-sm
          px-6 py-3 rounded shadow disabled:transform-none disabled:transition-none disabled:cursor-not-allowed
          hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
          type="button"
          disabled={disabled || !trim(name) || !trim(description)}
          onClick={() => handleSubmit()}
        >
          Toevoegen
        </button>
      </div>
    </Modal>
  );
}

export default ModalCreateSkill;
