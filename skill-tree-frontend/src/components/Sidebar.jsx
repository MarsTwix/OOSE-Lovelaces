import React from "react";
import { LogOut } from "react-feather";
import { Link, useLocation } from "react-router-dom";

import NavigationItems from "../routes/NavigationItems";

function Sidebar() {
  const location = useLocation();

  return (
    <div className="fixed h-screen left-0 top-0 py-16 pb-2 overflow-y-auto bg-black w-16 z-50">
      <div className="flex flex-col h-full justify-between">
        <aside>
          <ul>
            {
              NavigationItems.map((item) => (
                <li key={item.path} className={`w-full text-white ${location.pathname === item.path ? "bg-pink-500" : "hover:bg-pink-600"}`}>

                  <Link to={item.path} className="flex items-center justify-center py-5 " title={item.name}>
                    <item.icon size="24" />
                  </Link>
                </li>
              ))
            }
          </ul>
        </aside>
        <aside>
          <ul>
            <li className="hover:bg-pink-600 w-full ">
              <Link to="/logout" className="flex items-center justify-center py-4 text-white">
                <LogOut size="24" className="han-white" />
              </Link>
            </li>
          </ul>
        </aside>
      </div>
    </div>
  );
}

export default Sidebar;
