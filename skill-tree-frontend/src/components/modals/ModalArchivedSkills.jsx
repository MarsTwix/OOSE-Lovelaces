import React, {
  useState,
  useCallback,
  useEffect,
} from "react";
import { Disclosure } from "@headlessui/react";
import { useParams } from "react-router-dom";
import { random, uniq } from "lodash";
import { ChevronUp, Trash } from "react-feather";
import toastr from "toastr";

import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { ReactFlowService } from "../../services";
import { Modal } from "..";

function ModalArchivedSkills({
  show,
  onClose,
  addToReactFlow,
}) {
  const { id } = useParams();

  const [skills, setSkills] = useState([]);
  const [isWaiting, setIsWaiting] = useState(false);
  const [restoreDisabled, setRestoreDisabled] = useState(false);
  const [deleteDisabled, setDeleteDisabled] = useState(false);

  const handleGetSkills = useCallback(() => SkillTreeRepository.getArchivedSkills(id), []);
  const handleRestore = useCallback((skillId) => SkillTreeRepository.restoreSkill(id, skillId), [id]);
  const handleDelete = useCallback((skillId) => SkillTreeRepository.deleteSkill(id, skillId), [id]);

  const restoreSkill = (skill) => {
    setRestoreDisabled(true);
    handleRestore(skill.id)
      .then(() => {
        addToReactFlow(ReactFlowService.mapToNode(skill));

        toastr.success(`${skill.name} teruggezet`);

        handleGetSkills()
          .then((res) => {
            setSkills(res.data);
          });
      })
      .catch(() => {
        toastr.error("Er is iets misgegaan!");
      }).finally(() => {
        setRestoreDisabled(false);
      });
  };

  const deleteSkill = (skillId) => {
    setDeleteDisabled(true);
    handleDelete(skillId)
      .then(() => {
        toastr.success("Skill verwijderd");

        handleGetSkills()
          .then((res) => {
            setSkills(res.data);
          });
      })
      .catch(() => {
        toastr.error("Er is iets misgegaan!");
      }).finally(() => {
        setDeleteDisabled(false);
      });
  };

  useEffect(() => {
    setIsWaiting(true);

    handleGetSkills()
      .then((res) => {
        setSkills(res.data);
      }).finally(() => {
        setIsWaiting(false);
      });
  }, [id]);

  return (
    <Modal
      title="Archived skills"
      show={show}
      onClose={onClose}
    >
      <div className="mt-2 flex flex-col gap-2">
        {
          skills.length ? (
            skills.map((skill) => (
              <Disclosure key={`archived-skill-${skill.id}`}>
                {({ open }) => (
                  <div className="bg-pink-300 bg-opacity-25 rounded-lg">
                    <Disclosure.Button className="flex w-full justify-between items-center rounded-lg bg-pink-100
                     px-4 py-2 text-left text-md font-medium text-white hover:bg-pink-200 focus:outline-none
                     focus-visible:ring focus-visible:ring-pink-500 focus-visible:ring-opacity-75"
                    >
                      <span>{skill.name}</span>
                      <ChevronUp className={`${open ? "rotate-180 transform" : ""} h-5 w-5 text-white`} />
                    </Disclosure.Button>
                    <Disclosure.Panel className="px-4 pt-4 pb-2 text-sm text-gray-500 transition-all flex flex-col">
                      <p className="text-ellipsis overflow-hidden whitespace-nowrap">{skill.description}</p>

                      <div className="flex justify-end gap-2 pt-4">
                        <button
                          type="button"
                          disabled={restoreDisabled}
                          className="bg-black disabled:bg-black/50 flex items-center gap-2 text-white
                                  font-bold uppercase text-sm px-6 py-3 rounded shadow disabled:transform-none
                                  disabled:transition-none disabled:cursor-not-allowed outline-none focus:outline-none
                                  ease-linear transition-all duration-150"
                          onClick={() => restoreSkill(skill)}
                        >
                          Terugzetten
                        </button>
                        <button
                          type="button"
                          disabled={deleteDisabled}
                          className="bg-pink-500  active:bg-pink-600 disabled:bg-pink-300 hover:bg-pink-600
                                  flex items-center text-white font-bold uppercase text-sm px-4 py-2 rounded shadow
                                  disabled:transform-none disabled:transition-none disabled:cursor-not-allowed outline-none
                                  focus:outline-none ease-linear transition-all duration-150"
                          onClick={() => deleteSkill(skill.id)}
                        >
                          <Trash size={22} />
                        </button>
                      </div>
                    </Disclosure.Panel>
                  </div>
                )}
              </Disclosure>
            ))
          ) : (
            <div>
              {
                isWaiting ? (
                  uniq([...Array(random(3, 5))].map(() => random(0, 100))).map((o) => (
                    <div
                      className="py-3 px-6 border shadow-sm bg-gray-200 rounded-lg flex my-4 justify-between
                     items-center w-full animate-pulse"
                      key={`loader-${o}`}
                    >
                      <div className="bg-slate-300 w-48 h-8 rounded-md" />
                      <div className="bg-slate-300 w-14 h-8 rounded-md" />
                    </div>
                  ))
                ) : "Geen gearchiveerde skills gevonden"
              }
            </div>
          )
        }
      </div>
    </Modal>
  );
}

export default ModalArchivedSkills;
