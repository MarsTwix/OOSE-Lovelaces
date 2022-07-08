import React, {
  useCallback, useEffect, useMemo, useState,
} from "react";
import { useParams } from "react-router-dom";
import dagre from "dagre";
import ReactFlow, {
  Background, Controls,
} from "react-flow-renderer";

import SkillTreeRepository from "../../repositories/SkillTreeRepository";
import { DefaultNode, EvidenceRequiredNode } from "../../components/nodes";
import ReactFlowService from "../../services/ReactFlowService";

const dagreGraph = new dagre.graphlib.Graph();
dagreGraph.setDefaultEdgeLabel(() => ({}));

function SkillTree() {
  const { id } = useParams();

  const [skillTree, setSkillTree] = useState(null);
  const [nodes, setNodes] = useState([]);
  const [edges, setEdges] = useState([]);

  const getSkillTree = useCallback(() => SkillTreeRepository.getSkillTree(id), [id]);

  const nodeTypes = useMemo(() => (
    { evidenceRequired: EvidenceRequiredNode, default: DefaultNode }
  ), []);

  useEffect(() => {
    getSkillTree()
      .then((res) => {
        setSkillTree(res.data);

        setNodes(res.data.skills.map((skill) => ReactFlowService.mapToNode(skill, {
          draggable: false,
          connectable: false,
          selectable: false,
        })));
        setEdges(res.data.edges.map((edge) => ReactFlowService.mapToEdge(edge)));
      });
  }, [getSkillTree]);

  return (
    <div className="w-full relative ml-16">
      <div className="absolute left-6 top-4 right-6 z-10 flex justify-between items-center">
        <h3 className="font-medium leading-tight text-2xl text-gray-900">{skillTree?.name || ""}</h3>
        <div />
      </div>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        nodeTypes={nodeTypes}
        draggable={false}
        fitView
        className="absolute w-full h-full"
      >
        <Background />
        <Controls />
      </ReactFlow>
    </div>
  );
}

export default SkillTree;
