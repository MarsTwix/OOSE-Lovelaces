/* eslint-disable no-nested-ternary */
import { MarkerType } from "react-flow-renderer";

export default class ReactFlowService {
  static mapToNode(skill, options = {}) {
    return this.mapNodeToStyledNode({
      id: skill.id.toString(),
      data: {
        label: skill.name,
        description: skill.description,
        evidenceRequired: skill.evidenceRequired,
        assessmentCriteria: skill.assessmentCriteria,
        grade: skill.grade,
      },
      type: skill.evidenceRequired ? "evidenceRequired" : "default",
      position: { x: skill.x, y: skill.y },
      ...options,
    });
  }

  static mapToEdge(edge) {
    return {
      id: edge.id.toString(),
      target: edge.targetId.toString(),
      source: edge.sourceId.toString(),
      type: "smoothstep",
      markerEnd: {
        type: MarkerType.ArrowClosed,
        width: 40,
        height: 40,
      },
    };
  }

  static mapNodeToStyledNode(node) {
    const hasGrade = node?.data.grade && node.data.grade > 0;
    const isSufficient = node.data.grade >= 5.5;

    return {
      ...node,
      style: {
        border: hasGrade ? (isSufficient ? "1px solid #2da318" : "1px solid #a32d18") : "",
        background: hasGrade ? (isSufficient ? "#dfffcc" : "#ffd6cc") : "",
      },
    };
  }
}
