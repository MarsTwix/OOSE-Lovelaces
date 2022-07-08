import Axios from "./Axios";

export default class SkillTreeRepository {
  static repository = Axios.axios;

  static url = Axios.url;

  static setToken(token) {
    this.repository.defaults.headers.common.Authorization = `Bearer ${token}`;
  }

  static getSkillTrees() {
    return this.repository.get(`${this.url}/skilltrees`);
  }

  static getArchivedSkillTrees() {
    return this.repository.get(`${this.url}/skilltrees/archived`);
  }

  static getSkillTree(id) {
    return this.repository.get(`${this.url}/skilltrees/${id}`);
  }

  static postSkillTree(data) {
    return this.repository.post(`${this.url}/skilltrees`, { ...data });
  }

  static archiveSkillTree(id) {
    return this.repository.patch(`${this.url}/skilltrees/${id}/archive`);
  }

  static restoreSkillTree(id) {
    return this.repository.patch(`${this.url}/skilltrees/${id}/restore`);
  }

  static editSkillTree(id, data) {
    return this.repository.put(`${this.url}/skilltrees/${id}`, data);
  }

  static deleteSkillTree(id) {
    return this.repository.delete(`${this.url}/skilltrees/${id}`);
  }

  static postSkill(id, data) {
    return this.repository.post(`${this.url}/skilltrees/${id}/skills`, data);
  }

  static editSkill(id, skillId, data) {
    return this.repository.put(`${this.url}/skilltrees/${id}/skills/${skillId}`, data);
  }

  static archiveSkill(id, skillId) {
    return this.repository.patch(`${this.url}/skilltrees/${id}/skills/${skillId}`);
  }

  static postEdge(id, skillCouplingId) {
    return this.repository.post(`${this.url}/skilltrees/${id}/skills/attach`, skillCouplingId);
  }

  static removeEdge(id, skillCouplingId) {
    return this.repository.delete(`${this.url}/skilltrees/${id}/skills/${skillCouplingId}/detach`);
  }

  static getArchivedSkills(id) {
    return this.repository.get(`${this.url}/skilltrees/${id}/skills/archived`);
  }

  static putSkill(id, skillId, data) {
    return this.repository.put(`${this.url}/skilltrees/${id}/skills/${skillId}/reposition`, data);
  }

  static restoreSkill(id, skillId) {
    return this.repository.patch(`${this.url}/skilltrees/${id}/skills/${skillId}/restore`);
  }

  static deleteSkill(id, skillId) {
    return this.repository.delete(`${this.url}/skilltrees/${id}/skills/${skillId}`);
  }
}
