import { LoginUserVO } from "../../generated";
import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查权限（判断当前登录用户是否具有访问页面权限）
 * @param loginUser
 * @param needAccess
 */
const checkAccess = (loginUser: LoginUserVO, needAccess: string) => {
  const loginUserAccess = loginUser.userRole ?? ACCESS_ENUM.NOT_LOGIN;
  // 当前页面不需要登录
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }

  // 当前页面需要用户登录
  if (needAccess === ACCESS_ENUM.USER) {
    if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
      return false;
    }
  }

  // 当前页面需要管理员权限
  if (needAccess === ACCESS_ENUM.ADMIN) {
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false;
    }
  }
  return true;
};

export default checkAccess;
