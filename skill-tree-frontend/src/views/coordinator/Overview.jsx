import React, {
  Fragment, useCallback, useEffect, useState,
} from "react";
import { Menu, Transition } from "@headlessui/react";

import { Link } from "react-router-dom";
import {
  Plus, Archive, Edit2, ChevronDown, ArrowRight,
} from "react-feather";
import { random, uniq } from "lodash";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";

import { ModalCreateSkillTree, ModalArchiveSkillTree, ModalEditSkillTree } from "../../components/modals";

export default function Overview() {
  const [showCreate, setShowCreate] = useState(false);
  const [showArchive, setShowArchive] = useState(false);
  const [selectedSkillTree, setSelectedSkillTree] = useState(null);
  const [skillTrees, setSkillTrees] = useState([]);

  const [isWaiting, setIsWaiting] = useState(false);

  const getSkillTrees = useCallback(() => SkillTreeRepository.getSkillTrees(), []);

  useEffect(() => {
    setIsWaiting(true);
    getSkillTrees()
      .then((res) => {
        setSkillTrees(res.data);
      }).finally(() => {
        setIsWaiting(false);
      });
  }, []);

  const closeShowCreate = () => {
    setShowCreate(false);

    getSkillTrees()
      .then((res) => {
        setSkillTrees(res.data);
      });
  };

  const closeShowEdit = () => {
    setSelectedSkillTree(null);

    getSkillTrees()
      .then((res) => {
        setSkillTrees(res.data);
      });
  };

  const closeShowArchive = (success) => {
    setShowArchive(false);

    if (success) {
      getSkillTrees()
        .then((res) => {
          setSkillTrees(res.data);
        });
    }
  };

  return (
    <div className="w-full">
      <div className="w-2/3 mx-auto">
        <div className="flex justify-between items-center mt-16">
          <h1 className="font-medium leading-tight text-3xl mt-0 mb-2">
            Overzicht
          </h1>
          <div className="flex gap-2">
            <button
              id="save"
              className="bg-black flex items-center gap-2 text-white font-bold uppercase text-sm px-6 py-3 rounded
              shadow disabled:transform-none disabled:transition-none disabled:cursor-not-allowed outline-none
              focus:outline-none ease-linear transition-all duration-150"
              type="button"
              onClick={() => setShowCreate(true)}
            >
              Aanmaken
              <Plus />
            </button>
          </div>
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
                        <Menu.Button className="inline-flex w-full justify-center rounded-md bg-pink-500
                        px-4 py-2 text-sm font-medium text-white hover:bg-pink-600 focus:outline-none
                        focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75"
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
                                  className={`${active ? "bg-pink-500 text-white" : "text-gray-900"}
                                  group flex w-full items-center rounded-md px-2 py-2 text-sm`}
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
                                  className={`${active ? "bg-pink-500 text-white" : "text-gray-900"}
                                  group flex w-full items-center rounded-md px-2 py-2 text-sm`}
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
                                  className={`${active ? "bg-pink-500 text-white" : "text-gray-900"}
                                  group flex w-full items-center rounded-md px-2 py-2 text-sm`}
                                  onClick={() => setShowArchive(skillTree)}
                                >
                                  <Archive
                                    className={`${active ? "text-white" : "text-red-600"} mr-2 h-5 w-5`}
                                    aria-hidden="true"
                                  />
                                  Archiveren
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
                        <div
                          className="py-3 px-6 border shadow-sm bg-gray-200 rounded-lg flex
                        my-4 justify-between items-center w-full animate-pulse"
                          key={`loader-${o}`}
                        >
                          <div className="bg-slate-300 w-48 h-8 rounded-md" />
                          <div className="bg-slate-300 w-14 h-8 rounded-md" />
                        </div>
                      ))
                    ) : "Geen skill trees gevonden"
                  }
                </div>
              )
        }
        </div>

        <ModalCreateSkillTree show={showCreate} onClose={() => closeShowCreate()} />
        <ModalEditSkillTree show={selectedSkillTree != null} onClose={() => closeShowEdit()} skillTree={selectedSkillTree} />
        <ModalArchiveSkillTree
          show={showArchive !== null}
          skillTree={showArchive}
          onClose={(success) => closeShowArchive(success)}
        />
      </div>
    </div>
  );
}
