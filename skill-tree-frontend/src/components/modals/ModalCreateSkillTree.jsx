import React, { useCallback, useState } from "react";

import toastr from "toastr";
import "toastr/build/toastr.min.css";
import { trim } from "lodash";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { Modal } from "..";

export default function ModalCreateSkillTree({ show, onClose }) {
  const [skillTreeName, setSkillTreeName] = useState(null);
  const [disabled, setDisabled] = useState(false);

  const postSkillTree = useCallback(async () => SkillTreeRepository.postSkillTree({ name: skillTreeName }), [skillTreeName]);

  const handleSubmit = () => {
    setDisabled(true);

    postSkillTree()
      .then(() => {
        toastr.success(`${skillTreeName} aangemaakt`);
        setSkillTreeName(null);
        onClose();
      })
      .catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      })
      .finally(() => setDisabled(false));
  };

  return (
    <Modal
      title="Skill tree aanmaken"
      show={show}
      onClose={() => onClose()}
    >
      <div className="mt-2 flex flex-col">
        <label htmlFor="base-input" className="block font-medium text-gray-7">Naam skill tree</label>
        <input
          onChange={(e) => setSkillTreeName(e.target.value)}
          type="text"
          id="base-input"
          maxLength="255"
          className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
                   px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue
                   focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
        />
      </div>

      <div className="mt-4 flex justify-end">
        <button
          className="bg-black text-white font-bold uppercase px-6 py-3 text-sm outline-none
                  focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150 rounded"
          type="button"
          onClick={() => onClose()}
        >
          Sluiten
        </button>
        <button
          id="save"
          className="bg-pink-500 text-white active:bg-pink-600 disabled:bg-pink-50
                  font-bold uppercase text-sm px-6 py-3 rounded shadow disabled:transform-none
                  disabled:transition-none disabled:cursor-not-allowed hover:shadow-lg outline-none
                  focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
          type="button"
          disabled={disabled || !trim(skillTreeName)}
          onClick={() => handleSubmit()}
        >
          Aanmaken
        </button>
      </div>
    </Modal>
  );
}
