<template>
  <div id="questionSubmitPage">
    <a-form :model="searchParams" @submit="doSearch" layout="inline">
      <a-form-item field="questionId" label="题号" style="min-width: 240px">
        <a-input v-model="searchParams.questionId" placeholder="请输入题号" />
      </a-form-item>
      <a-form-item field="language" label="编程语言" style="min-width: 240px">
        <a-select
          style="width: 320px"
          v-model="searchParams.language"
          placeholder="请选择编程语言"
        >
          <a-option>Java</a-option>
          <a-option>cpp</a-option>
          <a-option>JavaScript</a-option>
          <a-option>go</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit" type="primary">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      :columns="columns"
      :data="questionSubmitList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="doPageChange"
    >
      <template #judgeInfo="{ record }">
        {{ JSON.stringify(record.judgeInfo) }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button
            @click="router.push(`/question/description/${record.questionId}`)"
            type="primary"
          >
            做题
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import moment from "moment/moment";
import { ref, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionQueryRequest,
  QuestionSubmitControllerService,
  QuestionSubmitQueryRequest,
  QuestionSubmitVO,
} from "../../../generated";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";

const router = useRouter();

const questionSubmitList = ref([]);

/**
 * 分页参数
 */
const searchParams = ref({
  title: "",
  tags: [],
  pageSize: 10,
  current: 1,
} as QuestionSubmitQueryRequest);

const total = ref(0);

const doSearch = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await QuestionSubmitControllerService.listQuestionSubmitByPage(
    searchParams.value
  );
  if (res.code === 0) {
    questionSubmitList.value = res.data?.records ?? [];
    total.value = res.data?.total ?? 0;
    console.log("加载成功", questionSubmitList.value);
  } else {
    Message.error("加载数据失败，" + res.message);
  }
};

/**
 * 数据一旦发生改变，就再次加载数据
 */
watchEffect(() => {
  loadData();
});

/**
 * 切换分页的时候会触发数据的重新加载
 * @param page
 */
const doPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const columns = [
  {
    title: "提交号",
    dataIndex: "id",
  },
  {
    title: "编程语言",
    dataIndex: "language",
  },
  {
    title: "用户代码",
    dataIndex: "code",
  },
  {
    title: "判题信息",
    dataIndex: "judgeInfo",
    slotName: "judeInfo",
  },
  {
    title: "判题状态",
    dataIndex: "status",
  },
  {
    title: "题目 id",
    dataIndex: "questionId",
  },
  {
    title: "用户 iD",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];
</script>

<style scoped></style>
