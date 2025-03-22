<template>
  <div id="globalHeader">
    <a-row class="headerGrid">
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
            <div>
              <img src="@/assets/logo.png" alt="" class="logo" />
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in currentRoutes" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div v-if="loginUserStore.loginUser.id">
          <div class="right-wrapper">
            <a-space>
              <img
                :src="loginUserStore.loginUser.userAvatar"
                class="avatarImg"
                alt=""
              />
              <p>{{ loginUserStore.loginUser.userName ?? "未登录" }}</p>
            </a-space>
          </div>
        </div>
        <div v-else>
          <a-button type="primary" @click="router.push('/user/login')">
            登录
          </a-button>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { routes } from "@/router/routes";
import { useLoginUserStore } from "@/store/userStore";

const router = useRouter();
const route = useRoute();
const loginUserStore = useLoginUserStore();

const selectedKeys = ref([route.path]);

const currentRoutes = routes.filter((item) => !item.meta?.hideInMenu);

router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path];
});

const doClick = (key: string) => {
  router.push(key);
};
</script>
<style scoped>
#globalHeader {
}

.logo {
  width: 64px;
  height: 64px;
}

.right-wrapper {
  display: flex;
}

.avatarImg {
  width: 64px;
  height: 64px;
}
</style>
