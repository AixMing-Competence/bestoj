<template>
  <div id="userLoginPage">
    <h1>用户登录页</h1>
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
      <a-form-item field="userPassword" tooltip="密码不少于8位" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit" style="width: 120px" type="primary">
          Submit
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../generated";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";

const router = useRouter();

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
    Message.success("登录成功");
    router.push("/");
  } else {
    Message.error("登录失败，" + res.message);
  }
};
</script>

<style scoped>
#userLoginPage {
}
</style>
