<template>
  <div id="doQuestionPage">
    <a-row gutter="[24,24]">
      <a-col span="12">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目描述">
            <a-card>
              <h1>{{ question.title }}</h1>
              <a-space style="margin-bottom: 16px">
                <a-tag
                  v-for="(tag, index) in question.tags"
                  :key="index"
                  color="arcoblue"
                  size="large"
                >
                  {{ tag }}
                </a-tag>
              </a-space>
              <a-descriptions
                title="判题条件"
                :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig?.timeLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig?.memoryLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig?.stackLimit ?? 0 }}
                </a-descriptions-item>
              </a-descriptions>
              <MdViewer :value="question.content" />
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论">评论区</a-tab-pane>
          <a-tab-pane key="answer" title="答案">暂时无法查看答案</a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col span="12">
        <a-form :model="form" layout="inline">
          <a-form-item
            field="language"
            label="编程语言"
            style="min-width: 240px"
          >
            <a-select
              placeholder="请选择编程语言"
              v-model="form.language"
              style="width: 320px"
            >
              <a-option>java</a-option>
              <a-option>cpp</a-option>
              <a-option>go</a-option>
            </a-select>
          </a-form-item>
        </a-form>
        <CodeEditor
          :value="form.code"
          :language="form.language"
          :handle-change="changeCode"
        />
        <div style="margin-top: 10px"></div>
        <a-button type="primary" @click="doSubmit">提交代码</a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, withDefaults, defineProps, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionSubmitControllerService,
  QuestionVO,
} from "../../../generated";
import { Message } from "@arco-design/web-vue";
import MdViewer from "@/components/editor/MdViewer.vue";
import CodeEditor from "@/components/editor/CodeEditor.vue";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const question = ref<QuestionVO>({});

const form = ref<QuestionSubmitAddRequest>({
  language: "",
  code: "",
});

const changeCode = (value: string) => {
  form.value.code = value;
};

const doSubmit = async () => {
  if (!question.value.id) {
    return;
  }
  const res = await QuestionSubmitControllerService.doQuestionSubmit({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    Message.success("提交成功");
  } else {
    Message.error("提交失败");
  }
};

/**
 * 刷新页面时加载数据
 */
const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoById(
    props.id as any
  );
  if (res.code === 0 && res.data) {
    question.value = res.data;
  } else {
    Message.error("加载题目失败，" + res.message);
  }
};

/**
 * 监听数据有变化时，重新加载数据
 */
watchEffect(() => {
  loadData();
});
</script>

<style scoped>
#doQuestionPage {
  margin: 0 auto;
  max-width: 1400px;
}
</style>
