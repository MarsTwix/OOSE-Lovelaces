import React, { useCallback, useState } from "react";

import toastr from "toastr";
import "toastr/build/toastr.min.css";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { Modal } from "..";

export default function ModalDeleteSkillTree({ show, onClose, skillTree }) {
  const [disabled, setDisabled] = useState(false);

  const deleteSkillTree = useCallback(() => SkillTreeRepository.deleteSkillTree(skillTree.id), [skillTree]);

  const handleSubmit = () => {
    setDisabled(true);

    deleteSkillTree()
      .then(() => {
        toastr.success(`${skillTree.name} is verwijderd`);
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
      title="Verwijderen skill tree"
      show={show}
      onClose={onClose}
    >

      <div className="mt-2 flex flex-col">
        <p>
          Weet u zeker dat u skill tree
          <strong>
            {` ${skillTree.name} `}
          </strong>
          wilt verwijderen?
        </p>
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
          className="text-white bg-pink-500 hover:bg-pink-700 disabled:bg-pink-300 font-bold
                   uppercase text-sm px-6 py-3 rounded shadow disabled:transform-none disabled:transition-none
                   disabled:cursor-not-allowed hover:shadow-lg outline-none focus:outline-none mr-1 mb-1
                   ease-linear transition-all duration-150"
          type="button"
          disabled={disabled}
          onClick={() => handleSubmit()}
        >
          Verwijderen
        </button>
      </div>
    </Modal>
  );
}
