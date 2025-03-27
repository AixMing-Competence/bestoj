<template>
  <div id="globalHeader">
    <a-row align="center">
      <a-col flex="auto">
        <a-menu
          mode="horizontal"
          :selected-keys="selectedKeys"
          @menu-item-click="doClick"
        >
          <a-menu-item
            key="0"
            disabled
            :style="{ padding: 0, marginRight: '38px' }"
          >
            <div class="title-bar">
              <img src="@/assets/logo.png" alt="" class="logo" />
              <span>bestoj</span>
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in visibleRoutes" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div class="right-wrapper">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown @select="handleSelect">
              <a-space>
                <img
                  :src="loginUserStore.loginUser.userAvatar"
                  class="avatarImg"
                  alt=""
                />
                <p>{{ loginUserStore.loginUser.userName ?? "user_noName" }}</p>
              </a-space>
              <template #content>
                <a-doption @click="doLogout">
                  <template #icon>
                    <icon-import />
                  </template>
                  <template #default> 退出</template>
                </a-doption>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" @click="router.push('/user/login')">
              登录
            </a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { routes } from "@/router/routes";
import { useLoginUserStore } from "@/store/userStore";
import { IconImport } from "@arco-design/web-vue/es/icon";
import { UserControllerService } from "../../generated";
import { Message } from "@arco-design/web-vue";
import accessCheck from "@/access/accessCheck";

const router = useRouter();
const route = useRoute();
const loginUserStore = useLoginUserStore();

const selectedKeys = ref([route.path]);

/**
 * 显示没有隐藏的路由以及
 */
const visibleRoutes = computed(() => {
  console.log(loginUserStore.loginUser);
  return routes.filter(
    (item) =>
      !item.meta?.hideInMenu &&
      accessCheck(loginUserStore.loginUser, item.meta?.access as any)
  );
});

router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path];
});

const doClick = (key: string) => {
  router.push(key);
};

const doLogout = async () => {
  const res = await UserControllerService.userLogout();
  if (res.code === 0) {
    Message.success("退出登录");
    router.push("/user/login");
  } else {
    Message.error("退出登录失败，" + res.message);
  }
};
</script>
<style scoped>
#globalHeader {
}

.title-bar {
  display: flex;
  align-items: center;
  font-size: 24px;
  color: black;
}

.title-bar .logo {
  height: 48px;
  margin-right: 16px;
}

.avatarImg {
  height: 48px;
}

.right-wrapper {
  margin-right: 24px;
}
</style>
