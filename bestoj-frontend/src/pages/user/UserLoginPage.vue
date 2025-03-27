<template>
  <div id="userLoginPage">
    <h1>用户登录</h1>
    <a-form
      :model="form"
      style="width: 400px; margin: 0 auto"
      @submit="handleSubmit"
      label-align="left"
      auto-label-width
    >
      <a-form-item field="userAccount" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item no-style>
        <div class="bottom-button-wrapper">
          <a-button html-type="submit" style="width: 120px" type="primary">
            登录
          </a-button>
          <a-button
            type="outline"
            style="width: 120px"
            @click="router.push('/user/register')"
          >
            去注册
          </a-button>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../generated";
import { Message } from "@arco-design/web-vue";
import { useRoute, useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/userStore";

const router = useRouter();

const route = useRoute();

const loginUserStore = useLoginUserStore();

const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);

/**
 * 上传表单
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userLogin(form);
  if (res.code === 0) {
    // 更新 store 中的用户登录信息
    await loginUserStore.fetchLoginUser();
    Message.success("登录成功");
    router.replace(`${route.query.redirect ?? "/"}`);
  } else {
    Message.error("登录失败，" + res.message);
  }
};
</script>

<style scoped>
#userLoginPage {
}

.bottom-button-wrapper {
  display: flex;
  justify-content: space-evenly;
}
</style>
