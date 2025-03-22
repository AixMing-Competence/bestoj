<template>
  <div id="viewQuestionPage">
    <h2>搜索题目</h2>
    {{ searchParams }}
    <a-form :model="searchParams" @submit="doSearch" layout="inline">
      <a-form-item field="title" label="标题" style="min-width: 240px">
        <a-input v-model="searchParams.title" placeholder="请输入标题" />
      </a-form-item>
      <a-form-item field="tags" label="标签" style="min-width: 240px">
        <a-input-tag v-model="searchParams.tags" placeholder="请输入标签" />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit" type="primary">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      :columns="columns"
      :data="questionList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="doPageChange"
    >
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag
            v-for="(tag, index) in record.tags"
            :key="index"
            color="green"
            closable
          >
            {{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${record.submitNum ? record.acceptedNum / record.submitNum : 0}%(${
            record.acceptedNum
          }/${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button
            @click="router.push(`/question/description/${record.id}`)"
            type="primary"
          >
            做题
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { ref, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionQueryRequest,
  QuestionVO,
} from "../../../generated";
import { Message } from "@arco-design/web-vue";
import moment from "moment";
import { useRouter } from "vue-router";

const router = useRouter();

const questionList = ref<QuestionVO[]>([]);

/**
 * 分页参数
 */
const searchParams = ref({
  title: "",
  tags: [],
  pageSize: 10,
  current: 1,
} as QuestionQueryRequest);

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
  const res = await QuestionControllerService.listQuestionVoByPage(
    searchParams.value
  );
  if (res.code === 0) {
    questionList.value = res.data?.records ?? [];
    total.value = res.data?.total ?? 0;
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
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "标签",
    dataIndex: "tags",
    slotName: "tags",
  },
  {
    title: "通过率",
    slotName: "acceptedRate",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];
</script>

<style scoped>
#viewQuestionPage {
  margin: 0 auto;
}
</style>
