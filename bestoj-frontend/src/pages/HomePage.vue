<template>
  <div id="homePage">
    <h1>主页</h1>
    <MdEditor :value="value" :handle-change="onChange" />
  </div>
</template>

<script setup lang="ts">
import MdEditor from "@/components/editor/MdEditor.vue";
import { ref } from "vue";

const value = ref("");
const onChange = (v: string) => {
  value.value = v;
};
</script>

<style scoped></style>
