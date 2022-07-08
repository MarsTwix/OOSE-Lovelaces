import { ArchivedOverview, Overview, SkillTree } from "../views/coordinator";
import Login from "../views/Login";
import Sidebar from "../components/Sidebar";
import Logout from "../views/Logout";
import AssessmentSkillTree from "../views/coordinator/AssessmentSkillTree";

export default [
  {
    path: "/",
    redirectTo: "/admin/overview",
  },
  {
    path: "/admin/overview",
    component: Overview,
    useGuard: true,
    accessibleBy: ["coordinator"],
    useNavigation: true,
    navigation: Sidebar,
  },
  {
    path: "/admin/overview/:id",
    component: SkillTree,
    useGuard: true,
    accessibleBy: ["coordinator"],
    useNavigation: true,
    navigation: Sidebar,
  },
  {
    path: "/overview/:id/:studentId",
    component: AssessmentSkillTree,
    useGuard: true,
    accessibleBy: ["docent"],
    useNavigation: true,
    navigation: Sidebar,
  },
  {
    path: "/admin/overview/archived",
    component: ArchivedOverview,
    useGuard: true,
    accessibleBy: ["coordinator"],
    useNavigation: true,
    navigation: Sidebar,
  },
  {
    path: "/login",
    component: Login,
    useGuard: false,
    useNavigation: false,
  },
  {
    path: "/logout",
    component: Logout,
    useGuard: true,
    useNavigation: false,
  },
];
