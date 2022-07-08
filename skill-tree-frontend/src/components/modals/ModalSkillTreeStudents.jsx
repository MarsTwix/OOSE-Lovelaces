import React, {
  useState,
  useCallback,
  useEffect,
} from "react";
import {
  Link, useNavigate, useParams,
} from "react-router-dom";
import { random, uniq } from "lodash";
import { ArrowRight } from "react-feather";
import { Modal } from "..";

import UserRepository from "../../repositories/UserRepository";

function ModalSkillTreeStudents({
  show,
  onClose,
}) {
  const { id } = useParams();
  const navigate = useNavigate();

  const [users, setUsers] = useState([]);
  const [isWaiting, setIsWaiting] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [filteredUsers, setFilteredUsers] = useState([]);

  // Callbacks
  const handleGetUsers = useCallback(() => UserRepository.getAllUsers(), []);

  const redirectAndClose = (studentId) => {
    onClose();
    navigate(`/overview/${id}/${studentId}`);
  };

  // Effects
  useEffect(() => {
    setIsWaiting(true);

    handleGetUsers()
      .then((res) => {
        setUsers(res.data);
      }).finally(() => {
        setIsWaiting(false);
      });
  }, [id]);

  useEffect(() => {
    if (searchQuery === "") {
      setFilteredUsers(users);
    } else {
      const newFilteredUsers = users.filter((user) => user.email
        .toLowerCase()
        .includes(searchQuery.toLowerCase())
        || `${user.firstname.toLowerCase()} ${user.lastname.toLowerCase()}`.includes(searchQuery.toLowerCase()));
      setFilteredUsers(newFilteredUsers);
    }
  }, [searchQuery, users]);

  return (
    <Modal
      title="Gebruikers"
      show={show}
      onClose={onClose}
    >
      <div className="mt-2 flex flex-col gap-2">
        <input
          id="searchUsers"
          type="text"
          name="searchUsers"
          placeholder="Zoek gebruikers op naam of e-mailadres."
          onChange={(e) => setSearchQuery(e.target.value)}
          className="mt-1 bg-gray-50 border border-gray-400 block w-full shadow-sm rounded-md
                  px-3 py-3 placeholder-gray-7 focus:outline-none focus:shadow-outline-blue
                  focus:ring-pink-500 focus:border-pink-500 focus:z-10 transition duration-150
                  ease-in-out sm:text-sm sm:leading-5"
        />
        <div className="overflow-y-auto flex flex-col gap-2 h-[50vh]">
          {
            filteredUsers.length ? (
              filteredUsers.map((user) => (
                <div
                  key={`user-${user.id}`}
                  className="flex-shrink-0 overflow-hidden py-2 px-6 border
                         shadow-sm bg-gray-50 rounded-lg flex justify-between items-center mr-2"
                >
                  <p className="font-medium text-sm truncate">{`${user.firstname} ${user.lastname}`}</p>
                  <div className="flex gap-2">
                    <Link to={`/overview/${id}/${user.id}`}>
                      <button
                        className="inline-flex w-full py-2 justify-center text-sm font-medium text-pink-500 hover:text-pink-600"
                        type="button"
                        onClick={() => redirectAndClose(user.id)}
                      >
                        <ArrowRight size={22} />
                      </button>
                    </Link>
                  </div>
                </div>
              ))
            ) : (
              <div>
                {
                  isWaiting && !users.length ? (
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
                  ) : "Geen studenten gevonden"
                }
              </div>
            )
          }
        </div>

      </div>
    </Modal>
  );
}

export default ModalSkillTreeStudents;
