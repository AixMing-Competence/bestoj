<template>
  <div id="manageQuestionPage">
    <h2 style="margin-bottom: 32px">管理题目</h2>
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
            v-for="(tag, index) in JSON.parse(record.tags)"
            :key="index"
            color="green"
            closable
          >
            {{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #createTime="{ record }">
        {{ moment(record).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button @click="doUpdate(record)" type="outline" status="success">
            修改
          </a-button>
          <a-button @click="doDelete(record.id)" type="outline" status="danger">
            删除
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
} from "../../../generated";
import { Message } from "@arco-design/web-vue";
import moment from "moment";
import { useRouter } from "vue-router";

const router = useRouter();

const questionList = ref<Question[]>([]);

const searchParams = ref({
  pageSize: 10,
  current: 1,
} as QuestionQueryRequest);

const total = ref(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPage(
    searchParams.value
  );
  if (res.code === 0) {
    // 当前页面最后一条数据被删除的时候，要重新抓取
    const pageTotal = Math.ceil(
      (res.data?.total ?? 0) / (searchParams.value.pageSize ?? 1)
    );
    if (pageTotal > 0 && (searchParams.value.current ?? 1) > pageTotal) {
      searchParams.value.current = pageTotal;
      loadData();
    }
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
 * 删除数据
 */
const doDelete = async (id: number) => {
  const res = await QuestionControllerService.deleteQuestion({ id });
  if (res.code === 0) {
    Message.success("删除成功");
    loadData();
  } else {
    Message.error("删除失败，" + res.message);
  }
};

/**
 * 修改题目
 */
const doUpdate = (question: Question) => {
  router.push(`/question/add/${question.id}`);
};

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
    title: "用户 id",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "标签列表（json 数组）",
    dataIndex: "tags",
    slotName: "tags",
  },
  {
    title: "题目答案",
    dataIndex: "answer",
  },
  {
    title: "题目提交数",
    dataIndex: "submitNum",
  },
  {
    title: "题目通过数",
    dataIndex: "acceptedNum",
  },
  {
    title: "判题用例（json 数组）",
    dataIndex: "judgeCase",
  },
  {
    title: "判题配置（json 对象）",
    dataIndex: "judgeConfig",
  },
  {
    title: "点赞数",
    dataIndex: "thumbNum",
  },
  {
    title: "收藏数",
    dataIndex: "favourNum",
  },
  {
    title: "创建用户 id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
</script>

<style scoped>
#manageQuestionPage {
  margin: 0 auto;
  max-width: 1200px;
}
</style>
