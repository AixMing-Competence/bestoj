import router from "@/router";
import { useLoginUserStore } from "@/store/userStore";

router.beforeEach(async (to, from, next) => {
  const userStore = useLoginUserStore();
  const loginUser = userStore.loginUser;
  if (!loginUser || !loginUser.userRole) {
    await userStore.fetchLoginUser();
  }
  next();
});
