<template>
  <div class="app">
    <BasicLayout />
  </div>
</template>

<script lang="ts" setup>
import BasicLayout from "@/components/layouts/BasicLayout.vue";
import { onMounted } from "vue";

const doInit = () => {
  console.log("hello 欢迎来到我的项目");
};

onMounted(() => {
  doInit();
});
</script>

<style scoped></style>
