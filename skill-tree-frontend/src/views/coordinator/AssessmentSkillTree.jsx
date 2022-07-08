/* eslint-disable no-param-reassign */
import React, {
  Fragment,
  useCallback,
  useEffect,
  useMemo,
  useState,
} from "react";
import {
  Eye,
  HelpCircle, MessageSquare,
  UserCheck, Users,
} from "react-feather";
import { useParams } from "react-router-dom";

import ReactFlow, {
  Background, Controls, MiniMap, useEdgesState, useNodesState,
} from "react-flow-renderer";

import { Popover, Transition } from "@headlessui/react";
import { concat } from "lodash";

import ReactFlowService from "../../services/ReactFlowService";
import { Loader } from "../../components";
import { DefaultNode, EvidenceRequiredNode } from "../../components/nodes";
import UserRepository from "../../repositories/UserRepository";
import { ModalStudentSkillDetails } from "../../components/modals";
import ModalSkillTreeStudents from "../../components/modals/ModalSkillTreeStudents";

const solutions = [
  {
    name: "Skill voortgang inzien",
    description: "Klik op een skill om de voortgang van een student in te zien.",
    href: "##",
    icon: Eye,
  },
  {
    name: "Beoordelen",
    description: "Beoordeel de skills van een student in het skill menu.",
    href: "##",
    icon: UserCheck,
  },
  {
    name: "Feedback geven",
    description: "Geef studenten feedback op skills.",
    href: "##",
    icon: MessageSquare,
  },
];

export default function AssessmentSkillTree() {
  const { id, studentId } = useParams();

  const [showUsers, setShowUsers] = useState(false);

  // eslint-disable-next-line no-unused-vars
  const [skillTree, setSkillTree] = useState(null);
  const [nodes, setNodes] = useNodesState([]);
  const [edges, setEdges] = useEdgesState([]);
  const [showDetails, setShowDetails] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const nodeTypes = useMemo(() => (
    { evidenceRequired: EvidenceRequiredNode, default: DefaultNode }
  ), []);

  // Callbacks
  const getSkillTree = useCallback(() => UserRepository.getSkillTreeFromStudent(studentId, id), [id, studentId]);

  const onGiveAssessment = useCallback((n) => {
    setNodes((nds) => concat(nds.filter((o) => o.id !== n.id), ReactFlowService.mapNodeToStyledNode(n)));
  }, [setNodes]);

  // Effects
  useEffect(() => {
    setIsLoading(true);
    getSkillTree()
      .then((res) => {
        setSkillTree(res.data);

        setNodes(res.data.userSkills.map((skill) => (
          ReactFlowService.mapToNode(skill, { grade: skill.grade })
        )));
        setEdges(res.data.edges.map((edge) => ReactFlowService.mapToEdge(edge)));
        setIsLoading(false);
      });
  }, [id, studentId]);

  return (
    <div className="w-full relative ml-16">
      <div className="absolute left-6 top-4 right-6 z-10 flex justify-between items-center">
        {
          skillTree?.name ? (
            <h3 className="font-medium leading-tight text-2xl text-gray-900 bg-white py-2 px-3 rounded-md border-2">
              {skillTree.name}
            </h3>
          ) : <div />
        }
        <div className="relative flex gap-2 text-white">
          <div className="flex">
            <button type="button" className="w-11 h-10 bg-pink-500 hover:bg-pink-600 items-center flex first:rounded-l-md last:rounded-r-md" onClick={() => setShowUsers(true)}>
              <Users size={22} className="mx-auto" />
            </button>
          </div>
          <div className="flex">
            <Popover className="relative">
              {({ open }) => (
                <>
                  <Popover.Button
                    className={`
                        ${open ? "" : "text-opacity-90"}
                        w-11 h-10 bg-pink-500 hover:bg-pink-600 items-center flex rounded-md`}
                  >
                    <HelpCircle size={22} className="mx-auto" />
                  </Popover.Button>
                  <Transition
                    as={Fragment}
                    enter="transition ease-out duration-200"
                    enterFrom="opacity-0 translate-y-1"
                    enterTo="opacity-100 translate-y-0"
                    leave="transition ease-in duration-150"
                    leaveFrom="opacity-100 translate-y-0"
                    leaveTo="opacity-0 translate-y-1"
                  >
                    <Popover.Panel className="absolute right-0 z-10 mt-3 w-screen max-w-sm transform px-4 sm:px-0">
                      <div className="overflow-hidden rounded-lg shadow-lg ring-1 ring-black ring-opacity-5">
                        <div className="relative flex flex-col gap-8 bg-white p-7">
                          {solutions.map((item) => (
                            <a
                              key={item.name}
                              href={item.href}
                              className="-m-3 flex items-center rounded-lg p-2 transition duration-150
                              ease-in-out hover:bg-gray-50 focus:outline-none focus-visible:ring
                              focus-visible:ring-orange-500 focus-visible:ring-opacity-50"
                            >
                              <div className="flex h-10 w-10 shrink-0 items-center justify-center text-white sm:h-12 sm:w-12">
                                <item.icon aria-hidden="true" className="text-pink" />
                              </div>
                              <div className="ml-4">
                                <p className="text-sm font-medium text-gray-900">
                                  {item.name}
                                </p>
                                <p className="text-sm text-gray-500">
                                  {item.description}
                                </p>
                              </div>
                            </a>
                          ))}
                        </div>
                        <div className="bg-pink-300  p-4">
                          <a
                            href="##"
                            className="flow-root rounded-md px-2 py-2 transition duration-150 ease-in-out
                            hover:bg-pink-100 focus:outline-none focus-visible:ring focus-visible:ring-orange-500
                            focus-visible:ring-opacity-50"
                          >
                            <span className="flex items-center">
                              <span className="text-sm font-medium text-white">
                                Documentation
                              </span>
                            </span>
                            <span className="block text-sm text-white">
                              Start integrating products and tools
                            </span>
                          </a>
                        </div>
                      </div>
                    </Popover.Panel>
                  </Transition>
                </>
              )}
            </Popover>
          </div>
        </div>
      </div>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        nodeTypes={nodeTypes}
        connectionLineType="smoothstep"
        draggable={false}
        onNodeClick={(e, node) => setShowDetails(node)}
        fitView
        className="absolute w-full h-full"
      >
        <Background />
        <Controls />
        <MiniMap />
      </ReactFlow>

      {
        showDetails
          ? (
            <ModalStudentSkillDetails
              node={showDetails}
              show={showDetails != null}
              studentId={studentId}
              onGiveAssessment={(n) => onGiveAssessment(n)}
              onClose={() => setShowDetails(null)}
            />
          ) : null
      }

      {
        showUsers
          ? (
            <ModalSkillTreeStudents
              show={showUsers}
              onClose={() => setShowUsers(false)}
              id={id}
            />
          ) : null
      }

      <div className={`absolute z-50 bg-black transition-all duration-700 w-full h-full
      flex justify-center items-center 
      ${isLoading ? "bg-opacity-40" : "bg-opacity-0 opacity-0 z-0"}`}
      >
        <div className="flex flex-col gap-y-4 items-center justify-center">
          <Loader />
        </div>
      </div>
    </div>
  );
}
