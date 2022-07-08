import React, { useCallback, useState, useEffect } from "react";

import toastr from "toastr";
import "toastr/build/toastr.min.css";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { Modal } from "..";

export default function ModalEditSkillTree({ show, onClose, skillTree }) {
  const [disabled, setDisabled] = useState(false);
  const [skillTreeName, setSkillTreeName] = useState("");

  const editSkillTree = useCallback(() => SkillTreeRepository.editSkillTree(skillTree.id, { name: skillTreeName }), [skillTree, skillTreeName]);

  useEffect(() => {
    setSkillTreeName(skillTree?.name);
  }, [skillTree]);

  const handleSubmit = () => {
    setDisabled(true);

    editSkillTree()
      .then(() => {
        toastr.success(`${skillTreeName} bewerkt`);
        onClose();
      })
      .catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      })
      .finally(() => setDisabled(false));
  };

  if (!skillTree) {
    return <div />;
  }

  return (
    <Modal
      title="Bijwerken skill tree"
      show={show}
      onClose={onClose}
    >
      <div className="mt-2 flex flex-col">
        <label htmlFor="base-input" className="block font-medium text-gray-7">Naam skill tree</label>
        <input
          type="text"
          id="base-input"
          value={skillTreeName ?? ""}
          placeholder={skillTree.name}
          onChange={(e) => { setSkillTreeName(e.target.value); }}
          maxLength="255"
          className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
                   px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue
                   focus:border-blue-300 transition duration-150 ease-in-out"
        />
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
          className="bg-pink-500 text-white active:bg-pink-600 disabled:bg-pink-50 font-bold
                   uppercase text-sm px-6 py-3 rounded shadow disabled:transform-none disabled:transition-none
                   disabled:cursor-not-allowed hover:shadow-lg outline-none focus:outline-none mr-1 mb-1
                   ease-linear transition-all duration-150"
          type="submit"
          disabled={skillTreeName === "" || skillTreeName === skillTree.name || disabled}
          onClick={() => handleSubmit()}
        >
          Bewerken
        </button>
      </div>
    </Modal>
  );
}
