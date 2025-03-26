import HomePage from "@/pages/HomePage.vue";
import UserLayout from "@/components/layouts/UserLayout.vue";
import UserLoginPage from "@/pages/user/UserLoginPage.vue";
import UserRegisterPage from "@/pages/user/UserRegisterPage.vue";
import ExamplePage from "@/pages/ExamplePage.vue";
import AddQuestionPage from "@/pages/question/AddQuestionPage.vue";
import ManageQuestionPage from "@/pages/question/ManageQuestionPage.vue";
import QuestionViewPage from "@/pages/question/ViewQuestionPage.vue";
import DoQuestionPage from "@/pages/question/doQuestionPage.vue";
import QuestionSubmitPage from "@/pages/question/QuestionSubmitPage.vue";
import NoAuth from "@/pages/auth/NoAuth.vue";
import ACCESS_ENUM from "@/access/accessEnum";

export const routes = [
  { path: "/", name: "主页", component: HomePage },
  {
    path: "/noAuth",
    name: "无权限页面",
    component: NoAuth,
    meta: {
      hideInMenu: true,
    },
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
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/question/add/:id",
    name: "更新题目",
    component: AddQuestionPage,
    props: true,
    meta: {
      hideInMenu: true,
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/question/manage",
    name: "管理题目",
    component: ManageQuestionPage,
    meta: {
      hideInMenu: false,
      access: ACCESS_ENUM.ADMIN,
    },
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
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/question_submit",
    name: "浏览题目并提交",
    component: QuestionSubmitPage,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/test",
    name: "测试页面",
    component: ExamplePage,
  },
];
