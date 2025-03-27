<template>
  <div id="userRegisterPage">
    <h1>用户注册</h1>
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
      <a-form-item field="checkPassword" label="确认密码">
        <a-input-password
          v-model="form.checkPassword"
          placeholder="请输入确认密码"
        />
      </a-form-item>
      <a-form-item no-style>
        <div class="bottom-button-wrapper">
          <a-button html-type="submit" style="width: 120px" type="primary">
            注册
          </a-button>
          <a-button
            type="outline"
            style="width: 120px"
            @click="router.push('/user/login')"
          >
            去登录
          </a-button>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { UserControllerService, UserRegisterRequest } from "../../../generated";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";

const router = useRouter();

const form = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
} as UserRegisterRequest);

/**
 * 上传表单
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userRegister(form);
  if (res.code === 0) {
    Message.success("注册成功");
    router.push("/user/login");
  } else {
    Message.error("注册失败，" + res.message);
  }
};
</script>

<style scoped>
#userRegisterPage {
}

.bottom-button-wrapper {
  display: flex;
  justify-content: space-evenly;
}
</style>
