import React, {
  Fragment,
  useCallback,
  useEffect,
  useState,
} from "react";
import { Menu, Transition } from "@headlessui/react";

import { random, uniq } from "lodash";
import { Link } from "react-router-dom";
import {
  Edit2,
  ChevronDown,
  ArrowRight,
  Trash,
  RotateCcw,
} from "react-feather";
import toastr from "toastr";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import "toastr/build/toastr.min.css";
import { ModalEditSkillTree } from "../../components/modals";

export default function ArchivedOverview() {
  const [skillTrees, setSkillTrees] = useState([]);
  const [selectedSkillTree, setSelectedSkillTree] = useState(null);
  const [isWaiting, setIsWaiting] = useState(false);

  const getSkillTrees = useCallback(() => SkillTreeRepository.getArchivedSkillTrees(), []);
  const handleRestore = useCallback((id) => SkillTreeRepository.restoreSkillTree(id), []);
  const handleDelete = useCallback((id) => SkillTreeRepository.deleteSkillTree(id), []);

  const closeShowEdit = () => {
    setSelectedSkillTree(null);

    getSkillTrees()
      .then((res) => {
        setSkillTrees(res.data);
      });
  };

  const restoreSkillTree = (id) => {
    handleRestore(id)
      .then(() => {
        toastr.success("Skill Tree is teruggezet");
        getSkillTrees()
          .then((res) => setSkillTrees(res.data));
      })
      .catch(() => {
        toastr.error("Er is iets misgegaan.");
      });
  };

  const deleteSkillTree = (id) => {
    handleDelete(id)
      .then(() => {
        toastr.success("Skill tree is verwijderd");
        getSkillTrees()
          .then((res) => setSkillTrees(res.data));
      })
      .catch(() => {
        toastr.error("Er is iets misgegaan.");
      });
  };

  useEffect(() => {
    setIsWaiting(true);

    getSkillTrees()
      .then((res) => {
        setSkillTrees(res.data);
      }).finally(() => {
        setIsWaiting(false);
      });
  }, [getSkillTrees]);

  return (
    <div className="w-full">
      <div className="w-2/3 mx-auto mt-16">
        <div className="flex justify-between items-center">
          <h1 className="font-medium leading-tight text-3xl mt-0 mb-2">
            Gearchiveerd
          </h1>
        </div>

        <div className="mt-4 mb-4 flex flex-col gap-3">
          {
            skillTrees.length
              ? skillTrees.map((skillTree) => (
                <div key={skillTree.id} className="py-3 px-6 border shadow-sm bg-gray-50 rounded-lg flex justify-between items-center">
                  <p className="font-medium text-lg">{skillTree.name}</p>
                  <div className="flex gap-2">
                    <Menu as="div" className="relative inline-block text-left">
                      <div>
                        <Menu.Button className="inline-flex w-full justify-center rounded-md
                        bg-slate-700 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800
                        focus:outline-none focus-visible:ring-2 focus-visible:ring-white
                        focus-visible:ring-opacity-75"
                        >
                          Opties
                          <ChevronDown
                            className="ml-2 -mr-1 h-5 w-5 text-violet-200 hover:text-violet-100"
                            aria-hidden="true"
                          />
                        </Menu.Button>
                      </div>
                      <Transition
                        as={Fragment}
                        enter="transition ease-out duration-100"
                        enterFrom="transform opacity-0 scale-95"
                        enterTo="transform opacity-100 scale-100"
                        leave="transition ease-in duration-75"
                        leaveFrom="transform opacity-100 scale-100"
                        leaveTo="transform opacity-0 scale-95"
                      >
                        <Menu.Items className="absolute right-0 mt-2 w-56 origin-top-right divide-y
                        divide-gray-100 rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5
                        focus:outline-none z-40"
                        >
                          <div className="px-1 py-1 ">
                            <Menu.Item>
                              {({ active }) => (
                                <Link
                                  to={`/admin/overview/${skillTree.id}`}
                                  className={`${active ? "bg-pink-600 text-white" : "text-gray-900"} group flex w-full items-center rounded-md px-2 py-2 text-sm`}
                                >
                                  <ArrowRight
                                    className="mr-2 h-5 w-5"
                                    aria-hidden="true"
                                  />
                                  Bekijken
                                </Link>
                              )}
                            </Menu.Item>
                            <Menu.Item>
                              {({ active }) => (
                                <button
                                  type="button"
                                  onClick={() => setSelectedSkillTree(skillTree)}
                                  className={`${active ? "bg-pink-600 text-white" : "text-gray-900"} group flex w-full items-center rounded-md px-2 py-2 text-sm`}
                                >
                                  <Edit2
                                    className="mr-2 h-5 w-5"
                                    aria-hidden="true"
                                  />
                                  Bewerken
                                </button>
                              )}
                            </Menu.Item>
                          </div>
                          <div className="px-1 py-1">
                            <Menu.Item>
                              {({ active }) => (
                                <button
                                  type="button"
                                  className={`${active ? "bg-pink-600 text-white" : "text-gray-900"} group flex w-full items-center rounded-md px-2 py-2 text-sm`}
                                  onClick={() => restoreSkillTree(skillTree.id)}
                                >
                                  <RotateCcw
                                    className={`${active ? "text-white" : "text-blue-600"} mr-2 h-5 w-5`}
                                    aria-hidden="true"
                                  />
                                  Herstellen
                                </button>
                              )}
                            </Menu.Item>
                            <Menu.Item>
                              {({ active }) => (
                                <button
                                  type="button"
                                  className={`${active ? "bg-pink-600 text-white" : "text-gray-900"} group flex w-full items-center rounded-md px-2 py-2 text-sm`}
                                  onClick={() => deleteSkillTree(skillTree.id)}
                                >
                                  <Trash
                                    className={`${active ? "text-white" : "text-red-600"} mr-2 h-5 w-5`}
                                    aria-hidden="true"
                                  />
                                  Permanent verwijderen
                                </button>
                              )}
                            </Menu.Item>
                          </div>
                        </Menu.Items>
                      </Transition>
                    </Menu>
                  </div>
                </div>
              )) : (
                <div>
                  {
                    isWaiting ? (
                      uniq([...Array(random(3, 5))].map(() => random(0, 100))).map((o) => (
                        <div className="py-3 px-6 border shadow-sm bg-gray-200 rounded-lg flex my-4 justify-between items-center w-full animate-pulse" key={`loader-${o}`}>
                          <div className="bg-slate-300 w-48 h-8 rounded-md" />
                          <div className="bg-slate-300 w-14 h-8 rounded-md" />
                        </div>
                      ))
                    ) : "Geen gearchiveerde skill trees gevonden"
                  }
                </div>
              )
          }
        </div>

        <ModalEditSkillTree
          show={selectedSkillTree != null}
          onClose={() => closeShowEdit()}
          skillTree={selectedSkillTree}
        />
      </div>
    </div>
  );
}
