import React, { memo } from "react";
import { Handle, Position } from "react-flow-renderer";

function DefaultNode({
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
      <div className="flex justify-center">
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

DefaultNode.displayName = "DefaultNode";

export default memo(DefaultNode);
