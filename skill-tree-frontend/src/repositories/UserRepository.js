import Axios from "./Axios";

export default class UserRepository {
  static repository = Axios.axios;

  static url = Axios.url;

  static login(data) {
    return this.repository.post(`${this.url}/users/login`, data);
  }

  static logout() {
    return this.repository.post(`${this.url}/users/logout`);
  }

  static getSkillTreeFromStudent(studentId, skillTreeId) {
    return this.repository.get(`${this.url}/users/${studentId}/skilltree/${skillTreeId}`);
  }

  static getAllUsers() {
    return this.repository.get(`${this.url}/users`);
  }

  static postFeedback(data) {
    return this.repository.post(`${this.url}/users/feedback`, data);
  }
}
