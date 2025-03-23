import router from "@/router";
import { useLoginUserStore } from "@/store/userStore";
import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/accessCheck";
import { Message } from "@arco-design/web-vue";

router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore();
  // 获取当前登录用户
  let loginUser = loginUserStore.loginUser;
  // 第一次进入任何页面时会尝试自动登录（获取登录信息）一次
  // 之后若想改变 store 中的登录信息需要手动抓取
  if (!loginUser || !loginUser.userRole) {
    await loginUserStore.fetchLoginUser();
    loginUser = loginUserStore.loginUser;
  }
  const needAccess = (to.meta.access as string) ?? ACCESS_ENUM.NOT_LOGIN;
  // 要跳转的页面需要登录
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    // 如果没登录，跳转到登录页
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === ACCESS_ENUM.NOT_LOGIN
    ) {
      Message.info("请先登录");
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    // 已登录，检查权限，如果无权限跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      next("/noAuth");
      return;
    }
  }
  // 放行
  next();
});
