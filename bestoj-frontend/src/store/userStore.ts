import { defineStore } from "pinia";
import { ref } from "vue";
import { LoginUserVO, UserControllerService } from "../../generated";
import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 登录用户信息全局状态
 */
export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<LoginUserVO>({
    userName: "未登录",
  });

  async function fetchLoginUser() {
    const res = await UserControllerService.getLoginUser();
    if (res.code === 0 && res.data) {
      loginUser.value = res.data;
    } else {
      loginUser.value = { userRole: ACCESS_ENUM.NOT_LOGIN };
    }
  }

  return { loginUser, fetchLoginUser };
});
