/* eslint-disable no-param-reassign */
import React, {
  Fragment,
  useCallback,
  useEffect,
  useMemo,
  useState,
} from "react";
import {
  Archive,
  HelpCircle,
  Plus, Users,
} from "react-feather";
import { useParams } from "react-router-dom";

import ReactFlow, {
  addEdge,
  Background, Controls, MiniMap, useEdgesState, useNodesState, MarkerType,
} from "react-flow-renderer";

import toastr from "toastr";
import { Popover, Transition } from "@headlessui/react";

import { ModalArchivedSkills, ModalCreateSkill, ModalEditSkill } from "../../components/modals";
import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import ReactFlowService from "../../services/ReactFlowService";
import { Loader } from "../../components";
import { DefaultNode, EvidenceRequiredNode } from "../../components/nodes";
import ModalSkillTreeStudents from "../../components/modals/ModalSkillTreeStudents";

const solutions = [
  {
    name: "Insights",
    description: "Measure actions your users take",
    href: "##",
    icon: Plus,
  },
  {
    name: "Automations",
    description: "Create your own targeted content",
    href: "##",
    icon: Plus,
  },
  {
    name: "Reports",
    description: "Keep track of your growth",
    href: "##",
    icon: Plus,
  },
];

export default function SkillTree() {
  const { id } = useParams();

  const [showCreate, setShowCreate] = useState(false);
  const [showArchived, setShowArchived] = useState(false);
  const [showEdit, setShowEdit] = useState(null);
  const [showUsers, setShowUsers] = useState(false);

  const [skillTree, setSkillTree] = useState(null);
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);

  const nodeTypes = useMemo(() => ({
    evidenceRequired: EvidenceRequiredNode, default: DefaultNode,
  }), []);

  const getSkillTree = useCallback(() => SkillTreeRepository.getSkillTree(id), [id]);
  const postEdge = useCallback((sourceId, targetId) => SkillTreeRepository.postEdge(id, { sourceId, targetId, id: parseInt(id, 10) }), [id]);
  const removeEdge = useCallback((skillCouplingId) => SkillTreeRepository.removeEdge(id, parseInt(skillCouplingId, 10)), [id]);
  const putSkill = useCallback((skillId, data) => SkillTreeRepository.putSkill(id, skillId, data), [id]);

  useEffect(() => {
    getSkillTree()
      .then((res) => {
        setSkillTree(res.data);

        setNodes(res.data.skills.map((skill) => ReactFlowService.mapToNode(skill)));
        setEdges(res.data.edges.map((edge) => ReactFlowService.mapToEdge(edge)));
      });
  }, [id]);

  const addEdgeToReactFlow = (params) => setEdges((eds) => addEdge({ ...params, type: "smoothstep", markerEnd: { type: MarkerType.ArrowClosed, width: 40, height: 40 } }, eds));
  const addNodeToReactFlow = (node) => {
    setNodes((nds) => nds.concat(node));

    getSkillTree()
      .then((res) => {
        setSkillTree(res.data);
      });
  };

  const updateNode = (node) => {
    const skill = skillTree.skills.find((s) => s.id.toString() === node.id);

    putSkill(skill.id, { ...skill, x: Math.floor(node.position.x), y: Math.floor(node.position.y) })
      .then(() => {
        toastr.success(`${node.data.label} is verplaatst!`);
      })
      .catch(() => {
        toastr.error("Error: Kon skill niet verplaatsen");
      });
  };

  const onConnectNodes = (params) => {
    if (edges.some((edge) => edge.target === params.target && edge.source === params.source)) {
      toastr.error("Error", "Deze koppeling bestaat al.");
    } else {
      postEdge(parseInt(params.source, 10), parseInt(params.target, 10)).then((r) => {
        params.id = r.data.id;
        addEdgeToReactFlow(params);
      });
    }
  };
  const onArchiveSkill = useCallback((skill) => {
    setNodes((nds) => nds.filter((n) => n.id !== skill.id.toString()));
    setEdges((els) => els.filter((e) => e.source !== skill.id.toString() && e.target !== skill.id.toString()));

    setShowEdit(null);
  }, [setNodes, setEdges]);

  const onDeleteEdge = (e) => {
    removeEdge(e[0].id)
      .then(() => {
        setEdges((eds) => eds.filter((edge) => edge.id !== e));
        toastr.success("Koppeling verwijderd");
      }).catch(() => {
        toastr.error("Er is iets fout gegaan, probeer het later opnieuw.");
      });
  };

  const onEditSkill = useCallback((skill) => {
    setNodes((nds) => nds.map((node) => {
      if (node.id === skill.id) {
        return {
          ...node,
          data: {
            label: skill.name,
            description: skill.description,
            evidenceRequired: skill.evidenceRequired,
            assessmentCriteria: skill.assessmentCriteria,
          },
          type: skill.evidenceRequired ? "evidenceRequired" : "default",
        };
      }

      return node;
    }));
    setShowEdit(null);
  }, [setNodes, setShowEdit]);
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
            <button
              type="button"
              className="w-11 h-10 bg-pink-500 hover:bg-pink-600 items-center flex
            first:rounded-l-md last:rounded-r-md border-r-2 border-pink-300"
              onClick={() => setShowCreate(!showCreate)}
            >
              <Plus size={22} className="mx-auto" />
            </button>
            <button
              type="button"
              className="w-11 h-10 bg-pink-500 hover:bg-pink-600 items-center flex
            first:rounded-l-md last:rounded-r-md border-r-2 border-pink-300"
              onClick={() => setShowArchived(true)}
            >
              <Archive size={22} className="mx-auto" />
            </button>
            <button
              type="button"
              className="w-11 h-10 bg-pink-500 hover:bg-pink-600 items-center flex
            first:rounded-l-md last:rounded-r-md"
              onClick={() => setShowUsers(true)}
            >
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
                            className="flow-root rounded-md px-2 py-2 transition duration-150
                            ease-in-out hover:bg-pink-100 focus:outline-none focus-visible:ring
                            focus-visible:ring-orange-500 focus-visible:ring-opacity-50"
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
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onNodeDoubleClick={(e, node) => setShowEdit(node)}
        onConnect={onConnectNodes}
        onEdgesDelete={(e) => onDeleteEdge(e)}
        onNodeDragStop={(e, node) => updateNode(node)}
        nodeTypes={nodeTypes}
        connectionLineType="smoothstep"
        fitView
        snapToGrid
        className="absolute w-full h-full"
      >
        <Background />
        <Controls />
        <MiniMap />
      </ReactFlow>

      {
        showCreate
          ? (
            <ModalCreateSkill
              show={showCreate}
              onClose={() => setShowCreate(false)}
              addToReactFlow={(s) => addNodeToReactFlow(s)}
            />
          ) : null
      }

      {
        showEdit
          ? (
            <ModalEditSkill
              node={showEdit}
              show={showEdit != null}
              onClose={() => setShowEdit(null)}
              onArchive={(s) => onArchiveSkill(s)}
              onEdit={(s) => onEditSkill(s)}
            />
          ) : null
      }

      {
        showArchived
          ? (
            <ModalArchivedSkills
              show={showArchived}
              onClose={() => setShowArchived(false)}
              addToReactFlow={(s) => addNodeToReactFlow(s)}
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

      <div className={`absolute z-50 bg-black transition-all duration-700 
      w-full h-full flex justify-center items-center 
      ${!skillTree ? "bg-opacity-40" : "bg-opacity-0 opacity-0 z-0"}`}
      >
        <div className="flex flex-col gap-y-4 items-center justify-center">
          <Loader />
        </div>
      </div>
    </div>
  );
}
