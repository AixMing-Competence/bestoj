<template>
  <div id="userLayout">
    <a-layout style="height: 100vh">
      <a-layout-header class="header">
        <img src="@/assets/logo.png" target="_blank" class="logo" />
      </a-layout-header>
      <a-layout-content class="content">
        <RouterView />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="https://github.com/AixMing-Competence" target="_blank">
          AixMing的github
        </a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
#userLayout {
  text-align: center;
  background: url(https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png)
    0% 0% / 100% 100%;
}

.header {
  margin-top: 100px;
}

.content {
  margin: 0 auto;
}

.footer {
  text-align: center;
  background: #f7f8fa;
}

.logo {
  height: 64px;
}
</style>
