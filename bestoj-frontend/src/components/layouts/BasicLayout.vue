<template>
  <div class="basicLayout">
    <a-layout style="height: 400px">
      <a-layout-header>
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content>
        <RouterView />
      </a-layout-content>
      <a-layout-footer>
        <GlobalFooter />
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
import GlobalFooter from "@/components/GlobalFooter.vue";
</script>

<style scoped></style>
