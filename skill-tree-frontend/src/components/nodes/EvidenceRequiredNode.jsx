import React, { memo } from "react";
import { UploadCloud } from "react-feather";
import { Handle, Position } from "react-flow-renderer";

function EvidenceRequiredNode({
  data,
  isConnectable,
  targetPosition = Position.Top,
  sourcePosition = Position.Bottom,
}) {
  return (
    <>
      <Handle
        type="target"
        position={targetPosition}
        isConnectable={isConnectable}
        style={{ width: "8px", height: "8px" }}
      />
      <div className="flex gap-2 justify-center">
        <UploadCloud className="h-4 w-4 shrink-0" />
        <span className="truncate">
          {data?.label}
        </span>
      </div>
      <Handle
        type="source"
        position={sourcePosition}
        isConnectable={isConnectable}
        style={{ width: "8px", height: "8px" }}
      />
    </>
  );
}

EvidenceRequiredNode.displayName = "EvidenceRequiredNode";

export default memo(EvidenceRequiredNode);
