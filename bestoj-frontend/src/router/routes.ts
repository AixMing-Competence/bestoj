import HomePage from "@/pages/HomePage.vue";
import UserLayout from "@/components/layouts/UserLayout.vue";
import UserLoginPage from "@/pages/user/UserLoginPage.vue";
import UserRegisterPage from "@/pages/user/UserRegisterPage.vue";
import ExamplePage from "@/pages/ExamplePage.vue";
import AddQuestionPage from "@/pages/question/AddQuestionPage.vue";
import ManageQuestionPage from "@/pages/question/ManageQuestionPage.vue";
import QuestionViewPage from "@/pages/question/ViewQuestionPage.vue";
import DoQuestionPage from "@/pages/question/doQuestionPage.vue";

export const routes = [
  { path: "/", name: "主页", component: HomePage },
  {
    path: "/test",
    name: "测试页面",
    component: ExamplePage,
  },
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    children: [
      {
        path: "login",
        name: "用户登录",
        component: UserLoginPage,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: UserRegisterPage,
      },
    ],
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/question/add",
    name: "创建题目",
    component: AddQuestionPage,
  },
  {
    path: "/question/add/:id",
    name: "更新题目",
    component: AddQuestionPage,
    props: true,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/question/manage",
    name: "管理题目",
    component: ManageQuestionPage,
  },
  {
    path: "/question",
    name: "浏览题目",
    component: QuestionViewPage,
  },
  {
    path: "/question/description/:id",
    name: "做题页面",
    component: DoQuestionPage,
    props: true,
    meta: {
      hideInMenu: true,
    },
  },
];
