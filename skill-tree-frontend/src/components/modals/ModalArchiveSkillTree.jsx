import React, { useCallback, useState } from "react";
import toastr from "toastr";
import "toastr/build/toastr.min.css";

import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { Modal } from "..";

export default function ModalArchiveSkillTree({ show, onClose, skillTree }) {
  const [disabled, setDisabled] = useState(false);

  const archiveSkillTree = useCallback(() => SkillTreeRepository.archiveSkillTree(skillTree.id), [skillTree]);

  const handleSubmit = () => {
    setDisabled(true);

    archiveSkillTree()
      .then(() => {
        toastr.success(`${skillTree.name} gearchiveerd`);
        onClose(true);
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
      title="Archive skill tree"
      show={show}
      onClose={onClose}
    >
      <div className="mt-2 flex flex-col">
        <p>
          Weet u zeker dat u skill tree
          <strong>
            {` ${skillTree.name} `}
          </strong>
          wilt archiveren?
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
          id="archive"
          className="bg-pink-500 text-white active:bg-pink-600 disabled:bg-pink-50 font-bold uppercase text-sm px-6
                  py-3 rounded shadow disabled:transform-none disabled:transition-none disabled:cursor-not-allowed
                  hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
          type="button"
          disabled={disabled}
          onClick={() => handleSubmit()}
        >
          Archiveren
        </button>
      </div>
    </Modal>
  );
}
