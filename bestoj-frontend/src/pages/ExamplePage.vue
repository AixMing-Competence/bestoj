<template>
  <div id="examplePage">
    <h1>测试页面</h1>
    <CodeEditor :value="codeValue" :handle-change="onCodeChange" />
    <MdEditor :value="mdValue" :handle-change="onMdChange" />
  </div>
</template>

<script setup lang="ts">
import MdEditor from "@/components/editor/MdEditor.vue";
import { ref } from "vue";
import CodeEditor from "@/components/editor/CodeEditor.vue";

const mdValue = ref("");
const codeValue = ref("");

const onMdChange = (v: string) => {
  mdValue.value = v;
};

const onCodeChange = (v: string) => {
  codeValue.value = v;
};
</script>

<style scoped></style>
