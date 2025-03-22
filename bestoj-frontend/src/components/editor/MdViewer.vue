<template>
  <Viewer :value="value" :plugins="plugins" readonly />
</template>

<script lang="ts" setup>
import gfm from "@bytemd/plugin-gfm";
import { Viewer } from "@bytemd/vue-next";
import highlight from "@bytemd/plugin-highlight";
import { withDefaults, defineProps } from "vue";

interface Props {
  value: string;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
});

const plugins = [
  gfm(),
  // Add more plugins here
  highlight(),
];
</script>
