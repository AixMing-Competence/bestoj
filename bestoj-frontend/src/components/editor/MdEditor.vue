<template>
  <Editor
    :value="value"
    :mode="mode"
    :plugins="plugins"
    @change="handleChange"
  />
</template>

<script lang="ts" setup>
import { Editor } from "@bytemd/vue-next";
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { defineProps, ref, withDefaults } from "vue";

interface Props {
  value: string;
  mode?: string;
  handleChange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  mode: () => "split",
  handleChange: (v: string) => {
    console.log(v);
  },
});

const plugins = [
  gfm(),
  // Add more plugins here
  highlight(),
];

const value = ref("");

const handleChange = (v: string) => {
  value.value = v;
  props.handleChange(value.value);
};
</script>

<style>
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child {
  display: none;
}
</style>
