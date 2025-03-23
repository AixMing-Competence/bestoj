<template>
  <div id="addQuestionPage">
    <h2 style="margin-bottom: 32px">设置题目</h2>
    <a-form :model="form" :style="{ width: '600px' }" @submit="handleSubmit">
      <a-form-item field="id" label="题目 id">
        {{ form.id }}
      </a-form-item>
      <a-form-item field="title" label="题目">
        <a-input v-model="form.title" placeholder="请输入标题"></a-input>
      </a-form-item>
      <a-form-item field="content" label="内容">
        <MdEditor :value="form.content" :handle-change="onContentChange" />
      </a-form-item>
      <a-form-item field="answer" label="答案">
        <MdEditor :value="form.answer" :handle-change="onAnswerChange" />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag v-model="form.tags" placeholder="请输入标签" allow-clear />
      </a-form-item>
      <a-form-item label="判题配置">
        <a-space direction="vertical" style="min-width: 400px">
          <a-form-item label="时间限制">
            <a-input-number
              v-model="form.judgeConfig.timeLimit"
              placeholder="请输入时间限制"
              mode="button"
              size="large"
              min="0"
            />
          </a-form-item>
          <a-form-item label="内存限制">
            <a-input-number
              v-model="form.judgeConfig.memoryLimit"
              placeholder="请输入内存限制"
              mode="button"
              size="large"
              min="0"
            />
          </a-form-item>
          <a-form-item label="堆栈限制">
            <a-input-number
              v-model="form.judgeConfig.stackLimit"
              placeholder="请输入堆栈限制"
              mode="button"
              size="large"
              min="0"
            />
          </a-form-item>
        </a-space>
      </a-form-item>
      <a-form-item label="测试用例配置" :content-flex="false">
        <a-form-item
          v-for="(judgeCaseItem, index) in form.judgeCase"
          :key="index"
          no-style
        >
          <a-space
            direction="vertical"
            style="min-width: 420px; margin-bottom: 16px"
          >
            <a-form-item :label="`输入样例-${index + 1}`">
              <a-input
                v-model="judgeCaseItem.input"
                placeholder="请输入测试输入样例"
              />
            </a-form-item>
            <a-form-item
              :field="`form.judgeCase[${index}].output`"
              :label="`输出样例-${index + 1}`"
            >
              <a-input
                v-model="judgeCaseItem.output"
                placeholder="请输入测试输出样例"
              />
            </a-form-item>
            <a-button status="danger" @click="handleDelete(index as any)">
              删除
            </a-button>
          </a-space>
        </a-form-item>
        <div style="margin-top: 16px">
          <a-button @click="handleAdd" type="outline" status="success">
            新增测试用例
          </a-button>
        </div>
      </a-form-item>
      <div style="margin-top: 16px"></div>
      <a-form-item>
        <a-button html-type="submit" type="primary"> 提交</a-button>
      </a-form-item>
    </a-form>
    {{ form }}
  </div>
</template>

<script lang="ts" setup>
import { defineProps, ref, watchEffect, withDefaults } from "vue";
import {
  QuestionAddRequest,
  QuestionControllerService,
  QuestionUpdateRequest,
} from "../../../generated";
import MdEditor from "@/components/editor/MdEditor.vue";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";

const router = useRouter();

interface Props {
  id?: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const form = ref<QuestionAddRequest>({
  judgeConfig: {},
  judgeCase: [],
});

/**
 * 修改题目时加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionVoById(
    props.id as any
  );
  if (res.code === 0) {
    form.value = res.data as any;
    if (form.value.judgeConfig) {
      form.value.judgeConfig.timeLimit = Number(
        form.value.judgeConfig.timeLimit
      );
      form.value.judgeConfig.memoryLimit = Number(
        form.value.judgeConfig.memoryLimit
      );
      form.value.judgeConfig.stackLimit = Number(
        form.value.judgeConfig.stackLimit
      );
    }
  } else {
    Message.error("加载数据失败，" + res.message);
  }
};

watchEffect(() => {
  if (!props.id) {
    return;
  }
  loadData();
});

/**
 * 删除测试用例
 * @param index
 */
const handleDelete = (index: number) => {
  form.value.judgeCase?.splice(index, 1);
};

/**
 * 添加测试用例
 */
const handleAdd = () => {
  form.value.judgeCase?.push({
    input: "",
    output: "",
  });
};

/**
 * 提交表单
 * @param data
 */
const handleSubmit = async () => {
  let res;
  // 如果是修改
  if (props.id) {
    res = await QuestionControllerService.updateQuestion(form.value);
  } else {
    res = await QuestionControllerService.addQuestion(form.value);
  }
  if (res.code === 0) {
    Message.success("设置成功");
    router.push("/");
  } else {
    Message.error("设置失败，" + res.message);
  }
};

const onContentChange = (v: string) => {
  form.value.content = v;
};

const onAnswerChange = (v: string) => {
  form.value.answer = v;
};
</script>

<style scoped>
#addQuestionPage {
}
</style>
