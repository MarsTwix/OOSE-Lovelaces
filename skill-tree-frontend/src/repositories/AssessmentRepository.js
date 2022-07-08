import Axios from "./Axios";

export default class AssessmentRepository {
  static repository = Axios.axios;

  static url = Axios.url;

  static postAssessment(data) {
    return this.repository.post(`${this.url}/assessments`, data);
  }
}
