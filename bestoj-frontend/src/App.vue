<template>
  <div class="app">
    <template v-if="route.path.startsWith('/user')">
      <RouterView />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<script lang="ts" setup>
import BasicLayout from "@/components/layouts/BasicLayout.vue";
import { onMounted } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();

const doInit = () => {
  console.log("hello 欢迎来到我的项目");
};

onMounted(() => {
  doInit();
});
</script>

<style scoped></style>
